package org.rumbledb.runtime.functions.arrays;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayDescendantClosure implements FlatMapFunction<Item, Item> {

    private static final long serialVersionUID = 1L;

    public ArrayDescendantClosure() {
    }

    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<Item>();
        List<Item> innerValues;

        if (arg0.isArray()) {
            results.add(arg0);
            if (arg0.isArrayOfItems()) {
                innerValues = arg0.getItemMembers();
            } else {
                innerValues = new ArrayList<Item>();
                for (java.util.List<Item> member : arg0.getSequenceMembers()) {
                    innerValues.addAll(member);
                }
            }
        } else if (arg0.isObject()) {
            innerValues = arg0.getValues();
        } else {
            return results.iterator();
        }

        for (Item item : innerValues) {
            Iterator<Item> innerResult = this.call(item);
            while (innerResult.hasNext()) {
                results.add(innerResult.next());
            }
        }
        return results.iterator();
    }
};
