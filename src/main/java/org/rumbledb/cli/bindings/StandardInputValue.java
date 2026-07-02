package org.rumbledb.cli.bindings;

import java.util.Objects;

public class StandardInputValue implements VariableSource {
    public enum InputFormat {
        JSON("json"),
        TEXT("text");

        private final String format;

        InputFormat(String format) {
            this.format = format;
        }

        public static InputFormat fromString(String format) {
            for (InputFormat inputFormat : InputFormat.values()) {
                if (inputFormat.format.equalsIgnoreCase(format)) {
                    return inputFormat;
                }
            }
            throw new IllegalArgumentException("Invalid input format: " + format);
        }
    }

    public final InputFormat format;

    public StandardInputValue(String format) {
        this.format = Objects.requireNonNullElse(InputFormat.fromString(format), InputFormat.JSON);
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
