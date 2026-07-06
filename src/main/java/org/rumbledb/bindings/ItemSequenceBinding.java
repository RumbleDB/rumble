package org.rumbledb.bindings;

import org.rumbledb.api.Item;
import lombok.Value;
import java.util.List;
import java.util.Objects;

@Value
public final class ItemSequenceBinding implements Binding {
    List<Item> items;

    public ItemSequenceBinding(List<Item> items) {
        this.items = List.copyOf(Objects.requireNonNull(items, "items"));
    }
}
