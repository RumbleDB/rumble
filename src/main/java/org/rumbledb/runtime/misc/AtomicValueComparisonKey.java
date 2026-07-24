package org.rumbledb.runtime.misc;

import java.io.Serializable;

import org.rumbledb.api.Item;

/**
 * Serializable adapter for using atomic value comparison in distributed hash operations.
 */
public final class AtomicValueComparisonKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private Item item;

    public AtomicValueComparisonKey() {
    }

    public AtomicValueComparisonKey(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return this.item;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof AtomicValueComparisonKey otherKey
            && AtomicValueComparison.equal(this.item, otherKey.item);
    }

    @Override
    public int hashCode() {
        return AtomicValueComparison.hash(this.item);
    }
}
