package org.rumbledb.runtime.functions.strings;

import com.ibm.icu.text.Collator;
import com.ibm.icu.text.RuleBasedCollator;
import com.ibm.icu.util.ULocale;

import org.rumbledb.context.CollationCatalogue;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedCollationException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Resolves a collation URI to a {@link Comparator} usable for string comparison, backed by ICU4J
 * for the UCA-parametrized collation family.
 */
public final class CollationResolver {

    private CollationResolver() {
    }

    public static Comparator<String> resolve(String collationUri, ExceptionMetadata metadata) {
        if (collationUri == null || CollationCatalogue.CODEPOINT_COLLATION.equals(collationUri)) {
            return CollationResolver::compareCodepoints;
        }
        if (CollationCatalogue.HTML_ASCII_CASE_INSENSITIVE_COLLATION.equals(collationUri)) {
            return Comparator.comparing(CollationResolver::asciiLowerCase);
        }
        if (CollationCatalogue.FOTS_CASEBLIND_COLLATION.equals(collationUri)) {
            return String.CASE_INSENSITIVE_ORDER;
        }
        if (CollationCatalogue.isUCACollation(collationUri)) {
            return buildUCAComparator(collationUri, metadata);
        }
        throw new UnsupportedCollationException("Unsupported collation: " + collationUri, metadata);
    }

    public static boolean equals(String a, String b, String collationUri, ExceptionMetadata metadata) {
        return resolve(collationUri, metadata).compare(a, b) == 0;
    }

    /**
     * Returns a byte encoding such that unsigned lexicographic order of the bytes matches the
     * collation order (used by fn:collation-key). For the codepoint collation, this is simply the
     * UTF-8 encoding, per its definition in terms of Unicode code points.
     */
    public static byte[] collationKeyBytes(String value, String collationUri, ExceptionMetadata metadata) {
        if (collationUri == null || CollationCatalogue.CODEPOINT_COLLATION.equals(collationUri)) {
            return value.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
        if (CollationCatalogue.HTML_ASCII_CASE_INSENSITIVE_COLLATION.equals(collationUri)) {
            return asciiLowerCase(value).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
        if (CollationCatalogue.FOTS_CASEBLIND_COLLATION.equals(collationUri)) {
            return value.toLowerCase(java.util.Locale.ROOT).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
        if (CollationCatalogue.isUCACollation(collationUri)) {
            return buildICUCollator(collationUri, metadata).getCollationKey(value).toByteArray();
        }
        throw new UnsupportedCollationException("Unsupported collation: " + collationUri, metadata);
    }

    private static int compareCodepoints(String a, String b) {
        return a.compareTo(b);
    }

    private static String asciiLowerCase(String value) {
        StringBuilder result = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c = (char) (c + ('a' - 'A'));
            }
            result.append(c);
        }
        return result.toString();
    }

    private static Comparator<String> buildUCAComparator(String collationUri, ExceptionMetadata metadata) {
        Collator collator = buildICUCollator(collationUri, metadata);
        return collator::compare;
    }

    private static Collator buildICUCollator(String collationUri, ExceptionMetadata metadata) {
        Map<String, String> params = parseQueryParams(collationUri, metadata);
        String lang = params.get("lang");
        ULocale locale = lang == null ? ULocale.ROOT : new ULocale(lang);
        Collator collator = Collator.getInstance(locale);

        String strength = params.get("strength");
        if (strength != null) {
            collator.setStrength(mapStrength(strength, metadata));
        }

        String caseFirst = params.get("caseFirst");
        if (caseFirst != null && collator instanceof RuleBasedCollator ruleBasedCollator) {
            if ("upper".equals(caseFirst)) {
                ruleBasedCollator.setUpperCaseFirst(true);
            } else if ("lower".equals(caseFirst)) {
                ruleBasedCollator.setLowerCaseFirst(true);
            }
        }
        return collator;
    }

    private static int mapStrength(String strength, ExceptionMetadata metadata) {
        return switch (strength) {
            case "primary" -> Collator.PRIMARY;
            case "secondary" -> Collator.SECONDARY;
            case "tertiary" -> Collator.TERTIARY;
            case "identical" -> Collator.IDENTICAL;
            default -> throw new UnsupportedCollationException(
                    "Unsupported collation strength: " + strength,
                    metadata
            );
        };
    }

    private static Map<String, String> parseQueryParams(String collationUri, ExceptionMetadata metadata) {
        Map<String, String> params = new HashMap<>();
        int questionMark = collationUri.indexOf('?');
        if (questionMark < 0) {
            return params;
        }
        String query = collationUri.substring(questionMark + 1);
        for (String pair : query.split(";")) {
            if (pair.isEmpty()) {
                continue;
            }
            int equalsIndex = pair.indexOf('=');
            if (equalsIndex < 0) {
                continue;
            }
            try {
                String key = URLDecoder.decode(pair.substring(0, equalsIndex), "UTF-8");
                String value = URLDecoder.decode(pair.substring(equalsIndex + 1), "UTF-8");
                params.put(key, value);
            } catch (UnsupportedEncodingException e) {
                throw new UnsupportedCollationException("Invalid collation URI: " + collationUri, metadata);
            }
        }
        return params;
    }
}
