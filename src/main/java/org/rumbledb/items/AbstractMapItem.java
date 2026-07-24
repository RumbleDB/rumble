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
        if (!(other instanceof Item otherItem) || !otherItem.isMap() || getSize() != otherItem.getSize()) {
            return false;
        }
        for (Item key : getItemKeys()) {
            List<Item> value = getSequenceByKey(key);
            List<Item> otherValue = otherItem.getSequenceByKey(key);
            if (otherValue == null || !value.equals(otherValue)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final int hashCode() {
        int result = getSize();
        for (Item key : getItemKeys()) {
            result += 31 * key.hashCode() + getSequenceByKey(key).hashCode();
        }
        return result;
    }
}
