package org.rumbledb.config;

import java.util.ArrayList;
import java.util.List;

public final class FormattingLanguageSupport {
    private FormattingLanguageSupport() {
    }

    // Language Codes
    public static final String ENGLISH = "en";

    public static final String DEFAULT_FORMATTING_LANGUAGE = ENGLISH;

    public static final List<String> supportedLanguages = new ArrayList<>();

    public static boolean isValidFormattingLanguage(String language) {
        return language != null && supportedLanguages.contains(language);
    }

    static {
        supportedLanguages.add(ENGLISH);
    }
}
