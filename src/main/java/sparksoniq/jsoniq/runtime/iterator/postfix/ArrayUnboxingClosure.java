package sparksoniq.jsoniq.runtime.iterator.postfix;

import org.apache.spark.api.java.function.FlatMapFunction;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.Item;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ArrayUnboxingClosure implements FlatMapFunction<Item, Item> {

    public ArrayUnboxingClosure() {
   }

    public Iterator<Item> call(Item arg0) throws Exception {
        if (!(arg0 instanceof ArrayItem))
           return Collections.emptyIterator();
       List<Item> results = arg0.getItems();
       return results.iterator();
   }
};