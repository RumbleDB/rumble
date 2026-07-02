package org.rumbledb.config;

import com.ibm.icu.util.ULocale;

import java.util.Locale;

public final class FormattingLanguageSupport {
    private FormattingLanguageSupport() {
    }

    public static final String DEFAULT_FORMATTING_LANGUAGE = "en";

    public static boolean isValidFormattingLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            return false;
        }

        Locale locale = Locale.forLanguageTag(language.trim());
        String resolvedLanguage = locale.getLanguage();
        return !resolvedLanguage.isEmpty();
    }

    public static boolean isSupportedFormattingLanguage(String language) {
        if (!isValidFormattingLanguage(language)) {
            return false;
        }

        String requestedLanguage = ULocale.forLanguageTag(language.trim()).getLanguage();
        for (ULocale availableLocale : ULocale.getAvailableLocales()) {
            if (requestedLanguage.equalsIgnoreCase(availableLocale.getLanguage())) {
                return true;
            }
        }

        return false;
    }
}
