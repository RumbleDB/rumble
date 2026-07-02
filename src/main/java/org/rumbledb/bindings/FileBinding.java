package org.rumbledb.bindings;

import lombok.Value;
import java.util.Objects;

@Value
public class FileBinding implements Binding {
    String location;

    public FileBinding(String location) {
        this.location = Objects.requireNonNull(location, "location");
    }

    @Override
    public boolean isFileBinding() {
        return true;
    }

    @Override
    public String getFileLocation() {
        return this.location;
    }
}
