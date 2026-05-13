package org.rumbledb.runtime.functions.util.formatting.language;

import org.rumbledb.config.FormattingLanguageSupport;

import java.util.Locale;

public final class LanguageSupport {

    // TODO instantiate once from dynamic context? adding more languages should throw an error even? Default values
    // TODO should eventually be sourced from dynamic context, they should not be configurable by the
    // TODO each function should get the default language from the config if its empty
    public static final String DEFAULT_LANGUAGE = FormattingLanguageSupport.DEFAULT_FORMATTING_LANGUAGE;

    private LanguageSupport() {
    }

    public static String normalizeLanguage(String language) {
        String normalized = language.trim();
        return normalized.toLowerCase(Locale.ROOT);
    }


    public static Locale resolveLocale(String language) {
        if (language == null || language.trim().isEmpty()) {
            return Locale.getDefault();
        }
        return Locale.forLanguageTag(language.trim().replace('_', '-'));
    }
}
