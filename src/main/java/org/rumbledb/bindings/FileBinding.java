package org.rumbledb.bindings;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.Objects;

@Value
@NoArgsConstructor
public final class FileBinding implements Binding {
    @NonFinal
    String location;
    @NonFinal
    InputFormat format;

    public FileBinding(String location) {
        this(location, InputFormat.JSON);
    }

    public FileBinding(String location, InputFormat format) {
        this.location = Objects.requireNonNull(location, "location");
        this.format = Objects.requireNonNullElse(format, InputFormat.JSON);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.location);
        output.writeString(this.format.name());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.location = input.readString();
        this.format = InputFormat.valueOf(input.readString());
    }
}
