package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommaExpressionClosure implements FlatMapFunction<Item, Item> {

    private static final long serialVersionUID = 1L;

    public CommaExpressionClosure() {
    }

    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<Item>();
        results.add(arg0);
        return results.iterator();
    }
};
