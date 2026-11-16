///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21+
//DEPS com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.2
//DEPS org.aesh:aesh:3.19.1

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.aesh.AeshRuntimeRunner;
import org.aesh.command.Command;
import org.aesh.command.CommandDefinition;
import org.aesh.command.CommandException;
import org.aesh.command.CommandResult;
import org.aesh.command.invocation.CommandInvocation;
import org.aesh.command.option.Argument;
import org.aesh.command.option.Option;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Packages a v3 mixin Kit with Podman, without BuildKit or the Kit frontend.
 * <p>
 * Reads <kit-dir>/<kit_name>.yaml, builds <kit_name>.dockerfile, stages the sources
 * under /usr/share/sandbox/kit/<kit_name>/ and writes the manifest annotations
 * (SPEC-v3 §9.3 and §10).
 * <p>
 * Usage: jbang BuildMixinKit.java <kit-dir> <kit_name>
 * With the spec checkout, the result is validated by `task tck:kit:layout`.
 */
@CommandDefinition(name = "build", description = "Builds a mixin sbx kit", generateHelp = true)
public class BuildMixinKit implements Command<CommandInvocation> {

    final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());
    final ObjectMapper JSON = new ObjectMapper();
    final String KIT = "vnd.docker.sandbox.kit.";
    final String OCI = "org.opencontainers.image.";

    @Option(shortName = 'n', name = "name", description = "The kit image name", defaultValue = "")
    private String imageName;

    @Argument(description = "Kit directory", required = true)
    private Path kitDir;

    private CommandInvocation invocation;

    public static void main(String[] args) throws Exception {
        AeshRuntimeRunner.builder()
                .command(BuildMixinKit.class)
                .args(args)
                .execute();
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws CommandException, InterruptedException {
        this.invocation = commandInvocation;
        imageName = imageName.isEmpty() ? kitDir.getFileName().toString() : imageName;
        String dockerFile = kitDir.resolve("Dockerfile").toString();
        String kitFile = "spec.yaml";
        JsonNode descriptor = null;
        try {
            descriptor = YAML.readTree(kitDir.resolve(kitFile).toFile());
            checkSupported(descriptor, kitDir.resolve(dockerFile));

            String contentRef = imageName + "-content:dev";
            String kitRef = imageName + ":dev";
            String tmpDir = imageName.substring(imageName.lastIndexOf("/")+1);
            invocation.println(tmpDir);
            Path work = Files.createTempDirectory(tmpDir + "-kit-");
            invocation.println("Working directory (kept if the build fails): " + work);

            buildContentImage(kitDir, dockerFile, contentRef);
            Path staging = stageSources(kitDir, imageName, dockerFile, descriptor, work);
            buildKit(kitRef, contentRef, annotations(descriptor), staging);
            deleteRecursively(work);
            return CommandResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e);
        }
    }

    /**
     * Refuses what only the real frontend handles: workloads and build-phase args.
     */
    void checkSupported(JsonNode d, Path dockerfile) {
        require("3".equals(d.path("schemaVersion").asText()), "schemaVersion must be \"3\"");
        require("mixin".equals(d.path("kind").asText()),
                "only kind: mixin is supported (workloads need derived deb/apk provides)");
        require(Files.isRegularFile(dockerfile), "missing companion recipe " + dockerfile);
        for (JsonNode arg : d.path("args")) {
            require(!arg.has("buildArg"), "build-phase args (buildArg) are not supported");
        }
        boolean hasVersion = d.has("version");
        for (JsonNode p : d.path("provides")) {
            require(hasVersion || p.asText().contains("@"), "provides entry needs a version: " + p.asText());
        }
    }

    void buildContentImage(Path kitDir, String dockerFile, String contentRef) throws Exception {
        invocation.println("Building content image " + contentRef);
        run(kitDir, "podman", "build", "--format", "oci",
                "--tag", contentRef,
                "--file", dockerFile,
                ".");
    }

    /**
     * kit.yaml (published descriptor), kit.dockerfile (recipe) and the packaging Containerfile.
     */
    Path stageSources(Path kitDir, String kitName, String dockerFile, JsonNode descriptor, Path work) throws Exception {
        Path staging = Files.createDirectory(work.resolve("staging"));
        Files.writeString(staging.resolve("kit.yaml"), JSON.writeValueAsString(descriptor));
        Files.copy(kitDir.resolve(dockerFile), staging.resolve("kit.dockerfile"));
        Files.writeString(staging.resolve("Containerfile"), """
                ARG CONTENT_IMAGE
                FROM ${CONTENT_IMAGE}
                COPY . /usr/share/sandbox/kit/%s/
                """.formatted(kitName));
        return staging;
    }

    /**
     * SPEC-v3 §9.3, minus built-by: this tool is not the Kit frontend.
     */
    Map<String, String> annotations(JsonNode d) throws Exception {
        Map<String, String> a = new LinkedHashMap<>();
        a.put(KIT + "descriptor", JSON.writeValueAsString(d));
        a.put(KIT + "schema-version", d.path("schemaVersion").asText());
        a.put(KIT + "capabilities", stream(d.path("capabilities"))
                .map(c -> c.path("type").asText()).distinct().sorted()
                .collect(Collectors.joining(",")));
        a.put(OCI + "title", d.path("displayName").asText());
        a.put(OCI + "description", d.path("description").asText());
        a.put(OCI + "authors", d.path("author").asText());
        a.put(OCI + "source", d.path("sourceUrl").asText());
        a.put(OCI + "licenses", stream(d.path("licenses")).map(JsonNode::asText)
                .collect(Collectors.joining(",")));
        a.put(OCI + "version", d.path("version").asText());
        a.values().removeIf(String::isEmpty);   // empty fields emit no key
        return a;
    }

    void buildKit(String kitRef, String contentRef, Map<String, String> annotations, Path staging)
            throws Exception {
        invocation.println("Building Kit " + kitRef + " with " + annotations.size() + " annotations");
        List<String> cmd = new ArrayList<>(List.of("podman", "build", "--format", "oci",
                "--build-arg", "CONTENT_IMAGE=" + contentRef));
        annotations.forEach((key, value) -> {
            cmd.add("--annotation");
            cmd.add(key + "=" + value);
        });
        cmd.addAll(List.of("--tag", kitRef, "--file", "Containerfile", "."));
        run(staging, cmd.toArray(String[]::new));
    }

    // --- helpers -------------------------------------------------------------

    void run(Path dir, String... cmd) throws Exception {
        int rc = new ProcessBuilder(cmd).directory(dir.toFile()).inheritIO().start().waitFor();
        if (rc != 0) throw new IllegalStateException("Failed (" + rc + "): " + String.join(" ", cmd));
    }

    Stream<JsonNode> stream(JsonNode array) {
        return StreamSupport.stream(array.spliterator(), false);
    }

    void require(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }

    void deleteRecursively(Path root) throws Exception {
        try (Stream<Path> files = Files.walk(root)) {
            for (Path p : files.sorted(Comparator.reverseOrder()).toList()) Files.delete(p);
        }
    }
}