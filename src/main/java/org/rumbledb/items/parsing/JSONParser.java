package org.rumbledb.items.parsing;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.DuplicateJSONKeyException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidJSONException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.json.JSONParsingOptions;
import org.rumbledb.runtime.functions.xml.XMLUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Parser for JSON texts used by fn:parse-json and fn:json-doc.
 *
 * The parser converts JSON values to RumbleDB items. It supports the parse-json
 * options liberal, duplicates, escape, and fallback.
 *
 * If liberal is false, the input must follow the JSON grammar strictly. If liberal
 * is true, this implementation accepts selected JSON extensions such as single-quoted
 * strings, unquoted object keys, comments, trailing commas, leading plus signs, leading
 * zeroes, and unescaped control characters.
 *
 * String parsing keeps two representations:
 * - xdmValue: the string value stored in the resulting XDM item.
 * - keyComparisonValue: the value used to detect duplicate object keys.
 *
 * These can differ when escape=false and invalid XML characters are replaced using
 * the fallback function.
 */
public final class JSONParser {

    private final String input;
    private final ExceptionMetadata metadata;
    private final JSONParsingOptions options;
    private final String xmlVersion;
    private int position;

    private JSONParser(String input, JSONParsingOptions options, String xmlVersion, ExceptionMetadata metadata) {
        if (input != null && !input.isEmpty() && input.charAt(0) == '\uFEFF') {
            this.input = input.substring(1);
        } else {
            this.input = input;
        }
        this.options = options == null ? JSONParsingOptions.defaultInstance() : options;
        this.metadata = metadata;
        this.position = 0;
        this.xmlVersion = xmlVersion;
    }

    public static Item parse(String jsonText, ExceptionMetadata metadata) {
        return parse(jsonText, JSONParsingOptions.defaultInstance(), XMLUtils.defaultXMLVersion(), metadata);
    }

    public static Item parse(
            String jsonText,
            JSONParsingOptions options,
            String xmlVersion,
            ExceptionMetadata metadata
    ) {
        if (jsonText == null) {
            return null;
        }
        JSONParser parser = new JSONParser(jsonText, options, xmlVersion, metadata);
        return parser.parseDocument();
    }

    private Item parseDocument() {
        skipIgnorable();
        Item result = parseValue();
        skipIgnorable();
        if (!isEnd()) {
            throw new InvalidJSONException(
                    "Extra content found after the end of the JSON value. JSON is not well-formed! [position "
                        + this.position
                        + "]",
                    this.metadata
            );
        }
        return result;
    }

    private Item parseValue() {
        skipIgnorable();

        if (isEnd()) {
            throw new InvalidJSONException(
                    "Unexpected end of input while parsing JSON value. [position " + this.position + "]",
                    this.metadata
            );
        }

        char c = peek();
        switch (c) {
            case '{':
                return parseObject();
            case '[':
                return parseArray();
            case '"':
                return ItemFactory.getInstance().createStringItem(parseString().xdmValue);
            case '\'':
                if (this.options.isLiberal()) {
                    return ItemFactory.getInstance().createStringItem(parseString().xdmValue);
                }
                throw new InvalidJSONException(
                        "Single-quoted strings are not allowed unless option 'liberal' is true. [position "
                            + this.position
                            + "]",
                        this.metadata
                );
            case 't':
                parseLiteral("true");
                return ItemFactory.getInstance().createBooleanItem(true);
            case 'f':
                parseLiteral("false");
                return ItemFactory.getInstance().createBooleanItem(false);
            case 'n':
                parseLiteral("null");
                return null;
            default:
                if (c == '-' || isDigit(c) || (this.options.isLiberal() && c == '+')) {
                    return parseNumber();
                }
                throw new InvalidJSONException(
                        "Unexpected character '"
                            + printable(c)
                            + "' while parsing JSON value. [position "
                            + this.position
                            + "]",
                        this.metadata
                );
        }
    }

    private Item parseObject() {
        expect('{');
        skipIgnorable();

        List<String> keys = new ArrayList<>();
        List<Item> values = new ArrayList<>();
        Map<String, Integer> seen = new LinkedHashMap<>();

        if (tryConsume('}')) {
            return ItemFactory.getInstance().createObjectItem(keys, values, this.metadata, false);
        }

        while (true) {
            skipIgnorable();

            ParsedString key;
            char c = peek();
            if (c == '"' || (this.options.isLiberal() && c == '\'')) {
                key = parseString();
            } else if (this.options.isLiberal()) {
                key = parseUnquotedKey();
            } else {
                throw new InvalidJSONException(
                        "Expected object key string. [position " + this.position + "]",
                        this.metadata
                );
            }

            skipIgnorable();
            expect(':');
            skipIgnorable();

            Item parsedValue = parseValue();
            Item storedValue = parsedValue == null
                ? ItemFactory.getInstance().createNullItem()
                : parsedValue;

            Integer existingIndex = seen.get(key.keyComparisonValue);

            if (existingIndex == null) {
                seen.put(key.keyComparisonValue, keys.size());
                keys.add(key.xdmValue);
                values.add(storedValue);
            } else {
                String policy = this.options.getDuplicates();

                if (JSONParsingOptions.DUPLICATES_REJECT.equals(policy)) {
                    throw new DuplicateJSONKeyException(
                            "Duplicate key '" + key.xdmValue + "' found in JSON object.",
                            this.metadata
                    );
                }

                if (JSONParsingOptions.DUPLICATES_USE_LAST.equals(policy)) {
                    keys.set(existingIndex, key.xdmValue);
                    values.set(existingIndex, storedValue);
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

        Item objectItem = ItemFactory.getInstance().createObjectItem(keys, values, this.metadata, false);
        return objectItem;
    }

    private Item parseArray() {
        expect('[');
        skipIgnorable();

        List<Item> members = new ArrayList<>();
        if (tryConsume(']')) {
            return ItemFactory.getInstance().createArrayItem(members, false);
        }

        while (true) {
            skipIgnorable();

            Item parsedMember = parseValue();
            Item storedMember = parsedMember == null
                ? ItemFactory.getInstance().createNullItem()
                : parsedMember;
            members.add(storedMember);

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

        return ItemFactory.getInstance().createArrayItem(members, false);
    }

    private Item parseNumber() {
        int start = this.position;

        if (peek() == '+' && this.options.isLiberal()) {
            advance();
        } else if (peek() == '-') {
            advance();
        }

        if (isEnd()) {
            throw new InvalidJSONException(
                    "Unexpected end of input while parsing number. [position " + this.position + "]",
                    this.metadata
            );
        }

        if (peek() == '0') {
            advance();
            if (!isEnd() && isDigit(peek()) && !this.options.isLiberal()) {
                throw new InvalidJSONException(
                        "Leading zeroes are not allowed in JSON numbers. [position " + this.position + "]",
                        this.metadata
                );
            }
            while (this.options.isLiberal() && !isEnd() && isDigit(peek())) {
                advance();
            }
        } else {
            if (!isDigit19(peek())) {
                throw new InvalidJSONException(
                        "Invalid number: expected digit. [position " + this.position + "]",
                        this.metadata
                );
            }
            while (!isEnd() && isDigit(peek())) {
                advance();
            }
        }

        if (!isEnd() && peek() == '.') {
            advance();
            if (isEnd() || !isDigit(peek())) {
                throw new InvalidJSONException(
                        "Invalid number: expected digit after decimal point. [position " + this.position + "]",
                        this.metadata
                );
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
                throw new InvalidJSONException(
                        "Invalid number: expected digit in exponent. [position " + this.position + "]",
                        this.metadata
                );
            }
            while (!isEnd() && isDigit(peek())) {
                advance();
            }
        }

        String number = this.input.substring(start, this.position);
        try {
            return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(number));
        } catch (NumberFormatException e) {
            InvalidJSONException error = new InvalidJSONException(
                    "Invalid number literal '" + number + "'. [position " + start + "]",
                    this.metadata
            );
            error.initCause(e);
            throw error;
        }
    }

    /**
     * Parses a quoted JSON string and returns both the XDM string value and the
     * value used for duplicate-key comparison.
     *
     * The XDM value depends on the escape option:
     * - escape=true: special characters are represented using JSON escape syntax.
     * - escape=false: valid XML characters are inserted directly; invalid XML
     * characters are replaced using the fallback function.
     *
     * The key comparison value follows the duplicate-key rules:
     * - escape=true: keys are compared in escaped form.
     * - escape=false: keys are compared after expanding escape sequences.
     */
    private ParsedString parseString() {
        if (isEnd()) {
            throw new InvalidJSONException(
                    "Unexpected end of input while parsing string. [position " + this.position + "]",
                    this.metadata
            );
        }

        char quote = peek();
        if (quote != '"' && !(this.options.isLiberal() && quote == '\'')) {
            throw new InvalidJSONException(
                    "Expected string literal. [position " + this.position + "]",
                    this.metadata
            );
        }
        advance();

        StringBuilder xdmValue = new StringBuilder();
        StringBuilder keyComparisonValue = new StringBuilder();

        while (!isEnd()) {
            char c = advance();

            if (c == quote) {
                return new ParsedString(
                        xdmValue.toString(),
                        keyComparisonValue.toString()
                );
            }

            if (c == '\\') {
                if (isEnd()) {
                    throw new InvalidJSONException(
                            "Unterminated escape sequence in string. [position " + this.position + "]",
                            this.metadata
                    );
                }

                char esc = advance();
                switch (esc) {
                    case '"':
                        handleEscapedCodePoint(xdmValue, keyComparisonValue, '"', "\\\"");
                        break;
                    case '\\':
                        handleEscapedCodePoint(xdmValue, keyComparisonValue, '\\', "\\\\");
                        break;
                    case '/':
                        handleEscapedCodePoint(xdmValue, keyComparisonValue, '/', "\\/");
                        break;
                    case 'b':
                        handleEscapedCodePoint(xdmValue, keyComparisonValue, '\b', "\\b");
                        break;
                    case 'f':
                        handleEscapedCodePoint(xdmValue, keyComparisonValue, '\f', "\\f");
                        break;
                    case 'n':
                        handleEscapedCodePoint(xdmValue, keyComparisonValue, '\n', "\\n");
                        break;
                    case 'r':
                        handleEscapedCodePoint(xdmValue, keyComparisonValue, '\r', "\\r");
                        break;
                    case 't':
                        handleEscapedCodePoint(xdmValue, keyComparisonValue, '\t', "\\t");
                        break;
                    case '\'':
                        if (!this.options.isLiberal()) {
                            throw new InvalidJSONException(
                                    "Invalid escape sequence \\" + esc + " in string. [position " + this.position + "]",
                                    this.metadata
                            );
                        }
                        handleEscapedCodePoint(xdmValue, keyComparisonValue, '\'', "\\'");
                        break;
                    case 'u':
                        parseUnicodeEscape(xdmValue, keyComparisonValue);
                        break;
                    default:
                        throw new InvalidJSONException(
                                "Invalid escape sequence \\" + esc + " in string. [position " + this.position + "]",
                                this.metadata
                        );
                }
                continue;
            }

            if (c <= 0x1F) {
                if (!this.options.isLiberal()) {
                    throw new InvalidJSONException(
                            "Unescaped control character U+"
                                + hex4(c)
                                + " is not allowed in JSON strings. [position "
                                + this.position
                                + "]",
                            this.metadata
                    );
                }
            }

            if (this.options.isEscape() && shouldEscapeInXdmOutput(c)) {
                String escaped = normalizedEscapeForCodePoint(c);
                xdmValue.append(escaped);
                keyComparisonValue.append(escaped);
            } else {
                xdmValue.append(c);
                keyComparisonValue.append(c);
            }
        }

        throw new InvalidJSONException(
                "Unterminated string literal. [position " + this.position + "]",
                this.metadata
        );
    }

    /**
     * Parses a JSON Unicode escape sequence after the leading "\\u" has already
     * been consumed.
     *
     * Handles surrogate pairs such as "\\uD83D\\uDE00" as a single Unicode codepoint.
     * Unpaired surrogates are handled as invalid XML characters: with escape=true
     * they are kept as "\\uXXXX"; with escape=false they are passed to the fallback
     * function for the XDM value.
     */
    private void parseUnicodeEscape(
            StringBuilder xdmValue,
            StringBuilder keyComparisonValue
    ) {
        int first = parseHexQuad();
        String firstEscape = "\\u" + hex4(first);

        if (Character.isHighSurrogate((char) first)) {
            int save = this.position;

            if (canReadLowSurrogateEscape()) {
                expect('\\');
                expect('u');

                int second = parseHexQuad();
                String secondEscape = "\\u" + hex4(second);

                if (Character.isLowSurrogate((char) second)) {
                    int codePoint = Character.toCodePoint((char) first, (char) second);

                    handleEscapedCodePoint(
                        xdmValue,
                        keyComparisonValue,
                        codePoint,
                        firstEscape + secondEscape
                    );
                    return;
                }

                this.position = save;
            }

            handleEscapedCodePoint(
                xdmValue,
                keyComparisonValue,
                first,
                firstEscape
            );
            return;
        }
        if (Character.isLowSurrogate((char) first)) {
            handleEscapedCodePoint(
                xdmValue,
                keyComparisonValue,
                first,
                firstEscape
            );
            return;
        }
        handleEscapedCodePoint(
            xdmValue,
            keyComparisonValue,
            first,
            firstEscape
        );
    }

    /**
     * Handles a decoded JSON escape sequence.
     *
     * xdmValue is the actual string value stored in the XDM result.
     * keyComparisonValue is the string used for duplicate-key comparison.
     *
     * If escape=true, special characters are represented in escaped form in both values.
     * If escape=false, xdmValue may use the fallback for invalid XML characters, while
     * keyComparisonValue keeps the decoded character for duplicate-key comparison.
     */
    private void handleEscapedCodePoint(
            StringBuilder xdmValue,
            StringBuilder keyComparisonValue,
            int codePoint,
            String originalEscape
    ) {
        if (this.options.isEscape()) {
            if (shouldEscapeInXdmOutput(codePoint)) {
                String escaped = normalizedEscapeForCodePoint(codePoint);
                xdmValue.append(escaped);
                keyComparisonValue.append(escaped);
            } else {
                xdmValue.appendCodePoint(codePoint);
                keyComparisonValue.appendCodePoint(codePoint);
            }
            return;
        }

        if (isValidXMLCodePoint(codePoint)) {
            xdmValue.appendCodePoint(codePoint);
        } else {
            xdmValue.append(this.options.getFallback().apply(originalEscape));
        }

        keyComparisonValue.appendCodePoint(codePoint);
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
     * in the XDM output when option escape=true is enabled.
     */
    private boolean shouldEscapeInXdmOutput(int codePoint) {
        return codePoint == '\\'
            || XMLUtils.isControlCharacter(codePoint)
            || !isValidXMLCodePoint(codePoint);
    }

    /**
     * Parses an unquoted object key accepted by this implementation in liberal mode.
     *
     * This is not valid standard JSON. It is an implementation-defined extension
     * enabled only when liberal=true.
     */
    private ParsedString parseUnquotedKey() {
        if (!this.options.isLiberal()) {
            throw new InvalidJSONException(
                    "Unquoted object keys are not allowed unless option 'liberal' is true. [position "
                        + this.position
                        + "]",
                    this.metadata
            );
        }

        int start = this.position;
        char first = peek();
        if (!isIdentifierStart(first)) {
            throw new InvalidJSONException(
                    "Invalid unquoted object key. [position " + this.position + "]",
                    this.metadata
            );
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
            if (isEnd() || advance() != literal.charAt(i)) {
                throw new InvalidJSONException(
                        "Expected literal '" + literal + "'. [position " + this.position + "]",
                        this.metadata
                );
            }
        }
    }

    private int parseHexQuad() {
        if (this.position + 4 > this.input.length()) {
            throw new InvalidJSONException(
                    "Incomplete unicode escape sequence in string. [position " + this.position + "]",
                    this.metadata
            );
        }

        int value = 0;
        for (int i = 0; i < 4; i++) {
            char c = advance();
            int digit = Character.digit(c, 16);
            if (digit < 0) {
                throw new InvalidJSONException(
                        "Invalid hexadecimal digit '"
                            + printable(c)
                            + "' in unicode escape. [position "
                            + this.position
                            + "]",
                        this.metadata
                );
            }
            value = (value << 4) | digit;
        }
        return value;
    }

    private boolean canReadLowSurrogateEscape() {
        if (this.position + 5 >= this.input.length()) {
            return false;
        }
        return this.input.charAt(this.position) == '\\' && this.input.charAt(this.position + 1) == 'u';
    }

    /**
     * Skips whitespace. In liberal mode, this implementation also skips line and
     * block comments.
     */
    private void skipIgnorable() {
        while (!isEnd()) {
            char c = peek();

            if (Character.isWhitespace(c)) {
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
                                throw new InvalidJSONException(
                                        "Unterminated block comment. [position " + this.position + "]",
                                        this.metadata
                                );
                            }
                            if (
                                peek() == '*'
                                    && this.position + 1 < this.input.length()
                                    && this.input.charAt(this.position + 1) == '/'
                            ) {
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
            throw new InvalidJSONException(
                    "Expected '"
                        + expected
                        + "', but found "
                        + (isEnd() ? "end of input" : "'" + printable(peek()) + "'")
                        + ". [position "
                        + this.position
                        + "]",
                    this.metadata
            );
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
        return XMLUtils.isValidCodePoint(codepoint, this.xmlVersion);
    }

    private String printable(char c) {
        if (c < 0x20 || c == 0x7F) {
            return "\\u" + hex4(c);
        }
        return String.valueOf(c);
    }

    /**
     * Result of parsing a JSON string.
     *
     * xdmValue is the string value stored in the resulting XDM item.
     *
     * keyComparisonValue is the string used to detect duplicate object keys.
     * It is equal to the escaped form when escape=true, and to the decoded form
     * when escape=false.
     *
     * The two values can differ when escape=false and a decoded character is not
     * valid in the configured XML version. In that case, xdmValue contains the
     * fallback result, while keyComparisonValue keeps the decoded character for
     * duplicate-key comparison.
     */
    private static final class ParsedString {
        private final String xdmValue;
        private final String keyComparisonValue;

        private ParsedString(String xdmValue, String keyComparisonValue) {
            this.xdmValue = xdmValue;
            this.keyComparisonValue = keyComparisonValue;
        }
    }
}
