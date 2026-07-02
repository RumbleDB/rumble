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

    /** Returns the normalized language if ICU supports it, otherwise the default. */
    public static String effectiveLanguageOf(String normalizedLanguage) {
        return FormattingLanguageSupport.isSupportedFormattingLanguage(normalizedLanguage)
            ? normalizedLanguage
            : DEFAULT_LANGUAGE;
    }


    public static Locale resolveLocale(String language) {
        return Locale.forLanguageTag(normalizeLanguage(language));
    }
}
