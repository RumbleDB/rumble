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
public final class LexicalBinding implements Binding {
    private static final long serialVersionUID = 1L;

    @NonFinal
    String value;

    public LexicalBinding(String value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = input.readString();
    }
}
