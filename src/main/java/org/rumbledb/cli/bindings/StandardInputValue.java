package org.rumbledb.cli.bindings;

public class StandardInputValue implements VariableSource {
    public enum InputFormat {
        JSON,
        TEXT
    }

    public final InputFormat format;

    public StandardInputValue() {
        // Default to JSON if no format is specified
        this(InputFormat.JSON);
    }

    public StandardInputValue(InputFormat format) {
        this.format = format;
    }

    @Override
    public boolean isStandardInput() {
        return true;
    }

    @Override
    public InputFormat getStandardInputFormat() {
        return this.format;
    }
}
