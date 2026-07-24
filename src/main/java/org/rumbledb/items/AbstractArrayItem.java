package org.rumbledb.items;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;

/**
 * Structural Java equality for both JSON arrays and XDM sequence-member arrays.
 */
public abstract class AbstractArrayItem implements Item {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Item otherItem) || !otherItem.isArray() || getSize() != otherItem.getSize()) {
            return false;
        }
        for (int i = 0; i < getSize(); i++) {
            if (!getSequenceAt(i).equals(otherItem.getSequenceAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final int hashCode() {
        int result = 1;
        for (List<Item> member : getSequenceMembers()) {
            result = 31 * result + member.hashCode();
        }
        return result;
    }
}
