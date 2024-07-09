package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;

public abstract class GeneratedRandomsIterator {

    public abstract Item getNextRandom();

    public abstract boolean hasNext();
}
