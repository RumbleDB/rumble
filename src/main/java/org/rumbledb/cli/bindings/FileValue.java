package org.rumbledb.cli.bindings;

import lombok.Value;
import java.net.URI;
import java.util.Objects;

@Value
public class FileValue implements VariableSource {
    URI location;

    public FileValue(URI location) {
        this.location = Objects.requireNonNull(location, "location");
    }

    @Override
    public boolean isFileValue() {
        return true;
    };

    @Override
    public URI getFileLocation() {
        return this.location;
    }
}
