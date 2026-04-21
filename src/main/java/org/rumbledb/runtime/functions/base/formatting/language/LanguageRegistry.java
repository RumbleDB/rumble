package org.rumbledb.runtime.functions.base.formatting.language;

import java.util.HashMap;
import java.util.Map;

public final class LanguageRegistry {

    private static final Map<String, LanguageFormatter> FORMATTERS = new HashMap<>();

    static {
        register(new EnglishFormatter());
    }

    private LanguageRegistry() {
    }

    public static void register(LanguageFormatter formatter) {
        FORMATTERS.put(
            LanguageSupport.normalizeLanguageForLookup(formatter.getLanguage()),
            formatter
        );
    }

    public static LanguageFormatter resolve(String language) {
        String normalized = LanguageSupport.normalizeLanguageForLookup(language);

        LanguageFormatter formatter = FORMATTERS.get(normalized);
        if (formatter != null) {
            return formatter;
        }

        String primary = LanguageSupport.getPrimaryLanguageSubtag(language);
        formatter = FORMATTERS.get(primary);
        if (formatter != null) {
            return formatter;
        }

        return FORMATTERS.get(LanguageSupport.DEFAULT_LANGUAGE);
    }
}
