package org.rumbledb.items;

import org.rumbledb.api.Item;

/**
 * Base class that gives every atomic item one Java equality and hashing contract.
 */
public abstract class AbstractAtomicItem implements Item {

    @Override
    public final boolean equals(Object other) {
        return other instanceof Item item && AtomicItemEquivalence.equivalent(this, item);
    }

    @Override
    public final int hashCode() {
        return AtomicItemEquivalence.hash(this);
    }
}
