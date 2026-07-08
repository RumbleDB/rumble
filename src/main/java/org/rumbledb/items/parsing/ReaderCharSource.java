package org.rumbledb.items.parsing;

import org.rumbledb.exceptions.OurBadException;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;

/**
 * A {@link JSONCharSource} backed by a streaming {@code Reader}, for
 * {@code fn:json-doc}. Maintains a small sliding lookahead buffer sized to
 * {@link JSONCharSource#MAX_ESCAPE_LOOKAHEAD} so the parser never needs to
 * hold the whole resource in memory.
 */
final class ReaderCharSource implements JSONCharSource {

    private final Reader reader;
    private final char[] buffer = new char[JSONCharSource.MAX_ESCAPE_LOOKAHEAD];
    private int bufferedCount;
    private boolean readerExhausted;
    private int position;

    ReaderCharSource(Reader reader) {
        this.reader = reader;
        this.bufferedCount = 0;
        this.readerExhausted = false;
        this.position = 0;

        // A decoded leading U+FEFF is a byte-order mark, not part of the
        // JSON text. Discard it here without counting it towards position(),
        // matching StringCharSource's substring(1) strip.
        ensureBuffered(1);
        if (this.bufferedCount > 0 && this.buffer[0] == '\uFEFF') {
            discardFirstBufferedChar();
        }
    }

    @Override
    public boolean isEnd() {
        ensureBuffered(1);
        return this.bufferedCount == 0;
    }

    @Override
    public char peek() {
        ensureBuffered(1);
        return this.buffer[0];
    }

    @Override
    public char peek(int offset) {
        ensureBuffered(offset + 1);
        return this.buffer[offset];
    }

    @Override
    public boolean hasCharsAhead(int count) {
        ensureBuffered(count);
        return this.bufferedCount >= count;
    }

    @Override
    public char advance() {
        ensureBuffered(1);
        char c = this.buffer[0];
        discardFirstBufferedChar();
        this.position++;
        return c;
    }

    @Override
    public int position() {
        return this.position;
    }

    private void discardFirstBufferedChar() {
        System.arraycopy(this.buffer, 1, this.buffer, 0, this.bufferedCount - 1);
        this.bufferedCount--;
    }

    private void ensureBuffered(int count) {
        if (count > this.buffer.length) {
            throw new OurBadException(
                    "Requested JSON parser lookahead ("
                        + count
                        + ") exceeds buffer capacity ("
                        + this.buffer.length
                        + ")."
            );
        }
        while (this.bufferedCount < count && !this.readerExhausted) {
            int n;
            try {
                n = this.reader.read(this.buffer, this.bufferedCount, this.buffer.length - this.bufferedCount);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            if (n == -1) {
                this.readerExhausted = true;
            } else {
                this.bufferedCount += n;
            }
        }
    }
}
