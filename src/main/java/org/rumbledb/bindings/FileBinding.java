package org.rumbledb.bindings;

import lombok.Value;
import java.util.Objects;

@Value
public final class FileBinding implements Binding {
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
