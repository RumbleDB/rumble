package org.rumbledb.bindings;

import java.util.Objects;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NoArgsConstructor
public final class StandardInputBinding implements Binding {
    @NonFinal
    InputFormat format;

    public StandardInputBinding(String format) {
        this(InputFormat.fromString(format));
    }

    public StandardInputBinding(InputFormat format) {
        this.format = Objects.requireNonNullElse(format, InputFormat.JSON);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.format.name());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.format = InputFormat.valueOf(input.readString());
    }
}
