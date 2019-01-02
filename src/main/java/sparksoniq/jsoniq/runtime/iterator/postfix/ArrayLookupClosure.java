package sparksoniq.jsoniq.runtime.iterator.postfix;

import org.apache.spark.api.java.function.FlatMapFunction;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayLookupClosure implements FlatMapFunction<Item, Item> {
    private final Integer _lookup;

    public ArrayLookupClosure(Integer lookup) {
        _lookup = lookup;
    }

    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<Item>();

        if (!(arg0 instanceof ArrayItem))
            return results.iterator();

        if (_lookup <= 0 || _lookup > arg0.getSize())
            return results.iterator();

        Item item = arg0.getItemAt(_lookup - 1);
        if(item != null)
        {
            results.add(item);
        }
        return results.iterator();
    }
};