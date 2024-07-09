package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;

import java.util.PrimitiveIterator;

public class GeneratedRandomDoublesIterator extends GeneratedRandomsIterator {
    private final PrimitiveIterator.OfDouble iterator;

    public GeneratedRandomDoublesIterator(PrimitiveIterator.OfDouble iterator) {
        this.iterator = iterator;
    }

    @Override
    public Item getNextRandom() {
        return ItemFactory.getInstance().createDoubleItem(iterator.next());
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }
}
