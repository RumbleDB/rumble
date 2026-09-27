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
package org.rumbledb.context;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Built-in collation URIs known to the implementation.
 */
public final class CollationCatalogue {

    public static final String CODEPOINT_COLLATION = Name.DEFAULT_COLLATION_NS;
    public static final String FOTS_CASEBLIND_COLLATION =
            "http://www.w3.org/2010/09/qt-fots-catalog/collation/caseblind";
    public static final String HTML_ASCII_CASE_INSENSITIVE_COLLATION =
            "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive";
    public static final String UCA_COLLATION_BASE = "http://www.w3.org/2013/collation/UCA";

    private static final Set<String> DEFAULT_STATICALLY_KNOWN_COLLATIONS =
            Set.of(CODEPOINT_COLLATION, FOTS_CASEBLIND_COLLATION, HTML_ASCII_CASE_INSENSITIVE_COLLATION);

    private CollationCatalogue() {}

    public static Set<String> defaultStaticallyKnownCollations() {
        return new LinkedHashSet<>(DEFAULT_STATICALLY_KNOWN_COLLATIONS);
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
        return UCA_COLLATION_BASE.equals(uri) || uri.startsWith(UCA_COLLATION_BASE + "?");
    }

    public static boolean isHTMLAsciiCaseInsensitiveCollation(String uri) {
        return HTML_ASCII_CASE_INSENSITIVE_COLLATION.equals(uri);
    }

    public static String toHtmlAsciiLowerCase(String value) {
        if (value == null) {
            return null;
        }
        char[] chars = value.toCharArray();
        boolean changed = false;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c >= 'A' && c <= 'Z') {
                chars[i] = (char) (c + 32);
                changed = true;
            }
        }
        return changed ? new String(chars) : value;
    }

    public static String normalizeString(String value, String collationUri) {
        if (value == null) {
            return null;
        }
        if (isHTMLAsciiCaseInsensitiveCollation(collationUri)) {
            return toHtmlAsciiLowerCase(value);
        }
        if (isCaseInsensitiveCollation(collationUri)) {
            return value.toLowerCase(Locale.ROOT);
        }
        return value;
    }
}
