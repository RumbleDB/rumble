package org.rumbledb.runtime.functions.util.formatting.language;

import org.rumbledb.config.FormattingLanguageSupport;
import org.rumbledb.exceptions.OurBadException;

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
        if (!FormattingLanguageSupport.isValidFormattingLanguage(formatter.getLanguage())) {
            throw new OurBadException("Unsupported formatting language added: " + formatter.getLanguage());
        }
        FORMATTERS.put(
            LanguageSupport.normalizeLanguage(formatter.getLanguage()),
            formatter
        );
    }


    public static LanguageFormatter forLanguage(String language) {
        return FORMATTERS.getOrDefault(
            language,
            FORMATTERS.get(LanguageSupport.DEFAULT_LANGUAGE)
        );
    }
}
