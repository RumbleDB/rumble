package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.Function2;
import org.rumbledb.api.Item;

import java.util.Iterator;

public class RemoveFirstItemClosure implements Function2<Integer, Iterator<Item>, Iterator<Item>> {

    private static final long serialVersionUID = 1L;

    public RemoveFirstItemClosure() {

    }

    @Override
    public Iterator<Item> call(Integer index, Iterator<Item> iterator) throws Exception {
        if (index == 0) {
            iterator.next();
        }
        return iterator;
    }
};
