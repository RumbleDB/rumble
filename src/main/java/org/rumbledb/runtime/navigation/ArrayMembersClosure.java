package org.rumbledb.runtime.navigation;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayMembersClosure implements FlatMapFunction<Item, Item> {

    private static final long serialVersionUID = 1L;

    public ArrayMembersClosure() {
    }

    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<Item>();

        if (!(arg0.isArray())) {
            return results.iterator();
        }

        if (arg0.isJSONArray()) {
            for (Item item : arg0.getItems()) {
                if (item != null) {
                    results.add(item);
                }
            }
        } else {
            for (java.util.List<Item> member : arg0.getSequenceMembers()) {
                results.addAll(member);
            }
        }

        return results.iterator();
    }
};
