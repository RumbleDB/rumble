package org.rumbledb.bindings;

import java.util.Objects;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public final class FileBinding implements Binding {
    private static final long serialVersionUID = 1L;

    String location;
    InputFormat format;

    public FileBinding(String location) {
        this(location, InputFormat.JSON);
    }

    public FileBinding(String location, InputFormat format) {
        this.location = Objects.requireNonNull(location, "location");
        this.format = Objects.requireNonNullElse(format, InputFormat.JSON);
    }
}
