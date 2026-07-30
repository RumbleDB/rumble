package org.rumbledb.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Built-in collation URIs known to the implementation.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollationCatalogue {

    public static final String CODEPOINT_COLLATION = Name.DEFAULT_COLLATION_NS;
    public static final String FOTS_CASEBLIND_COLLATION =
        "http://www.w3.org/2010/09/qt-fots-catalog/collation/caseblind";
    public static final String HTML_ASCII_CASE_INSENSITIVE_COLLATION =
        "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive";
    public static final String UCA_COLLATION_BASE = "http://www.w3.org/2013/collation/UCA";

    private static final Set<String> DEFAULT_STATICALLY_KNOWN_COLLATIONS;

    static {
        Set<String> collations = new LinkedHashSet<>();
        collations.add(CODEPOINT_COLLATION);
        collations.add(FOTS_CASEBLIND_COLLATION);
        collations.add(HTML_ASCII_CASE_INSENSITIVE_COLLATION);
        DEFAULT_STATICALLY_KNOWN_COLLATIONS = Collections.unmodifiableSet(collations);
    }


    public static Set<String> defaultStaticallyKnownCollations() {
        return DEFAULT_STATICALLY_KNOWN_COLLATIONS;
    }

    public static boolean isDefaultStaticallyKnownCollation(String uri) {
        return DEFAULT_STATICALLY_KNOWN_COLLATIONS.contains(uri)
            || UCA_COLLATION_BASE.equals(uri)
            || uri.startsWith(UCA_COLLATION_BASE + "?");
    }

    public static boolean isCaseInsensitiveCollation(String uri) {
        return FOTS_CASEBLIND_COLLATION.equals(uri)
            || HTML_ASCII_CASE_INSENSITIVE_COLLATION.equals(uri)
            || UCA_COLLATION_BASE.equals(uri)
            || uri.startsWith(UCA_COLLATION_BASE + "?");
    }

    public static boolean isUCACollation(String uri) {
        return UCA_COLLATION_BASE.equals(uri)
            || uri.startsWith(UCA_COLLATION_BASE + "?");
    }

    public static boolean isHTMLAsciiCaseInsensitiveCollation(String uri) {
        return HTML_ASCII_CASE_INSENSITIVE_COLLATION.equals(uri);
    }

    public static String normalizeString(String value, String collationUri) {
        if (value == null || !isCaseInsensitiveCollation(collationUri)) {
            return value;
        }
        return value.toLowerCase(Locale.ROOT);
    }
}
