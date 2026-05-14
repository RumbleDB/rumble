package org.rumbledb.runtime.functions.util.formatting.language;

import org.rumbledb.config.FormattingLanguageSupport;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.functions.util.formatting.language.formatter.EnglishFormatter;
import org.rumbledb.runtime.functions.util.formatting.language.formatter.LanguageFormatter;

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
            throw new OurBadException(
                    "Registered formatter is not supported by the RumbleRuntimeConfiguration: "
                        + formatter.getLanguage()
            );
        }
        FORMATTERS.put(
            LanguageSupport.normalizeLanguage(formatter.getLanguage()),
            formatter
        );
    }


    public static LanguageFormatter forLanguage(String language) {
        String normalized = LanguageSupport.normalizeLanguage(language);

        LanguageFormatter formatter = FORMATTERS.get(normalized);
        if (formatter != null) {
            return formatter;
        }

        String primary = LanguageSupport.getPrimaryLanguageSubtag(normalized);
        formatter = FORMATTERS.get(primary);
        if (formatter != null) {
            return formatter;
        }

        return FORMATTERS.get(LanguageSupport.DEFAULT_LANGUAGE);
    }
}
