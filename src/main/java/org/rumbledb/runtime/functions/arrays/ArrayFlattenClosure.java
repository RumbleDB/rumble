package org.rumbledb.runtime.functions.arrays;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayFlattenClosure implements FlatMapFunction<Item, Item> {

    private static final long serialVersionUID = 1L;

    public ArrayFlattenClosure() {
    }

    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<Item>();

        if (!arg0.isArray()) {
            results.add(arg0);
            return results.iterator();
        }

        for (Item item : arg0.getItems()) {
            Iterator<Item> innerResult = this.call(item);
            while (innerResult.hasNext()) {
                results.add(innerResult.next());
            }
        }
        return results.iterator();
    }
};
