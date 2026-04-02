package org.rumbledb.runtime.functions.base.formatting.language;

import java.util.Locale;

public final class LanguageSupport {

    public static final String DEFAULT_LANGUAGE = "en";

    private LanguageSupport() {
    }

    public static String normalizeLanguage(String language) {
        if (language == null) {
            return DEFAULT_LANGUAGE;
        }

        String normalized = language.trim();
        if (normalized.isEmpty()) {
            return DEFAULT_LANGUAGE;
        }

        return normalized.replace('_', '-');
    }

    public static String normalizeLanguageForLookup(String language) {
        return normalizeLanguage(language).toLowerCase(Locale.ROOT);
    }

    public static String getPrimaryLanguageSubtag(String language) {
        String normalized = normalizeLanguageForLookup(language);
        int dash = normalized.indexOf('-');
        if (dash > 0) {
            return normalized.substring(0, dash);
        }
        return normalized;
    }

    public static Locale resolveLocale(String language) {
        if (language == null || language.trim().isEmpty()) {
            return Locale.getDefault();
        }
        return Locale.forLanguageTag(language.trim().replace('_', '-'));
    }
}
