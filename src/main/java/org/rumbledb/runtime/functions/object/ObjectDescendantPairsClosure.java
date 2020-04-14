package org.rumbledb.runtime.functions.object;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.ItemFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ObjectDescendantPairsClosure implements FlatMapFunction<Item, Item> {

    private static final long serialVersionUID = 1L;
    private ExceptionMetadata metadata;

    public ObjectDescendantPairsClosure(ExceptionMetadata metadata) {
        this.metadata = metadata;
    }

    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<Item>();

        if (arg0.isArray()) {
            for (Item item : arg0.getItems()) {
                Iterator<Item> innerResult = this.call(item);
                while (innerResult.hasNext()) {
                    results.add(innerResult.next());
                }
            }
        } else if (arg0.isObject()) {
            List<String> keys = arg0.getKeys();
            for (String key : keys) {
                Item value = arg0.getItemByKey(key);

                List<String> keyList = Collections.singletonList(key);
                List<Item> valueList = Collections.singletonList(value);

                Item result = ItemFactory.getInstance()
                    .createObjectItem(keyList, valueList, this.metadata);
                results.add(result);

                Iterator<Item> innerResult = this.call(value);
                while (innerResult.hasNext()) {
                    results.add(innerResult.next());
                }
            }
        } else {
            // for atomic types: do nothing
            return results.iterator();
        }

        return results.iterator();
    }
};
