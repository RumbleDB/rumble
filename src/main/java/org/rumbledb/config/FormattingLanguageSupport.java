/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.config;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.icu.util.ULocale;

public final class FormattingLanguageSupport {
    private FormattingLanguageSupport() {}

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

        String requestedLanguage =
                ULocale.forLanguageTag(language.trim()).getLanguage().toLowerCase(Locale.ROOT);
        return AVAILABLE_LANGUAGES.contains(requestedLanguage);
    }
}
