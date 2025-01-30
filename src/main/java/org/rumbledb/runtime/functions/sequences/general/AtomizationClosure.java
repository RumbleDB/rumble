package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;

import java.util.Iterator;

public class AtomizationClosure implements FlatMapFunction<Item, Item> {

    private static final long serialVersionUID = 1L;

    public AtomizationClosure() {
    }

    public Iterator<Item> call(Item arg0) throws Exception {
        if (arg0.isArray()) {
            return arg0.getItems().iterator();
        } else {
            return arg0.typedValue().iterator();
        }

    }
};
