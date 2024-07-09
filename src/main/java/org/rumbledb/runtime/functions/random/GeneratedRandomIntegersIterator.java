package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;

import java.util.PrimitiveIterator;

public class GeneratedRandomIntegersIterator extends GeneratedRandomsIterator {
    private final PrimitiveIterator.OfInt iterator;

    public GeneratedRandomIntegersIterator(PrimitiveIterator.OfInt iterator) {
        this.iterator = iterator;
    }

    @Override
    public Item getNextRandom() {
        return ItemFactory.getInstance().createIntItem(iterator.next());
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }
}
