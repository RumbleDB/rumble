package org.rumbledb.config;

import com.ibm.icu.util.ULocale;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class FormattingLanguageSupport {
    private FormattingLanguageSupport() {
    }

    public static final String DEFAULT_FORMATTING_LANGUAGE = "en";

    private static final Set<String> AVAILABLE_LANGUAGES = buildAvailableLanguages();

    private static final Map<String, Boolean> SUPPORT_CACHE = new ConcurrentHashMap<>();

    private static Set<String> buildAvailableLanguages() {
        Set<String> languages = new HashSet<>();
        for (ULocale availableLocale : ULocale.getAvailableLocales()) {
            languages.add(availableLocale.getLanguage().toLowerCase(Locale.ROOT));
        }
        return languages;
    }

    public static boolean isValidFormattingLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            return false;
        }

        Locale locale = Locale.forLanguageTag(language.trim());
        String resolvedLanguage = locale.getLanguage();
        return !resolvedLanguage.isEmpty();
    }

    public static boolean isSupportedFormattingLanguage(String language) {
        if (language == null) {
            return false;
        }

        Boolean cached = SUPPORT_CACHE.get(language);
        if (cached != null) {
            return cached;
        }

        boolean supported = computeIsSupportedFormattingLanguage(language);
        SUPPORT_CACHE.put(language, supported);
        return supported;
    }

    private static boolean computeIsSupportedFormattingLanguage(String language) {
        if (!isValidFormattingLanguage(language)) {
            return false;
        }

        String requestedLanguage = ULocale.forLanguageTag(language.trim()).getLanguage().toLowerCase(Locale.ROOT);
        return AVAILABLE_LANGUAGES.contains(requestedLanguage);
    }
}
