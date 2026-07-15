package org.rumbledb.compiler;

import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ParsingException;

final class StringLiteralUtils {

    private StringLiteralUtils() {
    }

    static String parseJsoniq(String source, ExceptionMetadata metadata) {
        char delimiter = getDelimiter(source);
        String raw = source.substring(1, source.length() - 1);

        for (int i = 0; i < raw.length(); i++) {
            if (raw.charAt(i) != '\\') {
                continue;
            }
            if (++i == raw.length()) {
                throw new ParsingException("A JSONiq string literal must not end with a backslash.", metadata);
            }

            char escaped = raw.charAt(i);
            if (escaped == 'u') {
                if (i + 4 >= raw.length()) {
                    throw invalidUnicodeEscape(metadata);
                }
                for (int j = 1; j <= 4; j++) {
                    if (Character.digit(raw.charAt(i + j), 16) < 0) {
                        throw invalidUnicodeEscape(metadata);
                    }
                }
                i += 4;
                continue;
            }

            String simpleEscapes = delimiter == '"' ? "\"\\/bfnrt" : "'\\/bfnrt";
            if (simpleEscapes.indexOf(escaped) < 0) {
                throw new ParsingException("Invalid escape sequence in a JSONiq string literal.", metadata);
            }
        }
        return StringEscapeUtils.unescapeJson(raw);
    }

    static String parseXQuery(String source) {
        char delimiter = getDelimiter(source);
        String raw = source.substring(1, source.length() - 1);
        String escapedDelimiter = String.valueOf(delimiter) + delimiter;
        return StringEscapeUtils.unescapeXml(raw.replace(escapedDelimiter, String.valueOf(delimiter)));
    }

    private static char getDelimiter(String source) {
        if (source == null || source.length() < 2) {
            throw new OurBadException("Invalid string literal: " + source);
        }
        char delimiter = source.charAt(0);
        if ((delimiter != '"' && delimiter != '\'') || delimiter != source.charAt(source.length() - 1)) {
            throw new OurBadException("Invalid string literal: " + source);
        }
        return delimiter;
    }

    private static ParsingException invalidUnicodeEscape(ExceptionMetadata metadata) {
        return new ParsingException("A JSONiq Unicode escape must contain four hexadecimal digits.", metadata);
    }
}
