package org.rumbledb.bindings;

import java.util.Objects;

import lombok.Value;

@Value
public final class StandardInputBinding implements Binding {
    InputFormat format;

    public StandardInputBinding(String format) {
        this(InputFormat.fromString(format));
    }

    public StandardInputBinding(InputFormat format) {
        this.format = Objects.requireNonNullElse(format, InputFormat.JSON);
    }
}
