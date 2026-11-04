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
package org.rumbledb.items.parsing;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.DuplicateJSONKeyException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidJSONException;
import org.rumbledb.exceptions.SourcePosition;
import org.rumbledb.exceptions.SourceRange;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.xml.XMLUtils;

/**
 * Parser for JSON texts used by {@code fn:parse-json} and {@code fn:json-doc}.
 *
 * <p>
 * The parser converts JSON values to RumbleDB items. It supports the
 * {@code parse-json} options {@code liberal}, {@code duplicates},
 * {@code escape}, and {@code fallback}.
 * </p>
 *
 * <p>
 * If {@code liberal} is {@code false}, the input must follow the JSON grammar
 * strictly. If {@code liberal} is {@code true}, this implementation accepts
 * selected JSON extensions, including:
 * </p>
 *
 * <ul>
 * <li>single-quoted strings</li>
 * <li>unquoted object keys</li>
 * <li>comments</li>
 * <li>trailing commas</li>
 * <li>leading plus signs</li>
 * <li>leading zeroes</li>
 * <li>unescaped control characters</li>
 * </ul>
 *
 * <p>
 * String parsing keeps two representations:
 * </p>
 *
 * <ul>
 * <li>
 * {@code resultValue}: the string value stored in the resulting item
 * </li>
 * <li>
 * {@code keyComparisonValue}: the value used to detect duplicate object keys
 * </li>
 * </ul>
 *
 * <p>
 * These can differ when {@code escape=false} and invalid XML characters are
 * replaced using the fallback function.
 * </p>
 */
public final class JSONParser {
    private static final int MAX_NESTING_DEPTH = 1000;

    private final String input;
    private final String inputDescription;
    private final URI resourceUri;
    private final ExceptionMetadata metadata;
    private final JSONParsingOptions options;
    private final String xmlVersion;
    private final boolean isJSONiq10;
    private int position;
    private int nestingDepth;

    private JSONParser(
            String input,
            JSONParsingOptions options,
            String xmlVersion,
            boolean isJSONiq10,
            String inputDescription,
            URI resourceUri,
            ExceptionMetadata metadata) {
        this.resourceUri = resourceUri;
        this.input = input;
        this.inputDescription = inputDescription;
        this.options = options == null ? JSONParsingOptions.defaultInstance(isJSONiq10) : options;
        this.metadata = metadata;
        // Skip a leading BOM without removing it, so diagnostics retain original input offsets.
        this.position = !input.isEmpty() && input.charAt(0) == '\uFEFF' ? 1 : 0;
        this.xmlVersion = xmlVersion;
        this.isJSONiq10 = isJSONiq10;
    }

    // BY CONVENTION JAVA NULL IS THE EMPTY SEQUENCE
    public static Item parse(
            String jsonText,
            JSONParsingOptions options,
            String xmlVersion,
            boolean isJSONiq10,
            ExceptionMetadata metadata) {
        return parse(jsonText, options, xmlVersion, isJSONiq10, "JSON input", metadata);
    }

    /**
     * Parses a string. The description names the input in error messages, e.g. "the argument of fn:parse-json".
     * Exceptions raised by the fallback function propagate without relabeling or wrapping.
     */
    public static Item parse(
            String jsonText,
            JSONParsingOptions options,
            String xmlVersion,
            boolean isJSONiq10,
            String inputDescription,
            ExceptionMetadata metadata) {
        if (jsonText == null) {
            return null;
        }
        return new JSONParser(jsonText, options, xmlVersion, isJSONiq10, inputDescription, null, metadata)
                .parseDocument();
    }

    /**
     * Parses a retrieved resource. Syntax and duplicate-key errors point into the resource;
     * exceptions from user fallback functions retain their own metadata.
     */
    public static Item parseResource(
            String jsonText,
            JSONParsingOptions options,
            String xmlVersion,
            boolean isJSONiq10,
            URI resourceUri,
            String inputDescription,
            ExceptionMetadata metadata) {
        if (jsonText == null) {
            return null;
        }
        return new JSONParser(jsonText, options, xmlVersion, isJSONiq10, inputDescription, resourceUri, metadata)
                .parseDocument();
    }

    // BY CONVENTION JAVA NULL IS THE EMPTY SEQUENCE
    private Item parseDocument() {
        skipIgnorable();
        Item result = parseValue();
        skipIgnorable();
        if (!isEnd()) {
            throw invalidJSON(
                    "Extra content found after the end of the JSON value. JSON is not well-formed!", this.position);
        }
        return result;
    }

    // BY CONVENTION JAVA NULL IS THE EMPTY SEQUENCE
    private Item parseValue() {
        skipIgnorable();

        if (isEnd()) {
            throw invalidJSON("Unexpected end of input while parsing JSON value.", this.position);
        }

        char c = peek();
        switch (c) {
            case '{':
                return parseObject();
            case '[':
                return parseArray();
            case '"':
                return ItemFactory.getInstance().createStringItem(parseString().resultValue);
            case '\'':
                if (this.options.isLiberal()) {
                    return ItemFactory.getInstance().createStringItem(parseString().resultValue);
                }
                throw invalidJSON(
                        "Single-quoted strings are not allowed unless option 'liberal' is true.", this.position);
            case 't':
                parseLiteral("true");
                return ItemFactory.getInstance().createBooleanItem(true);
            case 'f':
                parseLiteral("false");
                return ItemFactory.getInstance().createBooleanItem(false);
            case 'n':
                parseLiteral("null");
                if (this.isJSONiq10) return ItemFactory.getInstance().createNullItem();
                return null;
            default:
                if (c == '-' || isDigit(c) || (this.options.isLiberal() && c == '+')) {
                    return parseNumber();
                }
                throw invalidJSON(
                        "Unexpected character '" + printable(c) + "' while parsing JSON value.", this.position);
        }
    }

    // NEVER RETURNS A JAVA NULL
    private Item parseObject() {
        enterContainer();
        try {
            return parseObjectBody();
        } finally {
            exitContainer();
        }
    }

    private Item parseObjectBody() {
        expect('{');
        skipIgnorable();

        List<String> keys = new ArrayList<>();
        List<Item> values = new ArrayList<>(); // BY CONVENTION A JAVA NULL MEANS AN EMPTY SEQUENCE
        Map<String, Integer> seen = new LinkedHashMap<>();

        if (tryConsume('}')) {
            return ItemFactory.getInstance().createObjectItem(keys, values, this.metadata, false);
        }

        while (true) {
            skipIgnorable();

            if (isEnd()) {
                throw invalidJSON("Unexpected end of input while parsing JSON object.", this.position);
            }

            int keyStart = this.position;
            ParsedString key;
            char c = peek();
            if (c == '"' || (this.options.isLiberal() && c == '\'')) {
                key = parseString();
            } else if (this.options.isLiberal()) {
                key = parseUnquotedKey();
            } else {
                throw invalidJSON("Expected object key string.", this.position);
            }

            int keyEnd = this.position;
            skipIgnorable();
            expect(':');
            skipIgnorable();

            Item parsedValue = parseValue();

            Integer existingIndex = seen.get(key.keyComparisonValue);

            if (existingIndex == null) {
                seen.put(key.keyComparisonValue, keys.size());
                keys.add(key.resultValue);
                values.add(parsedValue);
            } else {
                String policy = this.options.getDuplicates();

                if (JSONParsingOptions.DUPLICATES_REJECT.equals(policy)) {
                    throw new DuplicateJSONKeyException(
                            describe("Duplicate key '" + key.resultValue + "' found in JSON object.", keyStart),
                            errorMetadata(keyStart, keyEnd));
                }

                if (JSONParsingOptions.DUPLICATES_USE_LAST.equals(policy)) {
                    keys.set(existingIndex, key.resultValue);
                    values.set(existingIndex, parsedValue);
                }
            }

            skipIgnorable();
            if (tryConsume('}')) {
                break;
            }

            expect(',');

            skipIgnorable();
            if (this.options.isLiberal() && tryConsume('}')) {
                break;
            }
        }

        boolean containsJAVANull = false;
        for (Item item : values) {
            if (item == null) {
                containsJAVANull = true;
                break;
            }
        }

        if (!containsJAVANull) {
            return ItemFactory.getInstance().createObjectItem(keys, values, this.metadata, false);
        } else {
            List<Item> newKeys = new ArrayList<>();
            List<List<Item>> newValues = new ArrayList<>();
            for (String key : keys) {
                newKeys.add(ItemFactory.getInstance().createStringItem(key));
            }
            for (Item value : values) {
                if (value == null) newValues.add(Collections.emptyList());
                else newValues.add(Collections.singletonList(value));
            }
            return ItemFactory.getInstance().createMapItem(newKeys, newValues, this.metadata, false);
        }
    }

    private Item parseArray() {
        enterContainer();
        try {
            return parseArrayBody();
        } finally {
            exitContainer();
        }
    }

    private Item parseArrayBody() {
        expect('[');
        skipIgnorable();

        List<Item> members = new ArrayList<>();

        if (tryConsume(']')) {
            return ItemFactory.getInstance().createArrayItem(members, false);
        }

        while (true) {
            skipIgnorable();

            Item parsedMember = parseValue();
            members.add(parsedMember);

            skipIgnorable();

            if (tryConsume(']')) {
                break;
            }

            expect(',');

            skipIgnorable();

            if (this.options.isLiberal() && tryConsume(']')) {
                break;
            }
        }

        boolean containsJAVANull = false;

        for (Item member : members) {
            if (member == null) {
                containsJAVANull = true;
                break;
            }
        }

        if (!containsJAVANull) {
            return ItemFactory.getInstance().createArrayItem(members, false);
        } else {
            List<List<Item>> newMembers = new ArrayList<>();

            for (Item member : members) {
                if (member == null) {
                    newMembers.add(Collections.emptyList());
                } else {
                    newMembers.add(Collections.singletonList(member));
                }
            }

            return ItemFactory.getInstance().createSequenceArrayItem(newMembers, false);
        }
    }

    private Item parseNumber() {
        int start = this.position;

        if (peek() == '+' && this.options.isLiberal()) {
            advance();
        } else if (peek() == '-') {
            advance();
        }

        if (isEnd()) {
            throw invalidJSON("Unexpected end of input while parsing number.", this.position);
        }

        if (peek() == '0') {
            advance();
            if (!isEnd() && isDigit(peek()) && !this.options.isLiberal()) {
                throw invalidJSON("Leading zeroes are not allowed in JSON numbers.", this.position);
            }
            while (this.options.isLiberal() && !isEnd() && isDigit(peek())) {
                advance();
            }
        } else {
            if (!isDigit19(peek())) {
                throw invalidJSON("Invalid number: expected digit.", this.position);
            }
            while (!isEnd() && isDigit(peek())) {
                advance();
            }
        }

        if (!isEnd() && peek() == '.') {
            advance();
            if (isEnd() || !isDigit(peek())) {
                throw invalidJSON("Invalid number: expected digit after decimal point.", this.position);
            }
            while (!isEnd() && isDigit(peek())) {
                advance();
            }
        }

        if (!isEnd() && (peek() == 'e' || peek() == 'E')) {
            advance();
            if (!isEnd() && (peek() == '+' || peek() == '-')) {
                advance();
            }
            if (isEnd() || !isDigit(peek())) {
                throw invalidJSON("Invalid number: expected digit in exponent.", this.position);
            }
            while (!isEnd() && isDigit(peek())) {
                advance();
            }
        }

        String number = this.input.substring(start, this.position);
        try {
            return JSONLiteralParsingUtils.getItemFromJSONNumber(number, this.options.getNumberFormat());
        } catch (NumberFormatException e) {
            InvalidJSONException error = invalidJSON("Invalid number literal '" + number + "'.", start);
            error.initCause(e);
            throw error;
        }
    }

    /**
     * Parses a quoted JSON string and returns both:
     *
     * <ul>
     * <li>the result string value</li>
     * <li>the value used for duplicate-key comparison</li>
     * </ul>
     *
     * <p>
     * The result value depends on the {@code escape} option:
     * </p>
     *
     * <ul>
     * <li>
     * {@code escape=true}: special characters are represented using JSON escape syntax
     * </li>
     * <li>
     * {@code escape=false}: valid XML characters are inserted directly; invalid XML
     * characters are replaced using the fallback function
     * </li>
     * </ul>
     *
     * <p>
     * The key comparison value follows the duplicate-key rules:
     * </p>
     *
     * <ul>
     * <li>
     * {@code escape=true}: keys are compared in escaped form
     * </li>
     * <li>
     * {@code escape=false}: keys are compared after expanding escape sequences
     * </li>
     * </ul>
     */
    private ParsedString parseString() {
        if (isEnd()) {
            throw invalidJSON("Unexpected end of input while parsing string.", this.position);
        }

        char quote = peek();
        if (quote != '"' && !(this.options.isLiberal() && quote == '\'')) {
            throw invalidJSON("Expected string literal.", this.position);
        }
        advance();

        StringBuilder resultValue = new StringBuilder();
        StringBuilder keyComparisonValue = null;

        while (!isEnd()) {
            char c = advance();

            if (c == quote) {
                String result = resultValue.toString();
                return new ParsedString(result, keyComparisonValue == null ? result : keyComparisonValue.toString());
            }

            if (c == '\\') {
                if (isEnd()) {
                    throw invalidJSON("Unterminated escape sequence in string.", this.position);
                }

                if (peek() == '\'' && this.options.isLiberal()) {
                    advance();
                    keyComparisonValue = handleEscapedCodePoint(resultValue, keyComparisonValue, '\'', "\\'");
                    continue;
                }

                JSONLiteralParsingUtils.DecodedEscape decodedEscape;
                try {
                    decodedEscape = JSONLiteralParsingUtils.decodeEscapeSequence(this.input, this.position - 1);
                } catch (IllegalArgumentException e) {
                    InvalidJSONException error = invalidJSON(e.getMessage(), this.position - 1);
                    error.initCause(e);
                    throw error;
                }
                this.position = decodedEscape.getNextIndex();
                keyComparisonValue = appendDecodedEscape(
                        resultValue, keyComparisonValue, decodedEscape.getDecodedText(), decodedEscape.getRawEscape());
                continue;
            }

            if (c <= 0x1F) {
                if (!this.options.isLiberal()) {
                    throw invalidJSON(
                            "Unescaped control character U+" + hex4(c) + " is not allowed in JSON strings.",
                            this.position - 1);
                }
            }

            if (this.options.isEscape() && shouldEscapeInOutput(c)) {
                String escaped = normalizedEscapeForCodePoint(c);
                resultValue.append(escaped);
                if (keyComparisonValue != null) {
                    keyComparisonValue.append(escaped);
                }
            } else {
                resultValue.append(c);
                if (keyComparisonValue != null) {
                    keyComparisonValue.append(c);
                }
            }
        }

        throw invalidJSON("Unterminated string literal.", this.position);
    }

    private StringBuilder appendDecodedEscape(
            StringBuilder resultValue, StringBuilder keyComparisonValue, String decodedText, String originalEscape) {
        if (decodedText.length() == 2 && Character.isSurrogatePair(decodedText.charAt(0), decodedText.charAt(1))) {
            return handleEscapedCodePoint(
                    resultValue,
                    keyComparisonValue,
                    Character.toCodePoint(decodedText.charAt(0), decodedText.charAt(1)),
                    originalEscape);
        }
        return handleEscapedCodePoint(resultValue, keyComparisonValue, decodedText.charAt(0), originalEscape);
    }

    /**
     * Handles a decoded JSON escape sequence.
     *
     * <p>
     * Maintains two string representations:
     * </p>
     *
     * <ul>
     * <li>
     * {@code resultValue}: the actual string value stored in the result
     * </li>
     * <li>
     * {@code keyComparisonValue}: the string used for duplicate-key comparison
     * </li>
     * </ul>
     *
     * <p>
     * Behavior depends on the {@code escape} option:
     * </p>
     *
     * <ul>
     * <li>
     * {@code escape=true}: special characters are represented in escaped form
     * in both values
     * </li>
     * <li>
     * {@code escape=false}: {@code resultValue} may use the fallback for invalid XML
     * characters, while {@code keyComparisonValue} keeps the decoded character
     * for duplicate-key comparison
     * </li>
     * </ul>
     */
    private StringBuilder handleEscapedCodePoint(
            StringBuilder resultValue, StringBuilder keyComparisonValue, int codePoint, String originalEscape) {
        if (this.options.isEscape()) {
            if (shouldEscapeInOutput(codePoint)) {
                String escaped = normalizedEscapeForCodePoint(codePoint);
                resultValue.append(escaped);
                if (keyComparisonValue != null) {
                    keyComparisonValue.append(escaped);
                }
            } else {
                resultValue.appendCodePoint(codePoint);
                if (keyComparisonValue != null) {
                    keyComparisonValue.appendCodePoint(codePoint);
                }
            }
            return keyComparisonValue;
        }

        if (isValidXMLCodePoint(codePoint)) {
            resultValue.appendCodePoint(codePoint);
            if (keyComparisonValue != null) {
                keyComparisonValue.appendCodePoint(codePoint);
            }
            return keyComparisonValue;
        }

        // The only point where resultValue and keyComparisonValue can actually diverge: fork
        // keyComparisonValue from resultValue's content built so far, the first time this happens.
        if (keyComparisonValue == null) {
            keyComparisonValue = new StringBuilder(resultValue);
        }
        resultValue.append(this.options.getFallback().apply(originalEscape));
        keyComparisonValue.appendCodePoint(codePoint);
        return keyComparisonValue;
    }

    private String normalizedEscapeForCodePoint(int codePoint) {
        switch (codePoint) {
            case '\\':
                return "\\\\";
            case '\b':
                return "\\b";
            case '\f':
                return "\\f";
            case '\n':
                return "\\n";
            case '\r':
                return "\\r";
            case '\t':
                return "\\t";
            default:
                break;
        }

        if (codePoint <= 0xFFFF) {
            return "\\u" + hex4(codePoint);
        }

        char[] pair = Character.toChars(codePoint);
        return "\\u" + hex4(pair[0]) + "\\u" + hex4(pair[1]);
    }

    /**
     * Returns true if a codepoint must be represented using a JSON escape sequence
     * in the output when option escape=true is enabled.
     */
    private boolean shouldEscapeInOutput(int codePoint) {
        return codePoint == '\\' || XMLUtils.isControlCharacter(codePoint) || !isValidXMLCodePoint(codePoint);
    }

    /**
     * Parses an unquoted object key accepted by this implementation in liberal mode.
     * This is not valid standard JSON. It is an implementation-defined extension
     * enabled only when {@code liberal=true}.
     */
    private ParsedString parseUnquotedKey() {
        if (!this.options.isLiberal()) {
            throw invalidJSON("Unquoted object keys are not allowed unless option 'liberal' is true.", this.position);
        }

        int start = this.position;
        char first = peek();
        if (!isIdentifierStart(first)) {
            throw invalidJSON("Invalid unquoted object key.", this.position);
        }

        advance();
        while (!isEnd() && isIdentifierPart(peek())) {
            advance();
        }

        String key = this.input.substring(start, this.position);
        return new ParsedString(key, key);
    }

    private void parseLiteral(String literal) {
        for (int i = 0; i < literal.length(); i++) {
            if (isEnd() || peek() != literal.charAt(i)) {
                throw invalidJSON("Expected literal '" + literal + "'.", this.position);
            }
            advance();
        }
    }

    /**
     * Skips whitespace. In liberal mode, this implementation also skips line and
     * block comments.
     */
    private void skipIgnorable() {
        while (!isEnd()) {
            char c = peek();

            if (isJSONWhitespace(c)) {
                advance();
                continue;
            }

            if (this.options.isLiberal() && c == '/') {
                if (this.position + 1 < this.input.length()) {
                    char next = this.input.charAt(this.position + 1);

                    if (next == '/') {
                        this.position += 2;
                        while (!isEnd() && peek() != '\n' && peek() != '\r') {
                            advance();
                        }
                        continue;
                    }

                    if (next == '*') {
                        this.position += 2;
                        while (true) {
                            if (isEnd()) {
                                throw invalidJSON("Unterminated block comment.", this.position);
                            }
                            if (peek() == '*'
                                    && this.position + 1 < this.input.length()
                                    && this.input.charAt(this.position + 1) == '/') {
                                this.position += 2;
                                break;
                            }
                            advance();
                        }
                        continue;
                    }
                }
            }
            break;
        }
    }

    /**
     * Advances the parser if next consumable token matches `expected`
     *
     * @param expected next expected character
     * @throws InvalidJSONException (FOJS0001), if expected token doesn't match actual token or end is reached
     */
    private void expect(char expected) {
        if (isEnd() || peek() != expected) {
            throw invalidJSON(
                    "Expected '"
                            + expected
                            + "', but found "
                            + (isEnd() ? "end of input" : "'" + printable(peek()) + "'")
                            + ".",
                    this.position);
        }
        advance();
    }

    /**
     * @param expected next expected character
     * @return `true` if `expected` matches next token and EOF is not reached, `false` otherwise
     */
    private boolean tryConsume(char expected) {
        if (!isEnd() && peek() == expected) {
            advance();
            return true;
        }
        return false;
    }

    private void enterContainer() {
        this.nestingDepth++;
        if (this.nestingDepth > MAX_NESTING_DEPTH) {
            throw invalidJSON(
                    "JSON nesting depth exceeds the maximum supported depth of " + MAX_NESTING_DEPTH + ".",
                    this.position);
        }
    }

    private InvalidJSONException invalidJSON(String message, int offset) {
        return new InvalidJSONException(describe(message, offset), errorMetadata(offset, characterEnd(offset)));
    }

    private String describe(String message, int offset) {
        SourcePosition position = inputPosition(offset);
        return "Unable to parse "
                + this.inputDescription
                + " at line "
                + position.line()
                + ", column "
                + (position.column() + 1)
                + ": "
                + message;
    }

    /** Points into the resource when it has a URI, and to the caller otherwise. */
    private ExceptionMetadata errorMetadata(int startOffset, int endOffset) {
        if (this.resourceUri == null) {
            return this.metadata;
        }
        return new ExceptionMetadata(
                this.resourceUri.toString(), new SourceRange(inputPosition(startOffset), inputPosition(endOffset)), "");
    }

    /** The end of the character at the offset, treating a CRLF line break as one character. */
    private int characterEnd(int offset) {
        if (offset >= this.input.length()) {
            return offset;
        }
        int end = offset + Character.charCount(this.input.codePointAt(offset));
        if (this.input.charAt(offset) == '\r' && end < this.input.length() && this.input.charAt(end) == '\n') {
            end++;
        }
        return end;
    }

    /** Scans only on failure; successful parsing pays no line/column tracking cost. */
    private SourcePosition inputPosition(int offset) {
        int line = 1;
        int column = 0;
        for (int i = 0; i < offset; i++) {
            char c = this.input.charAt(i);
            if (c == '\r' || c == '\n') {
                if (c == '\r' && i + 1 < offset && this.input.charAt(i + 1) == '\n') {
                    i++;
                }
                line++;
                column = 0;
            } else {
                column++;
            }
        }
        return new SourcePosition(line, column);
    }

    private void exitContainer() {
        this.nestingDepth--;
    }

    /**
     * @return true if parser position has reached EOF
     */
    private boolean isEnd() {
        return this.position >= this.input.length();
    }

    /**
     * @return next consumable character
     */
    private char peek() {
        return this.input.charAt(this.position);
    }

    /**
     * Advances the parser by one position
     *
     * @return new current character
     */
    private char advance() {
        return this.input.charAt(this.position++);
    }

    /**
     *
     * @param c
     * @return true if c is an ASCII number
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * @param c
     * @return true if c is an ASCII number excluding zero
     */
    private boolean isDigit19(char c) {
        return c >= '1' && c <= '9';
    }

    /**
     * Returns whether a character is valid as the first character of an identifier.
     */
    private boolean isIdentifierStart(char c) {
        return Character.isLetter(c) || c == '_' || c == '$';
    }

    /**
     * Returns whether a character is valid inside an identifier after the first character.
     */
    private boolean isIdentifierPart(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '$' || c == '-';
    }

    private String hex4(int value) {
        return String.format("%04X", value & 0xFFFF);
    }

    private boolean isValidXMLCodePoint(int codepoint) {
        return XMLUtils.isValidXmlCharacter(codepoint, this.xmlVersion);
    }

    private String printable(char c) {
        if (c < 0x20 || c == 0x7F) {
            return "\\u" + hex4(c);
        }
        return String.valueOf(c);
    }

    private boolean isJSONWhitespace(char c) {
        return c == 0x20 || c == 0x09 || c == 0x0A || c == 0x0D;
    }

    /**
     * Result of parsing a JSON string.
     *
     * <p>
     * Contains two string representations:
     * </p>
     *
     * <ul>
     * <li>
     * {@code resultValue}: the string value stored in the resulting item
     * </li>
     * <li>
     * {@code keyComparisonValue}: the string used to detect duplicate object keys
     * </li>
     * </ul>
     *
     * <p>
     * The {@code keyComparisonValue} depends on the {@code escape} option:
     * </p>
     *
     * <ul>
     * <li>
     * {@code escape=true}: uses the escaped form
     * </li>
     * <li>
     * {@code escape=false}: uses the decoded form
     * </li>
     * </ul>
     *
     * <p>
     * The two values can differ when {@code escape=false} and a decoded character
     * is not valid in the configured XML version. In that case:
     * </p>
     *
     * <ul>
     * <li>
     * {@code resultValue}: contains the fallback result
     * </li>
     * <li>
     * {@code keyComparisonValue}: keeps the decoded character for duplicate-key comparison
     * </li>
     * </ul>
     */
    private static final class ParsedString {
        private final String resultValue;
        private final String keyComparisonValue;

        private ParsedString(String resultValue, String keyComparisonValue) {
            this.resultValue = resultValue;
            this.keyComparisonValue = keyComparisonValue;
        }
    }
}
