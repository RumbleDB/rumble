package org.rumbledb.runtime.functions.object;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectProjectClosure implements FlatMapFunction<Item, Item> {

    private static final long serialVersionUID = 1L;
    private List<String> projectionKeys;

    public ObjectProjectClosure(List<Item> projectionKeys) {
        this.projectionKeys = projectionKeys.stream().map(x -> x.getStringValue()).collect(Collectors.toList());
    }

    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<>();

        if (!arg0.isObject()) {
            results.add(arg0);
            return results.iterator();
        }

        LinkedHashMap<String, Item> content = new LinkedHashMap<>();
        for (String key : arg0.getKeys()) {
            if (!this.projectionKeys.contains(key)) {
                content.put(key, arg0.getItemByKey(key));
            }
        }

        results.add(ItemFactory.getInstance().createObjectItem(content));
        return results.iterator();
    }
};
