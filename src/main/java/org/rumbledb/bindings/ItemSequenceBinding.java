package org.rumbledb.bindings;

import org.rumbledb.api.Item;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ItemSequenceBinding implements Binding {
    private List<Item> items;

    public ItemSequenceBinding() {
        this.items = new ArrayList<>();
    }

    public ItemSequenceBinding(List<Item> items) {
        this.items = new ArrayList<>(Objects.requireNonNull(items, "items"));
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }
}
