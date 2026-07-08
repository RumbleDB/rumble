package org.rumbledb.items.parsing;

/**
 * A character source for {@link JSONParser}, abstracting over an in-memory
 * {@code String} ({@link StringCharSource}) or a streaming {@code Reader}
 * ({@link ReaderCharSource}).
 *
 * <p>
 * The parser is LL(1) except when deciding whether a Unicode escape (backslash,
 * u, 4 hex digits) is the high surrogate of a UTF-16 pair: it must speculatively
 * check for a second such escape immediately following the first, without
 * committing to consuming it unless it turns out to be a valid low surrogate.
 * That check needs up to 6 characters of forward peek counted from the
 * position right after the first escape has been fully consumed: the
 * candidate second escape's {@code \}, {@code u}, and 4 hex digits
 * ({@code canReadLowSurrogateEscape} + {@code peekHexQuad} in
 * {@link JSONLiteralParsingUtils}). {@code MAX_ESCAPE_LOOKAHEAD} is that
 * exact bound.
 * </p>
 */
interface JSONCharSource {

    int MAX_ESCAPE_LOOKAHEAD = 6;

    /**
     * True if there are no more characters to consume.
     */
    boolean isEnd();

    /**
     * Returns the current character without consuming it. Caller must have
     * checked {@code !isEnd()}.
     */
    char peek();

    /**
     * Returns the character {@code offset} positions ahead of the current
     * one, without consuming anything. {@code offset} must be in
     * {@code [0, MAX_ESCAPE_LOOKAHEAD)}; caller must have checked
     * {@code hasCharsAhead(offset + 1)}.
     */
    char peek(int offset);

    /**
     * True if at least {@code count} more characters are available from the
     * current position onward.
     */
    boolean hasCharsAhead(int count);

    /**
     * Consumes and returns the current character. Caller must have checked
     * {@code !isEnd()}.
     */
    char advance();

    /**
     * Number of characters consumed via {@link #advance()} so far. A
     * stripped leading byte-order-mark character is not counted.
     */
    int position();
}
