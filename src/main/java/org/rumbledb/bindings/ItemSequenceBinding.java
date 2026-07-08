package org.rumbledb.bindings;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ItemSequenceBinding implements Binding {
    private static final long serialVersionUID = 1L;

    private List<Item> items;

    public ItemSequenceBinding() {
        this.items = new ArrayList<>();
    }

    public ItemSequenceBinding(List<Item> items) {
        this.items = new ArrayList<>(Objects.requireNonNull(items, "items"));
    }

    public List<Item> getItems() {
        return this.items;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeClassAndObject(output, this.items);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.items = (List<Item>) kryo.readClassAndObject(input);
    }
}
