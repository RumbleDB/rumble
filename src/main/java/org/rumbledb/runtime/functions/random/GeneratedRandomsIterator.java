package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;

import java.util.Random;

public abstract class GeneratedRandomsIterator {
    protected Random random;

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
