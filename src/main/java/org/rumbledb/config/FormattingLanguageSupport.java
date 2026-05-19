package org.rumbledb.config;

import java.util.List;

public final class FormattingLanguageSupport {
    private FormattingLanguageSupport() {
    }

    // Language Codes
    public static final String ENGLISH = "en";

    public static final String DEFAULT_FORMATTING_LANGUAGE = ENGLISH;

    public static final List<String> supportedLanguages = List.of(ENGLISH);

    public static boolean isValidFormattingLanguage(String language) {
        return language != null && supportedLanguages.contains(language);
    }
}
