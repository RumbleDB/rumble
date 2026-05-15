package org.rumbledb.runtime.functions.util.formatting.language;

import org.rumbledb.config.FormattingLanguageSupport;

import java.util.Locale;

public final class LanguageSupport {

    public static final String DEFAULT_LANGUAGE = FormattingLanguageSupport.DEFAULT_FORMATTING_LANGUAGE;

    private LanguageSupport() {
    }

    public static String normalizeLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            return DEFAULT_LANGUAGE;
        }

        return language.trim().replace('_', '-').toLowerCase(Locale.ROOT);
    }


    public static Locale resolveLocale(String language) {
        return Locale.forLanguageTag(normalizeLanguage(language));
    }

    public static String getPrimaryLanguageSubtag(String language) {
        String normalized = normalizeLanguage(language);
        int dash = normalized.indexOf('-');
        if (dash > 0) {
            return normalized.substring(0, dash);
        }
        return normalized;
    }
}
