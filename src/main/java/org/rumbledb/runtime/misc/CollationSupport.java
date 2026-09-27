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

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.icu.text.Collator;
import com.ibm.icu.text.RuleBasedCollator;
import com.ibm.icu.text.StringSearch;
import com.ibm.icu.util.ULocale;

import org.rumbledb.api.Item;
import org.rumbledb.context.CollationCatalogue;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CollationDoesNotSupportCollationUnitsException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedCollationException;
import org.rumbledb.items.ItemFactory;

public final class CollationSupport {

    private static final ConcurrentHashMap<String, RuleBasedCollator> UCA_COLLATOR_CACHE = new ConcurrentHashMap<>();

    private CollationSupport() {}

    /**
     * Resolves an optional collation URI against the static base URI if relative.
     * If null, falls back to the default collation of the static context (or Unicode codepoint collation).
     *
     * @param explicitCollationUri the collation URI supplied by the query, or null
     * @param staticContext the runtime static context providing the base URI and default collation
     * @return the resolved collation URI string
     */
    public static String resolveCollation(String explicitCollationUri, RuntimeStaticContext staticContext) {
        if (explicitCollationUri == null) {
            return staticContext != null ? staticContext.getDefaultCollation() : CollationCatalogue.CODEPOINT_COLLATION;
        }
        try {
            URI uri = URI.create(explicitCollationUri);
            if (!uri.isAbsolute() && staticContext != null && staticContext.getStaticURI() != null) {
                return staticContext.getStaticURI().resolve(uri).toString();
            }
        } catch (IllegalArgumentException ignored) {
            // let checkCollationSupported reject invalid URI
        }
        return explicitCollationUri;
    }

    /**
     * Resolves an optional collation URI and validates that it is supported by the engine.
     *
     * @param explicitCollationUri the collation URI supplied by the query, or null
     * @param staticContext the runtime static context providing the base URI and default collation
     * @param metadata source location metadata for error reporting
     * @return the resolved, validated collation URI string
     * @throws UnsupportedCollationException if the collation is not supported (err:FOCH0002)
     */
    public static String resolveAndCheckCollation(
            String explicitCollationUri, RuntimeStaticContext staticContext, ExceptionMetadata metadata) {
        String collation = resolveCollation(explicitCollationUri, staticContext);
        checkCollationSupported(collation, metadata);
        return collation;
    }

    /**
     * Verifies that the specified collation URI is recognized and supported.
     *
     * @param collationUri the collation URI to validate
     * @param metadata source location metadata for error reporting
     * @throws UnsupportedCollationException if the collation is not supported (err:FOCH0002)
     */
    public static void checkCollationSupported(String collationUri, ExceptionMetadata metadata) {
        if (CollationCatalogue.isDefaultStaticallyKnownCollation(collationUri)) {
            return;
        }
        throw new UnsupportedCollationException("Wrong collation parameter", metadata);
    }

    /**
     * Checks if the item has a type that participates in string collation comparisons
     * (xs:string, xs:anyURI, or xs:untypedAtomic).
     *
     * @param item the item to test
     * @return true if the item is string-like for collation purposes
     */
    public static boolean isStringCollationType(Item item) {
        return item != null && (item.isString() || item.isAnyURI() || item.isUntypedAtomic());
    }

    /**
     * Normalizes an item into a form suitable for collation-based equality or sorting.
     * For UCA collations, returns a sort key as a hexBinary item; for case-insensitive collations,
     * returns a normalized lowercase string item.
     *
     * @param item the item to normalize
     * @param collationUri the collation URI to use
     * @param metadata source location metadata for error reporting
     * @return the normalized item, or the original item if not subject to collation normalization
     */
    public static Item normalizeItemForCollation(Item item, String collationUri, ExceptionMetadata metadata) {
        if (item == null) {
            return null;
        }
        checkCollationSupported(collationUri, metadata);
        if (!isStringCollationType(item) || Name.DEFAULT_COLLATION_NS.equals(collationUri)) {
            return item;
        }
        if (CollationCatalogue.isUCACollation(collationUri)) {
            byte[] sortKeyBytes = getUcaCollator(collationUri, metadata)
                    .getCollationKey(item.getStringValue())
                    .toByteArray();
            return ItemFactory.getInstance().createHexBinaryItem(HexFormat.of().formatHex(sortKeyBytes));
        }
        return ItemFactory.getInstance()
                .createStringItem(CollationCatalogue.normalizeString(item.getStringValue(), collationUri));
    }

    /**
     * Compares two strings according to the rules of the given collation.
     *
     * @param left the left string operand
     * @param right the right string operand
     * @param collationUri the collation URI defining the comparison rules
     * @param metadata source location metadata for error reporting
     * @return negative if left &lt; right, zero if left == right, positive if left &gt; right
     */
    public static int compareStrings(String left, String right, String collationUri, ExceptionMetadata metadata) {
        checkCollationSupported(collationUri, metadata);
        if (Name.DEFAULT_COLLATION_NS.equals(collationUri)) {
            return compareByCodePoint(left, right);
        }
        if (CollationCatalogue.isUCACollation(collationUri)) {
            return getUcaCollator(collationUri, metadata).compare(left, right);
        }
        return CollationCatalogue.normalizeString(left, collationUri)
                .compareTo(CollationCatalogue.normalizeString(right, collationUri));
    }

    /**
     * Compares two strings by Unicode code point value, as required by the Unicode Codepoint Collation
     */
    public static int compareByCodePoint(String left, String right) {
        int leftLength = left.length();
        int rightLength = right.length();
        int i = 0;
        int j = 0;
        while (i < leftLength && j < rightLength) {
            int leftCodePoint = left.codePointAt(i);
            int rightCodePoint = right.codePointAt(j);
            if (leftCodePoint != rightCodePoint) {
                return leftCodePoint - rightCodePoint;
            }
            i += Character.charCount(leftCodePoint);
            j += Character.charCount(rightCodePoint);
        }
        return (leftLength - i) - (rightLength - j);
    }

    /**
     * Locates the first occurrence of {@code target} in {@code source} under the specified collation.
     *
     * <p>
     * Handles codepoint matching via {@link String#indexOf}, UCA collation matching via ICU4J
     * {@link com.ibm.icu.text.StringSearch} (including ignorable/blanked collation units),
     * and case-insensitive collations via normalized string matching.
     * </p>
     *
     * @param source the string to search within
     * @param target the substring to search for
     * @param collationUri the collation URI defining the matching rules
     * @param metadata source location metadata for error reporting
     * @return an {@code int[]} with two elements: {@code [startIndex, matchLength]}, or {@code null} if no match is
     *         found
     * @throws CollationDoesNotSupportCollationUnitsException if the collation does not support collation units
     *         (err:FOCH0004)
     */
    public static int[] indexOf(String source, String target, String collationUri, ExceptionMetadata metadata) {
        checkCollationSupported(collationUri, metadata);
        if (target.isEmpty()) {
            return new int[] {0, 0};
        }
        if (Name.DEFAULT_COLLATION_NS.equals(collationUri)) {
            if (source.isEmpty()) {
                return null;
            }
            int idx = source.indexOf(target);
            return idx == -1 ? null : new int[] {idx, target.length()};
        }
        if (CollationCatalogue.isUCACollation(collationUri)) {
            RuleBasedCollator collator = getUcaCollator(collationUri, metadata);
            if (collator.compare(target, "") == 0) {
                return new int[] {0, 0};
            }
            if (source.isEmpty()) {
                return null;
            }
            try {
                StringSearch stringSearch = new StringSearch(target, new StringCharacterIterator(source), collator);
                int idx = stringSearch.first();
                if (idx == StringSearch.DONE) {
                    return null;
                }
                return new int[] {idx, stringSearch.getMatchLength()};
            } catch (UnsupportedOperationException e) {
                throw new CollationDoesNotSupportCollationUnitsException(
                        "Collation does not support collation units: " + e.getMessage(), metadata);
            }
        }
        if (source.isEmpty()) {
            return null;
        }
        String normSource = CollationCatalogue.normalizeString(source, collationUri);
        String normTarget = CollationCatalogue.normalizeString(target, collationUri);
        int idx = normSource.indexOf(normTarget);
        return idx == -1 ? null : new int[] {idx, target.length()};
    }

    /**
     * Evaluates {@code fn:contains} semantics: returns true if {@code source} contains {@code target}
     * under the specified collation. An empty target always matches.
     *
     * @param source the string to search within
     * @param target the substring to search for
     * @param collationUri the collation URI defining matching rules
     * @param metadata source location metadata for error reporting
     * @return true if the target is contained in the source under the collation
     */
    public static boolean contains(String source, String target, String collationUri, ExceptionMetadata metadata) {
        checkCollationSupported(collationUri, metadata);
        return indexOf(source, target, collationUri, metadata) != null;
    }

    /**
     * Evaluates {@code fn:substring-before} semantics: returns the substring of {@code source} preceding
     * the first occurrence of {@code target} under the specified collation.
     *
     * @param source the string to extract from
     * @param target the delimiter substring
     * @param collationUri the collation URI defining matching rules
     * @param metadata source location metadata for error reporting
     * @return the substring before the target match, or empty string if no match or empty inputs
     */
    public static String substringBefore(
            String source, String target, String collationUri, ExceptionMetadata metadata) {
        checkCollationSupported(collationUri, metadata);
        if (source.isEmpty() || target.isEmpty()) {
            return "";
        }
        int[] match = indexOf(source, target, collationUri, metadata);
        if (match == null || match[1] == 0) {
            return "";
        }
        return source.substring(0, match[0]);
    }

    /**
     * Evaluates {@code fn:substring-after} semantics: returns the substring of {@code source} following
     * the first occurrence of {@code target} under the specified collation.
     *
     * @param source the string to extract from
     * @param target the delimiter substring
     * @param collationUri the collation URI defining matching rules
     * @param metadata source location metadata for error reporting
     * @return the substring after the target match, or empty string if not found / empty source
     */
    public static String substringAfter(String source, String target, String collationUri, ExceptionMetadata metadata) {
        checkCollationSupported(collationUri, metadata);
        if (source.isEmpty()) {
            return "";
        }
        if (target.isEmpty()) {
            return source;
        }
        int[] match = indexOf(source, target, collationUri, metadata);
        if (match == null) {
            return "";
        }
        if (match[1] == 0) {
            return source;
        }
        return source.substring(match[0] + match[1]);
    }

    /**
     * Evaluates {@code fn:starts-with} semantics: returns true if {@code value} starts with {@code prefix}
     * under the specified collation. An empty prefix always matches.
     *
     * @param value the string to check
     * @param prefix the prefix to look for
     * @param collationUri the collation URI defining matching rules
     * @param metadata source location metadata for error reporting
     * @return true if the string starts with the prefix under the collation
     */
    public static boolean startsWith(String value, String prefix, String collationUri, ExceptionMetadata metadata) {
        checkCollationSupported(collationUri, metadata);
        if (prefix.isEmpty()) {
            return true;
        }
        if (Name.DEFAULT_COLLATION_NS.equals(collationUri)) {
            return value.startsWith(prefix);
        }
        int[] match = indexOf(value, prefix, collationUri, metadata);
        return match != null && match[0] == 0;
    }

    /**
     * Evaluates {@code fn:ends-with} semantics: returns true if {@code value} ends with {@code suffix}
     * under the specified collation. An empty suffix always matches.
     *
     * @param value the string to check
     * @param suffix the suffix to look for
     * @param collationUri the collation URI defining matching rules
     * @param metadata source location metadata for error reporting
     * @return true if the string ends with the suffix under the collation
     */
    public static boolean endsWith(String value, String suffix, String collationUri, ExceptionMetadata metadata) {
        checkCollationSupported(collationUri, metadata);
        if (suffix.isEmpty()) {
            return true;
        }
        if (Name.DEFAULT_COLLATION_NS.equals(collationUri)) {
            return value.endsWith(suffix);
        }
        if (CollationCatalogue.isUCACollation(collationUri)) {
            RuleBasedCollator collator = getUcaCollator(collationUri, metadata);
            if (collator.compare(suffix, "") == 0) {
                return true;
            }
            if (value.isEmpty()) {
                return false;
            }
            try {
                StringSearch stringSearch = new StringSearch(suffix, new StringCharacterIterator(value), collator);
                int lastIndex = stringSearch.last();
                return lastIndex != StringSearch.DONE && lastIndex + stringSearch.getMatchLength() == value.length();
            } catch (UnsupportedOperationException e) {
                throw new CollationDoesNotSupportCollationUnitsException(
                        "Collation does not support collation units: " + e.getMessage(), metadata);
            }
        }
        if (value.isEmpty()) {
            return false;
        }
        return CollationCatalogue.normalizeString(value, collationUri)
                .endsWith(CollationCatalogue.normalizeString(suffix, collationUri));
    }

    private static RuleBasedCollator getUcaCollator(String collationUri, ExceptionMetadata metadata) {
        try {
            RuleBasedCollator prototype =
                    UCA_COLLATOR_CACHE.computeIfAbsent(collationUri, uri -> buildUcaCollator(uri, metadata));
            return prototype.cloneAsThawed();
        } catch (RuntimeException e) {
            if (e instanceof UnsupportedCollationException) {
                throw e;
            }
            throw new UnsupportedCollationException("Wrong collation parameter", metadata);
        }
    }

    private static RuleBasedCollator buildUcaCollator(String collationUri, ExceptionMetadata metadata) {
        UcaParameters parameters = parseUcaParameters(collationUri, metadata);
        ULocale locale = parameters.languageTag == null ? ULocale.ROOT : ULocale.forLanguageTag(parameters.languageTag);
        Collator collator = Collator.getInstance(locale);
        if (!(collator instanceof RuleBasedCollator ruleBasedCollator)) {
            throw new UnsupportedCollationException("Wrong collation parameter", metadata);
        }

        ruleBasedCollator.setStrength(parameters.strength);
        ruleBasedCollator.setDecomposition(
                parameters.normalization ? Collator.CANONICAL_DECOMPOSITION : Collator.NO_DECOMPOSITION);
        ruleBasedCollator.setCaseLevel(parameters.caseLevel);
        if (parameters.backwards != null) {
            ruleBasedCollator.setFrenchCollation(parameters.backwards);
        }
        if (parameters.alternateShifted != null) {
            ruleBasedCollator.setAlternateHandlingShifted(parameters.alternateShifted);
        }
        if (parameters.numeric != null) {
            ruleBasedCollator.setNumericCollation(parameters.numeric);
        }
        return ruleBasedCollator;
    }

    private static UcaParameters parseUcaParameters(String collationUri, ExceptionMetadata metadata) {
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

        for (Map.Entry<String, String> parameter : queryParameters.entrySet()) {
            String key = parameter.getKey();
            String value = parameter.getValue();
            switch (key) {
                case "lang":
                    parameters.languageTag = value;
                    break;
                case "strength":
                    parameters.strength = parseStrength(value, metadata);
                    break;
                case "normalization":
                    parameters.normalization = parseYesNo(value, key, metadata);
                    break;
                case "backwards":
                    parameters.backwards = parseYesNo(value, key, metadata);
                    break;
                case "caseLevel":
                    parameters.caseLevel = parseYesNo(value, key, metadata);
                    break;
                case "alternate":
                    parameters.alternateShifted = parseAlternate(value, metadata);
                    break;
                case "numeric":
                    parameters.numeric = parseYesNo(value, key, metadata);
                    break;
                case "fallback":
                case "version":
                    if ("version".equals(key) && "no".equals(queryParameters.get("fallback"))) {
                        throw new UnsupportedCollationException("Wrong collation parameter", metadata);
                    }
                    break;
                default:
                    if ("no".equals(queryParameters.get("fallback"))) {
                        throw new UnsupportedCollationException("Wrong collation parameter", metadata);
                    }
                    break;
            }
        }
        return parameters;
    }

    private static String decodeQueryComponent(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static boolean parseYesNo(String value, String key, ExceptionMetadata metadata) {
        if ("yes".equalsIgnoreCase(value)) {
            return true;
        }
        if ("no".equalsIgnoreCase(value)) {
            return false;
        }
        throw new UnsupportedCollationException("Wrong collation parameter", metadata);
    }

    private static Boolean parseAlternate(String value, ExceptionMetadata metadata) {
        String normalized = value.toLowerCase(Locale.ROOT);
        if ("shifted".equals(normalized) || "blanked".equals(normalized)) {
            return true;
        }
        if ("non-ignorable".equals(normalized)) {
            return false;
        }
        throw new UnsupportedCollationException("Wrong collation parameter", metadata);
    }

    private static int parseStrength(String value, ExceptionMetadata metadata) {
        String normalized = value.toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "1", "primary" -> Collator.PRIMARY;
            case "2", "secondary" -> Collator.SECONDARY;
            case "3", "tertiary" -> Collator.TERTIARY;
            case "4", "quaternary" -> Collator.QUATERNARY;
            case "5", "identical" -> Collator.IDENTICAL;
            default -> throw new UnsupportedCollationException("Wrong collation parameter", metadata);
        };
    }

    private static final class UcaParameters {
        private String languageTag;
        private int strength = Collator.TERTIARY;
        private boolean normalization;
        private Boolean backwards;
        private boolean caseLevel;
        private Boolean alternateShifted;
        private Boolean numeric;
    }
}
