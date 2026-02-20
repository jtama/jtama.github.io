import io.quarkiverse.roq.frontmatter.runtime.model.Page;
import io.quarkiverse.roq.frontmatter.runtime.model.RoqUrl;
import io.quarkus.qute.TemplateExtension;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Template extension for the multilingual posts.
 * Provides methods to access multilingual information from Qute templates.
 */
public class I18NTemplateExtension {

    // Frontmatter property names
    private static final String LANG = "lang";
    private static final String DOCUMENT_KEY = "key";

    /**
     * Checks if the given page has translations available.
     *
     * @param page the page to check
     * @return <code>true</code> if the page has translations, <code>false</code> otherwise
     */
    @TemplateExtension
    public static boolean hasTranslations(Page page) {
        return getMultilingualData(page).findAny().isPresent();
    }

    /**
     * Returns the list of available languages for the given page.
     * Each language object contains the language code, flag emoji, and document URL.
     *
     * @param page the page to get languages for
     * @return a list of language instance, or an empty list if no multilingual data is available
     */
    @TemplateExtension
    public static List<Translation> languages(Page page) {
        return getMultilingualData(page)
                .map(Translation::fromPage)
                .toList();
    }

    /**
     * Returns the current language code for the given page.
     *
     * @param page the page to get the current language for
     * @return the current language code, or null if no multilingual data is available
     */
    @TemplateExtension
    public static String currentLanguage(Page page) {
        return page.data().getString(LANG);
    }

    /**
     * Helper method to extract multilingual data from a page.
     *
     * @param page the page to extract data from
     * @return the multilingual data object, or null if not available
     */
    private static Stream<Page> getMultilingualData(Page page) {
        String translationId = page.data().getString(DOCUMENT_KEY);
        if (translationId == null) {
            return Stream.of();
        }
        return page.site().allPages()
                .stream()
                .filter(doc -> translationId.equals(doc.data(DOCUMENT_KEY)))
                .filter(doc -> !doc.equals(page));
        //.filter(distinctByKey(Page::id));
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public record Translation(String code, String flag, RoqUrl url) {
        static Translation fromPage(Page page) {
            String languageCode = page.data().getString(LANG, "fr")
                    .toLowerCase()
                    .trim();
            return new Translation(languageCode, Language.fromCode(languageCode).flag(), page.url());
        }
    }
}