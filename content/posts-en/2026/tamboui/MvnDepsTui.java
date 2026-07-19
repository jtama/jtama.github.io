///usr/bin/env jbang "$0" "$@" ; exit $?
//REPOS mavencentral,sonatype-snapshots=https://central.sonatype.com/repository/maven-snapshots/
//DEPS eu.maveniverse.maven.mima:context:2.4.46
//DEPS eu.maveniverse.maven.mima.runtime:standalone-static:2.4.46
//DEPS eu.maveniverse.maven.mima.extensions:mmr:2.4.46
//DEPS org.slf4j:slf4j-nop:2.0.16
//DEPS dev.tamboui:tamboui-toolkit:0.5.0-SNAPSHOT
//DEPS dev.tamboui:tamboui-jline3-backend:0.5.0-SNAPSHOT
//NATIVE_OPTIONS --no-fallback -H:+ReportExceptionStackTraces


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import eu.maveniverse.maven.mima.context.Context;
import eu.maveniverse.maven.mima.context.ContextOverrides;
import eu.maveniverse.maven.mima.context.Runtimes;
import eu.maveniverse.maven.mima.extensions.mmr.MavenModelReader;
import eu.maveniverse.maven.mima.extensions.mmr.ModelRequest;
import eu.maveniverse.maven.mima.extensions.mmr.ModelResponse;

import org.apache.maven.model.InputLocation;
import org.apache.maven.model.Model;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.eclipse.aether.util.graph.manager.DependencyManagerUtils;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;

import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.terminal.Frame;
import dev.tamboui.toolkit.app.ToolkitRunner;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.element.RenderContext;
import dev.tamboui.toolkit.element.Size;
import dev.tamboui.toolkit.element.StyledElement;
import dev.tamboui.toolkit.elements.TreeElement;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.toolkit.event.GlobalEventHandler;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widgets.input.TextInputState;
import dev.tamboui.widgets.tree.GuideStyle;
import dev.tamboui.widgets.tree.TreeNode;
import dev.tamboui.widgets.tree.TreeWidget;

import static dev.tamboui.toolkit.Toolkit.*;

/**
 * Visualizes the Maven dependency tree of a project, resolved in pure Java via
 * <a href="https://github.com/maveniverse/mima">MIMA</a> (no {@code mvn} subprocess).
 * <p>
 * Level 1: the tree structure (equivalent to {@code dependency:tree}).
 * Level 2: the version/scope as they would have been without {@code dependencyManagement},
 * shown in the side panel (equivalent to {@code help:effective-pom -Dverbose}).
 * <p>
 * Usage: {@code jbang MvnDepsTui.java [path/to/pom.xml]}
 */
public class MvnDepsTui {

    public static void main(String[] args) throws Exception {
        Path pomFile = Paths.get(args.length > 0 ? args[0] : "pom.xml").toAbsolutePath();
        if (!Files.isRegularFile(pomFile)) {
            System.err.println("pom file not found: " + pomFile);
            System.exit(1);
        }

        System.out.println("Resolving Maven dependencies for " + pomFile + "...");
        TreeNode<DependencyInfo> root;
        try {
            root = resolveDependencyTree(pomFile);
        } catch (Exception e) {
            System.err.println("Failed to resolve dependencies: " + e.getMessage());
            System.exit(1);
            return;
        }

        MvnDepsView view = new MvnDepsView(root);
        try (ToolkitRunner runner = ToolkitRunner.builder().build()) {
            // Global handler: runs before the tree widget gets a turn, so it can intercept
            // '/', Enter and Escape for the search bar before TreeElement's own key handling
            // (which processes Enter/arrows unconditionally, regardless of framework focus).
            GlobalEventHandler searchHandler = event ->
                    event instanceof KeyEvent ke ? view.handleGlobalKey(ke) : EventResult.UNHANDLED;
            runner.eventRouter().addGlobalHandler(searchHandler);
            runner.run(() -> view);
        }
    }

    /**
     * Builds the dependency tree via MIMA: effective POM (mmr) then full transitive
     * resolution (Aether), with the "premanaged" version/scope annotations enabled
     * to recover what dependencyManagement changed.
     */
    static TreeNode<DependencyInfo> resolveDependencyTree(Path pomFile) throws Exception {
        try (Context context = Runtimes.INSTANCE
                .getRuntime()
                .create(ContextOverrides.create().withUserSettings(true).build())) {

            MavenModelReader reader = new MavenModelReader(context);
            ModelResponse response = reader.readModel(
                    ModelRequest.builder().setPomFile(pomFile).build());
            Model effectiveModel = response.getEffectiveModel();
            ArtifactDescriptorResult adr = response.toArtifactDescriptorResult(effectiveModel);

            CollectRequest collectRequest = new CollectRequest();
            collectRequest.setRootArtifact(adr.getArtifact());
            collectRequest.setDependencies(adr.getDependencies());
            collectRequest.setManagedDependencies(adr.getManagedDependencies());
            collectRequest.setRepositories(context.remoteRepositories());

            DefaultRepositorySystemSession verboseSession =
                    new DefaultRepositorySystemSession(context.repositorySystemSession());
            verboseSession.setConfigProperty(ConflictResolver.CONFIG_PROP_VERBOSE, true);
            verboseSession.setConfigProperty(DependencyManagerUtils.CONFIG_PROP_VERBOSE, true);

            DependencyNode aetherRoot = context.repositorySystem()
                    .collectDependencies(verboseSession, collectRequest)
                    .getRoot();

            Map<String, org.apache.maven.model.Dependency> managementIndex = buildManagementIndex(effectiveModel);
            return buildTreeNode(aetherRoot, effectiveModel, managementIndex);
        }
    }

    /**
     * Indexes the effective dependencyManagement by {@code groupId:artifactId:type:classifier}
     * (Maven's own matching key), so that for a given dependency we can find which
     * model (POM) provided the winning version — the equivalent of the
     * {@code <!-- groupId:artifactId:version, line N -->} comment from {@code help:effective-pom -Dverbose}.
     */
    static Map<String, org.apache.maven.model.Dependency> buildManagementIndex(Model effectiveModel) {
        Map<String, org.apache.maven.model.Dependency> index = new HashMap<>();
        if (effectiveModel.getDependencyManagement() != null) {
            for (org.apache.maven.model.Dependency dep : effectiveModel.getDependencyManagement().getDependencies()) {
                index.put(managementKey(dep.getGroupId(), dep.getArtifactId(), dep.getType(), dep.getClassifier()), dep);
            }
        }
        return index;
    }

    static String managementKey(String groupId, String artifactId, String type, String classifier) {
        String normalizedType = (type == null || type.isEmpty()) ? "jar" : type;
        String normalizedClassifier = classifier == null ? "" : classifier;
        return groupId + ":" + artifactId + ":" + normalizedType + ":" + normalizedClassifier;
    }

    static TreeNode<DependencyInfo> buildTreeNode(
            DependencyNode aetherRoot, Model effectiveModel, Map<String, org.apache.maven.model.Dependency> managementIndex) {
        DependencyInfo rootInfo = new DependencyInfo(
                effectiveModel.getGroupId(),
                effectiveModel.getArtifactId(),
                effectiveModel.getVersion(),
                effectiveModel.getPackaging(),
                null, null, false, null, null, null, -1);
        TreeNode<DependencyInfo> rootNode = TreeNode.of(rootInfo.coordinates(), rootInfo);
        for (DependencyNode child : aetherRoot.getChildren()) {
            rootNode.add(convert(child, managementIndex));
        }
        return rootNode.expanded();
    }

    static TreeNode<DependencyInfo> convert(DependencyNode node, Map<String, org.apache.maven.model.Dependency> managementIndex) {
        Artifact artifact = node.getArtifact();
        Dependency dependency = node.getDependency();
        String premanagedVersion = (String) node.getData().get(DependencyManagerUtils.NODE_DATA_PREMANAGED_VERSION);

        String managedByModelId = null;
        int managedByLine = -1;
        if (premanagedVersion != null && !premanagedVersion.equals(artifact.getVersion())) {
            org.apache.maven.model.Dependency managed = managementIndex.get(
                    managementKey(artifact.getGroupId(), artifact.getArtifactId(), artifact.getExtension(), artifact.getClassifier()));
            if (managed != null) {
                InputLocation location = managed.getLocation("version");
                if (location != null) {
                    managedByLine = location.getLineNumber();
                    managedByModelId = location.getSource() != null ? location.getSource().getModelId() : null;
                }
            }
        }

        DependencyInfo info = new DependencyInfo(
                artifact.getGroupId(),
                artifact.getArtifactId(),
                artifact.getVersion(),
                artifact.getExtension(),
                artifact.getClassifier(),
                dependency != null ? dependency.getScope() : null,
                dependency != null && dependency.isOptional(),
                premanagedVersion,
                (String) node.getData().get(DependencyManagerUtils.NODE_DATA_PREMANAGED_SCOPE),
                managedByModelId,
                managedByLine);

        TreeNode<DependencyInfo> treeNode = TreeNode.of(info.coordinates(), info);
        List<DependencyNode> children = node.getChildren();
        if (children.isEmpty()) {
            return treeNode.leaf();
        }
        for (DependencyNode child : children) {
            treeNode.add(convert(child, managementIndex));
        }
        return treeNode.expanded();
    }

    /** Data displayed for a dependency (or the root project, with {@code scope() == null}). */
    record DependencyInfo(
            String groupId,
            String artifactId,
            String version,
            String extension,
            String classifier,
            String scope,
            boolean optional,
            String premanagedVersion,
            String premanagedScope,
            String managedByModelId,
            int managedByLine) {

        String coordinates() {
            String base = groupId + ":" + artifactId;
            if (classifier != null && !classifier.isEmpty()) {
                base += ":" + classifier;
            }
            return base + ":" + version;
        }

        boolean versionManaged() {
            return premanagedVersion != null && !premanagedVersion.equals(version);
        }

        boolean scopeManaged() {
            return premanagedScope != null && !premanagedScope.equals(scope);
        }
    }

    private static Color scopeColor(String scope) {
        if (scope == null) {
            return Color.WHITE;
        }
        return switch (scope) {
            case "compile" -> Color.GREEN;
            case "provided" -> Color.CYAN;
            case "runtime" -> Color.YELLOW;
            case "test" -> Color.MAGENTA;
            case "system" -> Color.RED;
            default -> Color.WHITE;
        };
    }

    /**
     * Main view: tree + detail panel + vim-style search bar ({@code /}, {@code n}/{@code N}).
     * Implements {@code Element} directly to intercept keys before delegating to the tree.
     */
    static final class MvnDepsView implements Element {

        private final TreeElement<DependencyInfo> tree;
        private final TextInputState searchState = new TextInputState();
        private boolean searchMode = false;
        private String activeQuery;

        MvnDepsView(TreeNode<DependencyInfo> root) {
            this.tree = tree(root)
                    .title("Maven Dependencies")
                    .rounded()
                    .highlightColor(Color.CYAN)
                    .scrollbar()
                    .guideStyle(GuideStyle.UNICODE)
                    .nodeRenderer(MvnDepsView::renderNode);
        }

        private static StyledElement<?> renderNode(TreeNode<DependencyInfo> node) {
            DependencyInfo info = node.data();
            if (info == null) {
                return text(node.label()).fit();
            }
            List<Element> parts = new ArrayList<>();
            parts.add(text(info.groupId() + ":" + info.artifactId() + ":").fit());
            parts.add(text(info.version()).fg(scopeColor(info.scope())).bold().fit());
            if (info.versionManaged()) {
                parts.add(text("  (unmanaged: " + info.premanagedVersion() + ")").yellow().italic().fit());
            }
            parts.add(spacer());
            if (info.scope() != null) {
                parts.add(text("[" + info.scope() + "]").dim().fit());
            }
            if (info.optional()) {
                parts.add(text(" optional").dim().italic().fit());
            }
            parts.add(text(" ").fit());
            return row(parts.toArray(new Element[0]));
        }

        private Element renderDetails(DependencyInfo info) {
            List<Element> lines = new ArrayList<>();
            lines.add(row(text("GroupId    : ").bold().fit(), text(info.groupId()).fit()));
            lines.add(row(text("ArtifactId : ").bold().fit(), text(info.artifactId()).fit()));
            lines.add(row(text("Version    : ").bold().fit(), text(info.version()).fg(scopeColor(info.scope())).fit()));
            lines.add(row(text("Type       : ").bold().fit(), text(info.extension()).fit()));
            if (info.classifier() != null && !info.classifier().isEmpty()) {
                lines.add(row(text("Classifier : ").bold().fit(), text(info.classifier()).fit()));
            }
            lines.add(row(text("Scope      : ").bold().fit(),
                    text(info.scope() == null ? "(project root)" : info.scope()).fit()));
            lines.add(row(text("Optional   : ").bold().fit(), text(info.optional() ? "yes" : "no").fit()));
            lines.add(text(""));
            lines.add(text("Version management (dependencyManagement)").bold().cyan());
            if (info.versionManaged()) {
                lines.add(text("  Unmanaged version: " + info.premanagedVersion()).dim());
                lines.add(text("  Resolved version: " + info.version()).green());
                if (info.managedByModelId() != null) {
                    String suffix = info.managedByLine() > 0 ? ", line " + info.managedByLine() : "";
                    lines.add(text("  Managed by: " + info.managedByModelId() + suffix).cyan());
                } else {
                    lines.add(text("  Unknown management origin").dim());
                }
            } else {
                lines.add(text("  Version not modified by dependencyManagement").dim());
            }
            if (info.scopeManaged()) {
                lines.add(text("  Unmanaged scope: " + info.premanagedScope()).dim());
            }
            return column(lines.toArray(new Element[0]));
        }

        private Element renderSearchBar() {
            return row(
                    text(" / ").bold().yellow().fit(),
                    textInput(searchState)
                            .focusable(false)
                            .cursorRequiresFocus(false)
                            .placeholder("search for a dependency...")
            ).length(1);
        }

        private Element renderHelpBar() {
            return row(
                    text(" [/] Search  [n/N] Next/Prev match  [↑↓←→] Navigate  [q] Quit ")
                            .dim().fit(),
                    spacer(),
                    (activeQuery != null
                            ? text(" Search: \"" + activeQuery + "\" ").cyan()
                            : text("")).fit()
            ).length(1);
        }

        @Override
        public void render(Frame frame, Rect area, RenderContext context) {
            TreeNode<DependencyInfo> selectedNode = tree.selectedNode();
            DependencyInfo selected = selectedNode != null ? selectedNode.data() : null;

            Element details = panel(
                    column(
                            text("Details").bold().cyan(),
                            text(""),
                            selected != null ? renderDetails(selected) : text("(no selection)").dim()
                    )
            ).title("Details").rounded().borderColor(Color.DARK_GRAY).fill();

            Element ui = column(
                    row(tree.fill(2), details).fill(),
                    searchMode ? renderSearchBar() : renderHelpBar()
            );
            ui.render(frame, area, context);
        }

        @Override
        public Size preferredSize(int availableWidth, int availableHeight, RenderContext context) {
            return Size.UNKNOWN;
        }

        @Override
        public dev.tamboui.layout.Constraint constraint() {
            return dev.tamboui.layout.Constraint.fill();
        }

        /**
         * Called from a global handler (registered in {@code main()}) instead of via
         * {@link Element#handleKeyEvent}: the tree widget is always rendered (and thus
         * registered with the event router) before this view, so it always gets first
         * refusal on a key in the normal per-element routing — and {@code TreeElement}
         * processes navigation keys unconditionally, regardless of framework focus.
         * A global handler is the only hook that runs before that.
         */
        EventResult handleGlobalKey(KeyEvent event) {
            if (searchMode) {
                if (event.isCancel()) {
                    searchMode = false;
                    return EventResult.HANDLED;
                }
                if (event.isConfirm()) {
                    searchMode = false;
                    activeQuery = searchState.text();
                    jumpToMatch(activeQuery, true);
                    return EventResult.HANDLED;
                }
                handleTextInputKey(searchState, event);
                return EventResult.HANDLED;
            }
            if (event.isChar('/')) {
                searchMode = true;
                searchState.clear();
                return EventResult.HANDLED;
            }
            if (activeQuery != null && event.isChar('n')) {
                jumpToMatch(activeQuery, true);
                return EventResult.HANDLED;
            }
            if (activeQuery != null && event.isChar('N')) {
                jumpToMatch(activeQuery, false);
                return EventResult.HANDLED;
            }
            return EventResult.UNHANDLED;
        }

        private void jumpToMatch(String query, boolean forward) {
            if (query == null || query.isBlank()) {
                return;
            }
            String needle = query.toLowerCase(Locale.ROOT);
            List<TreeWidget.FlatEntry<TreeNode<DependencyInfo>>> entries = tree.lastFlatEntries();
            int total = entries.size();
            if (total == 0) {
                return;
            }
            int start = tree.selected();
            for (int step = 1; step <= total; step++) {
                int idx = forward ? Math.floorMod(start + step, total) : Math.floorMod(start - step, total);
                DependencyInfo info = entries.get(idx).node().data();
                if (info != null && info.coordinates().toLowerCase(Locale.ROOT).contains(needle)) {
                    tree.selected(idx);
                    return;
                }
            }
        }
    }
}
