package org.rumbledb.items;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;

/**
 * Order-independent structural Java equality for every materialized and lazy map representation.
 */
public abstract class AbstractMapItem implements Item {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Item otherItem) || !otherItem.isMap() || this.getSize() != otherItem.getSize()) {
            return false;
        }
        for (Item key : this.getItemKeys()) {
            List<Item> value = this.getSequenceByKey(key);
            List<Item> otherValue = otherItem.getSequenceByKey(key);
            if (!value.equals(otherValue)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final int hashCode() {
        int result = this.getSize();
        for (Item key : this.getItemKeys()) {
            result += 31 * key.hashCode() + this.getSequenceByKey(key).hashCode();
        }
        return result;
    }
}
