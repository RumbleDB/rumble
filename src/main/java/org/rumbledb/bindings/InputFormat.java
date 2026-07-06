package org.rumbledb.bindings;

public enum InputFormat {
    JSON,
    TEXT;

    public static InputFormat fromString(String format) {
        if (format == null) {
            return JSON;
        }
        for (InputFormat inputFormat : values()) {
            if (inputFormat.name().equalsIgnoreCase(format)) {
                return inputFormat;
            }
        }
        throw new IllegalArgumentException("Invalid input format: " + format);
    }
}
