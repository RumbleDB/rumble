package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;

import java.util.PrimitiveIterator;

public class GeneratedRandomDoublesIterator extends GeneratedRandomsIterator {
    private final PrimitiveIterator.OfDouble iterator;

    public GeneratedRandomDoublesIterator(int size, double low, double high) {
        super();
        this.iterator = this.random.doubles(size, low, high).iterator();
    }

    public GeneratedRandomDoublesIterator(int size, double low, double high, int seed) {
        super(seed);
        this.iterator = this.random.doubles(size, low, high).iterator();
    }

    public GeneratedRandomDoublesIterator(int size) {
        super();
        this.iterator = this.random.doubles(size).iterator();
    }

    public GeneratedRandomDoublesIterator(int size, int seed) {
        super(seed);
        this.iterator = this.random.doubles(size).iterator();
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
