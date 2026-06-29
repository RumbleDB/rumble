package org.rumbledb.runtime.functions.strings;

import java.util.Locale;

final class RegexUnicodeSupport {

    private RegexUnicodeSupport() {
    }

    static String lower(String value) {
        return value.toLowerCase(Locale.ROOT);
    }

    static String upper(String value) {
        return value.toUpperCase(Locale.ROOT);
    }

    static String foldCase(String value) {
        return lower(upper(value));
    }
}
