package org.rumbledb.bindings;

import java.util.Objects;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public final class StandardInputBinding implements Binding {
    private static final long serialVersionUID = 1L;

    InputFormat format;

    public StandardInputBinding(String format) {
        this(InputFormat.fromString(format));
    }

    public StandardInputBinding(InputFormat format) {
        this.format = Objects.requireNonNullElse(format, InputFormat.JSON);
    }
}
