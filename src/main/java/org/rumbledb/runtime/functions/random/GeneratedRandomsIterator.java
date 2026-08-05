package org.rumbledb.runtime.functions.random;

import java.util.Random;

import org.rumbledb.api.Item;

public abstract class GeneratedRandomsIterator {
    protected final Random random;

    protected GeneratedRandomsIterator() {
        this.random = new Random();
    }

    protected GeneratedRandomsIterator(int seed) {
        this.random = new Random();
        this.random.setSeed(seed);
    }

    public abstract Item getNextRandom();

    public abstract boolean hasNext();
}
