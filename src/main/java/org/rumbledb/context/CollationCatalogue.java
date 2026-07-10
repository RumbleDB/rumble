package org.rumbledb.context;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Built-in collation URIs known to the implementation.
 */
public final class CollationCatalogue {

    public static final String CODEPOINT_COLLATION = Name.DEFAULT_COLLATION_NS;
    public static final String FOTS_CASEBLIND_COLLATION =
        "http://www.w3.org/2010/09/qt-fots-catalog/collation/caseblind";

    private static final Set<String> DEFAULT_STATICALLY_KNOWN_COLLATIONS;

    static {
        Set<String> collations = new LinkedHashSet<>();
        collations.add(CODEPOINT_COLLATION);
        collations.add(FOTS_CASEBLIND_COLLATION);
        DEFAULT_STATICALLY_KNOWN_COLLATIONS = Collections.unmodifiableSet(collations);
    }

    private CollationCatalogue() {
    }

    public static Set<String> defaultStaticallyKnownCollations() {
        return DEFAULT_STATICALLY_KNOWN_COLLATIONS;
    }

    public static boolean isDefaultStaticallyKnownCollation(String uri) {
        return DEFAULT_STATICALLY_KNOWN_COLLATIONS.contains(uri);
    }
}
