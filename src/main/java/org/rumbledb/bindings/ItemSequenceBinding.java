package org.rumbledb.bindings;

import lombok.Value;

import java.util.List;

import org.rumbledb.api.Item;

import java.util.Objects;


@Value
public class ItemSequenceBinding implements Binding {
    List<Item> items;

    public ItemSequenceBinding(List<Item> items) {
        this.items = List.copyOf(Objects.requireNonNull(items, "items"));
    }

    @Override
    public boolean isItemSequenceBinding() {
        return true;
    }
}
