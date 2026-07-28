package org.rumbledb.runtime.misc;

import com.ibm.icu.text.Collator;
import com.ibm.icu.text.RuleBasedCollator;
import com.ibm.icu.text.StringSearch;
import com.ibm.icu.util.ULocale;
import org.rumbledb.api.Item;
import org.rumbledb.context.CollationCatalogue;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedCollationException;
import org.rumbledb.items.ItemFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CollationSupport {

    private static final ConcurrentHashMap<String, RuleBasedCollator> UCA_COLLATOR_CACHE = new ConcurrentHashMap<>();

    private CollationSupport() {
    }

    public static String resolveCollation(String explicitCollationUri, RuntimeStaticContext staticContext) {
        if (explicitCollationUri != null) {
            return explicitCollationUri;
        }
        return staticContext.getDefaultCollation();
    }

    public static void checkCollationSupported(String collationUri, ExceptionMetadata metadata) {
        if (CollationCatalogue.isDefaultStaticallyKnownCollation(collationUri)) {
            return;
        }
        throw new UnsupportedCollationException("Wrong collation parameter", metadata);
    }

    public static boolean isStringCollationType(Item item) {
        return item != null && (item.isString() || item.isAnyURI() || item.isUntypedAtomic());
    }

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
            .createStringItem(
                CollationCatalogue.normalizeString(item.getStringValue(), collationUri)
            );
    }

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
     * (http://www.w3.org/2005/xpath-functions/collation/codepoint), which is Rumble's default collation.
     * Unlike {@link String#compareTo}, which orders by UTF-16 code unit, this orders supplementary
     * characters (encoded as surrogate pairs) correctly relative to BMP characters above U+E000.
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

    public static boolean startsWith(String value, String prefix, String collationUri, ExceptionMetadata metadata) {
        checkCollationSupported(collationUri, metadata);
        if (Name.DEFAULT_COLLATION_NS.equals(collationUri)) {
            return value.startsWith(prefix);
        }
        if (CollationCatalogue.isUCACollation(collationUri)) {
            RuleBasedCollator collator = getUcaCollator(collationUri, metadata);
            StringSearch stringSearch = new StringSearch(prefix, new StringCharacterIterator(value), collator);
            return stringSearch.first() == 0;
        }
        return CollationCatalogue.normalizeString(value, collationUri)
            .startsWith(CollationCatalogue.normalizeString(prefix, collationUri));
    }

    private static RuleBasedCollator getUcaCollator(String collationUri, ExceptionMetadata metadata) {
        try {
            RuleBasedCollator prototype = UCA_COLLATOR_CACHE.computeIfAbsent(
                collationUri,
                uri -> buildUcaCollator(uri, metadata)
            );
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
        ULocale locale = parameters.languageTag == null
            ? ULocale.ROOT
            : ULocale.forLanguageTag(parameters.languageTag);
        Collator collator = Collator.getInstance(locale);
        if (!(collator instanceof RuleBasedCollator ruleBasedCollator)) {
            throw new UnsupportedCollationException("Wrong collation parameter", metadata);
        }

        ruleBasedCollator.setStrength(parameters.strength);
        ruleBasedCollator.setDecomposition(
            parameters.normalization
                ? Collator.CANONICAL_DECOMPOSITION
                : Collator.NO_DECOMPOSITION
        );
        ruleBasedCollator.setCaseLevel(parameters.caseLevel);
        if (parameters.backwards != null) {
            ruleBasedCollator.setFrenchCollation(parameters.backwards);
        }
        if (parameters.alternateShifted != null) {
            ruleBasedCollator.setAlternateHandlingShifted(parameters.alternateShifted);
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
                    decodeQueryComponent(part.substring(separator + 1))
                );
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
        if ("shifted".equals(normalized)) {
            return true;
        }
        if ("non-ignorable".equals(normalized)) {
            return false;
        }
        if ("blanked".equals(normalized)) {
            return true;
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
    }
}
