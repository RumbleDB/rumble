package org.rumbledb.items.parsing;

/**
 * A character source for {@link JSONParser}, abstracting over an in-memory
 * {@code String} ({@link StringCharSource}) or a streaming {@code Reader}
 * ({@link ReaderCharSource}).
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
     * Number of characters consumed via {@link #advance()} so far.
     */
    int position();
}
