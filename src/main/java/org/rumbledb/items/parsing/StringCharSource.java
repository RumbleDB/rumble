package org.rumbledb.items.parsing;

/**
 * A {@link JSONCharSource} backed by an in-memory {@code String}. Used by
 * {@code fn:parse-json}, which always already holds the JSON text as a materialized string.
 */
final class StringCharSource implements JSONCharSource {

    private final String input;
    private int index;

    StringCharSource(String input) {
        this(input, true);
    }

    StringCharSource(String input, boolean stripLeadingBom) {
        if (stripLeadingBom && input != null && !input.isEmpty() && input.charAt(0) == '\uFEFF') {
            this.input = input.substring(1);
        } else {
            this.input = input;
        }
        this.index = 0;
    }

    @Override
    public boolean isEnd() {
        return this.index >= this.input.length();
    }

    @Override
    public char peek() {
        return this.input.charAt(this.index);
    }

    @Override
    public char peek(int offset) {
        return this.input.charAt(this.index + offset);
    }

    @Override
    public boolean hasCharsAhead(int count) {
        return this.index + count <= this.input.length();
    }

    @Override
    public char advance() {
        return this.input.charAt(this.index++);
    }

    @Override
    public int position() {
        return this.index;
    }
}
