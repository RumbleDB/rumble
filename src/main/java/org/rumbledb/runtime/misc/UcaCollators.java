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
package org.rumbledb.runtime.misc;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.icu.text.Collator;
import com.ibm.icu.text.RuleBasedCollator;
import com.ibm.icu.util.ULocale;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedCollationException;

/**
 * Builds ICU collators for UCA collation URIs such as {@code http://www.w3.org/2013/collation/UCA?lang=en}.
 */
final class UcaCollators {

    private static final ConcurrentHashMap<String, RuleBasedCollator> CACHE = new ConcurrentHashMap<>();

    private UcaCollators() {}

    /**
     * Returns a collator for the given UCA collation URI. Each call returns a fresh copy, as ICU collators are not
     * thread-safe.
     *
     * @throws UnsupportedCollationException if the URI cannot be supported (err:FOCH0002)
     */
    static RuleBasedCollator get(String collationUri, ExceptionMetadata metadata) {
        try {
            return CACHE.computeIfAbsent(collationUri, uri -> build(uri, metadata))
                    .cloneAsThawed();
        } catch (RuntimeException e) {
            if (e instanceof UnsupportedCollationException) {
                throw e;
            }
            throw new UnsupportedCollationException("Wrong collation parameter", metadata);
        }
    }

    private static RuleBasedCollator build(String collationUri, ExceptionMetadata metadata) {
        UcaParameters parameters = parseParameters(collationUri, metadata);
        ULocale locale = parameters.languageTag == null ? ULocale.ROOT : ULocale.forLanguageTag(parameters.languageTag);
        Collator collator = Collator.getInstance(locale);
        if (!(collator instanceof RuleBasedCollator ruleBasedCollator)) {
            throw new UnsupportedCollationException("Wrong collation parameter", metadata);
        }

        int strength = parameters.strength != null ? parameters.strength : Collator.TERTIARY;
        if (parameters.alternate == Alternate.BLANKED && strength == Collator.QUATERNARY) {
            // ICU has no "blanked" mode. Shifted only differs from blanked by distinguishing variable characters
            // at the quaternary level, so blanked is shifted without that level.
            strength = Collator.TERTIARY;
        }
        ruleBasedCollator.setStrength(strength);
        ruleBasedCollator.setDecomposition(
                Boolean.TRUE.equals(parameters.normalization)
                        ? Collator.CANONICAL_DECOMPOSITION
                        : Collator.NO_DECOMPOSITION);
        ruleBasedCollator.setCaseLevel(Boolean.TRUE.equals(parameters.caseLevel));
        if (parameters.backwards != null) {
            ruleBasedCollator.setFrenchCollation(parameters.backwards);
        }
        if (parameters.alternate != null) {
            ruleBasedCollator.setAlternateHandlingShifted(parameters.alternate != Alternate.NON_IGNORABLE);
        }
        if (parameters.upperCaseFirst != null) {
            if (parameters.upperCaseFirst) {
                ruleBasedCollator.setUpperCaseFirst(true);
            } else {
                ruleBasedCollator.setLowerCaseFirst(true);
            }
        }
        if (parameters.numeric != null) {
            ruleBasedCollator.setNumericCollation(parameters.numeric);
        }
        return ruleBasedCollator;
    }

    private static UcaParameters parseParameters(String collationUri, ExceptionMetadata metadata) {
        UcaParameters parameters = new UcaParameters();
        int queryIndex = collationUri.indexOf('?');
        if (queryIndex < 0 || queryIndex == collationUri.length() - 1) {
            return parameters;
        }
        String query = collationUri.substring(queryIndex + 1);
        Map<String, String> queryParameters = new HashMap<>();
        for (String part : query.split(";")) {
            if (part.isEmpty()) {
                continue;
            }
            int separator = part.indexOf('=');
            if (separator < 0) {
                queryParameters.put(decodeQueryComponent(part), "");
            } else {
                queryParameters.put(
                        decodeQueryComponent(part.substring(0, separator)),
                        decodeQueryComponent(part.substring(separator + 1)));
            }
        }

        // With fallback=yes (the default), unsupported keywords and invalid values are ignored.
        boolean fallback = !Boolean.FALSE.equals(parseYesNo(queryParameters.get("fallback")));
        for (Map.Entry<String, String> parameter : queryParameters.entrySet()) {
            String value = parameter.getValue();
            switch (parameter.getKey()) {
                case "lang" -> parameters.languageTag = value;
                case "strength" -> parameters.strength = validOrFallback(parseStrength(value), fallback, metadata);
                case "alternate" -> parameters.alternate = validOrFallback(parseAlternate(value), fallback, metadata);
                case "backwards" -> parameters.backwards = validOrFallback(parseYesNo(value), fallback, metadata);
                case "normalization" -> parameters.normalization =
                        validOrFallback(parseYesNo(value), fallback, metadata);
                case "caseLevel" -> parameters.caseLevel = validOrFallback(parseYesNo(value), fallback, metadata);
                case "caseFirst" -> parameters.upperCaseFirst =
                        validOrFallback(parseCaseFirst(value), fallback, metadata);
                case "numeric" -> parameters.numeric = validOrFallback(parseYesNo(value), fallback, metadata);
                case "fallback" -> {
                    // Already read before the loop.
                }
                default -> {
                    // Unsupported keyword. This includes version, as no specific UCA version is guaranteed.
                    rejectUnlessFallback(fallback, metadata);
                }
            }
        }
        return parameters;
    }

    /**
     * Returns a parsed UCA parameter value, where null means the value is invalid or unsupported.
     * An invalid value is ignored with fallback=yes and raises err:FOCH0002 with fallback=no.
     */
    private static <T> T validOrFallback(T parsedValue, boolean fallback, ExceptionMetadata metadata) {
        if (parsedValue == null) {
            rejectUnlessFallback(fallback, metadata);
        }
        return parsedValue;
    }

    private static void rejectUnlessFallback(boolean fallback, ExceptionMetadata metadata) {
        if (!fallback) {
            throw new UnsupportedCollationException("Wrong collation parameter", metadata);
        }
    }

    private static String decodeQueryComponent(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static Boolean parseYesNo(String value) {
        if ("yes".equalsIgnoreCase(value)) {
            return true;
        }
        if ("no".equalsIgnoreCase(value)) {
            return false;
        }
        return null;
    }

    private static Boolean parseCaseFirst(String value) {
        return switch (value.toLowerCase(Locale.ROOT)) {
            case "upper" -> true;
            case "lower" -> false;
            default -> null;
        };
    }

    private static Alternate parseAlternate(String value) {
        return switch (value.toLowerCase(Locale.ROOT)) {
            case "non-ignorable" -> Alternate.NON_IGNORABLE;
            case "shifted" -> Alternate.SHIFTED;
            case "blanked" -> Alternate.BLANKED;
            default -> null;
        };
    }

    private static Integer parseStrength(String value) {
        return switch (value.toLowerCase(Locale.ROOT)) {
            case "1", "primary" -> Collator.PRIMARY;
            case "2", "secondary" -> Collator.SECONDARY;
            case "3", "tertiary" -> Collator.TERTIARY;
            case "4", "quaternary" -> Collator.QUATERNARY;
            case "5", "identical" -> Collator.IDENTICAL;
            default -> null;
        };
    }

    private enum Alternate {
        NON_IGNORABLE,
        SHIFTED,
        BLANKED
    }

    /**
     * Parameters given in a UCA collation URI. A null field means the parameter was absent or ignored.
     */
    private static final class UcaParameters {
        private String languageTag;
        private Integer strength;
        private Alternate alternate;
        private Boolean backwards;
        private Boolean normalization;
        private Boolean caseLevel;
        private Boolean upperCaseFirst;
        private Boolean numeric;
    }
}
