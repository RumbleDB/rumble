package org.rumbledb.compiler;

import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ParsingException;

/** Decodes string literals according to the rules of the source query language. */
final class StringLiteralUtils {

    /** Utility class; instances carry no state. */
    private StringLiteralUtils() {
    }

    /**
     * Validates and decodes a complete JSONiq string literal.
     *
     * <p>
     * The surrounding quote or apostrophe is removed, JSON escapes are expanded, and malformed simple or
     * Unicode escapes are reported with the supplied query metadata. A string may escape its own delimiter but not
     * the other delimiter.
     *
     * @param source the complete literal, including matching delimiters
     * @param metadata location to attach to a syntax error
     * @return the decoded string value
     */
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

    /**
     * Decodes a complete XQuery string literal.
     *
     * <p>
     * The surrounding delimiter is removed, a doubled delimiter is collapsed, and XML character and predefined
     * entity references are expanded.
     *
     * @param source the complete literal, including matching delimiters
     * @return the decoded string value
     */
    static String parseXQuery(String source) {
        char delimiter = getDelimiter(source);
        String raw = source.substring(1, source.length() - 1);
        String escapedDelimiter = String.valueOf(delimiter) + delimiter;
        return StringEscapeUtils.unescapeXml(raw.replace(escapedDelimiter, String.valueOf(delimiter)));
    }

    /**
     * Verifies that a source string is enclosed by matching quote or apostrophe delimiters.
     *
     * @return the shared opening and closing delimiter
     */
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

    /** Creates syntax error used for malformed four-digit Unicode escapes. */
    private static ParsingException invalidUnicodeEscape(ExceptionMetadata metadata) {
        return new ParsingException("A JSONiq Unicode escape must contain four hexadecimal digits.", metadata);
    }
}
