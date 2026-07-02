package org.rumbledb.bindings;

import java.util.Objects;

public class StandardInputBinding implements Binding {
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

    public StandardInputBinding(String format) {
        this.format = Objects.requireNonNullElse(InputFormat.fromString(format), InputFormat.JSON);
    }

    public StandardInputBinding(InputFormat format) {
        this.format = format;
    }

    @Override
    public boolean isStandardInputBinding() {
        return true;
    }

    @Override
    public InputFormat getStandardInputFormat() {
        return this.format;
    }
}
