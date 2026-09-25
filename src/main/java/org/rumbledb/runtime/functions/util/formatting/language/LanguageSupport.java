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
package org.rumbledb.runtime.functions.util.formatting.language;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.icu.util.ULocale;

import org.rumbledb.config.FormattingLanguageSupport;

public final class LanguageSupport {

    public static final String DEFAULT_LANGUAGE = FormattingLanguageSupport.DEFAULT_FORMATTING_LANGUAGE;

    private static final Map<String, ULocale> ULOCALE_CACHE = new ConcurrentHashMap<>();

    private LanguageSupport() {}

    /** Normalizes, applies the ICU-support fallback, and resolves to a ULocale, cached by language string. */
    public static ULocale resolveEffectiveULocale(String language) {
        return ULOCALE_CACHE.computeIfAbsent(
                language == null ? DEFAULT_LANGUAGE : language,
                l -> ULocale.forLanguageTag(effectiveLanguageOf(normalizeLanguage(l))));
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
