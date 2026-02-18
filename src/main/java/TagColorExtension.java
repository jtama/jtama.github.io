import io.quarkiverse.roq.frontmatter.runtime.model.Page;
import io.quarkus.qute.TemplateExtension;

import java.util.List;
import java.util.Map;

public class TagColorExtension {

    /**
     * Language flag mapping for nice display.
     */
    private static final List<String> COLORS = List.of(
            "text-green-600",
            "text-blue-600",
            "text-red-400",
            "text-orange-500",
            "text-violet-400"
    );

    @TemplateExtension
    public static String tagColor(Integer index) {
        return COLORS.get(index % COLORS.size());
    }
}
