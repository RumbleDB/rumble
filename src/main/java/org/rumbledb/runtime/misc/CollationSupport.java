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
import java.text.StringCharacterIterator;
import java.util.HexFormat;
import java.util.Locale;

import com.ibm.icu.text.RuleBasedCollator;
import com.ibm.icu.text.StringSearch;

import org.rumbledb.api.Item;
import org.rumbledb.context.CollationCatalogue;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CollationDoesNotSupportCollationUnitsException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedCollationException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/**
 * Resolves collation URIs, and compares and searches strings under the codepoint, case-insensitive and UCA
 * collations.
 */
public final class CollationSupport {

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
     * Evaluates an optional collation argument of a built-in function, then resolves and validates it.
     *
     * @param collationPlan the plan of the collation argument, or null if the argument is absent
     * @param context the dynamic context to evaluate the argument in
     * @param staticContext the runtime static context providing the base URI and default collation
     * @param metadata source location metadata for error reporting
     * @return the resolved, validated collation URI string
     * @throws UnsupportedCollationException if the collation is not supported (err:FOCH0002)
     */
    public static String resolveCollationArgument(
            ItemRuntimePlan collationPlan,
            DynamicContext context,
            RuntimeStaticContext staticContext,
            ExceptionMetadata metadata) {
        String explicitCollationUri = collationPlan == null
                ? null
                : collationPlan.materializeFirstOrNull(context).getStringValue();
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
            byte[] sortKeyBytes = UcaCollators.get(collationUri, metadata)
                    .getCollationKey(item.getStringValue())
                    .toByteArray();
            return ItemFactory.getInstance().createHexBinaryItem(HexFormat.of().formatHex(sortKeyBytes));
        }
        return ItemFactory.getInstance().createStringItem(foldCase(item.getStringValue(), collationUri));
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
            return UcaCollators.get(collationUri, metadata).compare(left, right);
        }
        return foldCase(left, collationUri).compareTo(foldCase(right, collationUri));
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
        Match match = indexOf(source, target, collationUri, metadata);
        return match == null ? "" : source.substring(0, match.start());
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
        Match match = indexOf(source, target, collationUri, metadata);
        return match == null ? "" : source.substring(match.end());
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
        if (CollationCatalogue.isUCACollation(collationUri)) {
            RuleBasedCollator collator = UcaCollators.get(collationUri, metadata);
            if (isIgnorable(prefix, collator)) {
                return true;
            }
            if (value.isEmpty()) {
                return false;
            }
            StringSearch stringSearch = newStringSearch(prefix, value, collator, metadata);
            int firstIndex = stringSearch.first();
            // Leading ignorable characters have no collation units, so the match may start after them.
            return firstIndex != StringSearch.DONE && isIgnorable(value.substring(0, firstIndex), collator);
        }
        return foldCase(value, collationUri).startsWith(foldCase(prefix, collationUri));
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
            RuleBasedCollator collator = UcaCollators.get(collationUri, metadata);
            if (isIgnorable(suffix, collator)) {
                return true;
            }
            if (value.isEmpty()) {
                return false;
            }
            StringSearch stringSearch = newStringSearch(suffix, value, collator, metadata);
            int lastIndex = stringSearch.last();
            // Trailing ignorable characters have no collation units, so the match may end before them.
            return lastIndex != StringSearch.DONE
                    && isIgnorable(value.substring(lastIndex + stringSearch.getMatchLength()), collator);
        }
        return foldCase(value, collationUri).endsWith(foldCase(suffix, collationUri));
    }

    /**
     * A match of a search string, as the start index and length of the matched region in the searched string.
     */
    private record Match(int start, int length) {
        int end() {
            return this.start + this.length;
        }
    }

    /**
     * Locates the first occurrence of {@code target} in {@code source} under the specified collation.
     *
     * @return the first match, or null if there is none
     * @throws CollationDoesNotSupportCollationUnitsException if the collation does not support collation units
     *         (err:FOCH0004)
     */
    private static Match indexOf(String source, String target, String collationUri, ExceptionMetadata metadata) {
        checkCollationSupported(collationUri, metadata);
        if (target.isEmpty()) {
            return new Match(0, 0);
        }
        if (Name.DEFAULT_COLLATION_NS.equals(collationUri)) {
            int idx = source.indexOf(target);
            return idx == -1 ? null : new Match(idx, target.length());
        }
        if (CollationCatalogue.isUCACollation(collationUri)) {
            RuleBasedCollator collator = UcaCollators.get(collationUri, metadata);
            if (isIgnorable(target, collator)) {
                return new Match(0, 0);
            }
            if (source.isEmpty()) {
                return null;
            }
            StringSearch stringSearch = newStringSearch(target, source, collator, metadata);
            int idx = stringSearch.first();
            return idx == StringSearch.DONE ? null : new Match(idx, stringSearch.getMatchLength());
        }
        if (CollationCatalogue.isHTMLAsciiCaseInsensitiveCollation(collationUri)) {
            // ASCII lowercasing preserves length, so indices in the lowercased string match the original.
            int idx = toHtmlAsciiLowerCase(source).indexOf(toHtmlAsciiLowerCase(target));
            return idx == -1 ? null : new Match(idx, target.length());
        }
        // Full Unicode lowercasing may change the string length, so match on the original string instead.
        for (int idx = 0; idx + target.length() <= source.length(); idx++) {
            if (source.regionMatches(true, idx, target, 0, target.length())) {
                return new Match(idx, target.length());
            }
        }
        return null;
    }

    /**
     * Folds the case of a string under a case-insensitive collation. Callers handle the codepoint and UCA
     * collations first, so only the HTML ASCII and the QT3 case-blind collations reach this method.
     */
    private static String foldCase(String value, String collationUri) {
        if (CollationCatalogue.isHTMLAsciiCaseInsensitiveCollation(collationUri)) {
            return toHtmlAsciiLowerCase(value);
        }
        return value.toLowerCase(Locale.ROOT);
    }

    /**
     * Lowercases only the ASCII letters A-Z, as the HTML ASCII case-insensitive collation requires.
     */
    private static String toHtmlAsciiLowerCase(String value) {
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'A' && chars[i] <= 'Z') {
                chars[i] = (char) (chars[i] - 'A' + 'a');
            }
        }
        return new String(chars);
    }

    private static boolean isIgnorable(String value, RuleBasedCollator collator) {
        return collator.compare(value, "") == 0;
    }

    private static StringSearch newStringSearch(
            String pattern, String text, RuleBasedCollator collator, ExceptionMetadata metadata) {
        try {
            return new StringSearch(pattern, new StringCharacterIterator(text), collator);
        } catch (UnsupportedOperationException e) {
            // ICU rejects collations without collation units, e.g. numeric collation.
            throw new CollationDoesNotSupportCollationUnitsException(
                    "Collation does not support collation units: " + e.getMessage(), metadata);
        }
    }
}
