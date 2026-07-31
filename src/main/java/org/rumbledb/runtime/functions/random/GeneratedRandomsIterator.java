package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;

import java.util.Iterator;
import java.util.Random;

public abstract class GeneratedRandomsIterator implements Iterator<Item> {
    protected Random random;

    protected GeneratedRandomsIterator() {
        this.random = new Random();
    }

    protected GeneratedRandomsIterator(int seed) {
        this.random = new Random();
        this.random.setSeed(seed);
    }

    public abstract Item getNextRandom();

    @Override
    public final Item next() {
        return getNextRandom();
    }

    @Override
    public abstract boolean hasNext();
}
