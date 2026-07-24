package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;

import java.io.Serial;
import java.util.Iterator;

public class AtomizationClosure implements FlatMapFunction<Item, Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    public AtomizationClosure() {
    }

    @Override
    public Iterator<Item> call(Item arg0) throws Exception {
        return arg0.atomizedValue().iterator();
    }
};
