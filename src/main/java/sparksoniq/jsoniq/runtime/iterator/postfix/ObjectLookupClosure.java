package sparksoniq.jsoniq.runtime.iterator.postfix;

import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;

public class ObjectLookupClosure implements FlatMapFunction<Item, Item> {
    private final String _key;

    public ObjectLookupClosure(String key) {
        _key = key;
    }

    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<Item>();

        if (!(arg0 instanceof ObjectItem))
            return results.iterator();

        Item item = arg0.getItemByKey(_key);
        if(item != null)
        {
            results.add(item);
        }
        return results.iterator();
    }
};
