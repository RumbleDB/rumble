package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;

import java.util.PrimitiveIterator;

public class GeneratedRandomIntegersIterator extends GeneratedRandomsIterator {
    private final PrimitiveIterator.OfInt iterator;

    public GeneratedRandomIntegersIterator(int size, int low, int high) {
        super();
        this.iterator = this.random.ints(size, low, high).iterator();
    }

    public GeneratedRandomIntegersIterator(int size, int low, int high, int seed) {
        super(seed);
        this.iterator = this.random.ints(size, low, high).iterator();
    }

    @Override
    public Item getNextRandom() {
        return ItemFactory.getInstance().createIntItem(this.iterator.next());
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }
}
