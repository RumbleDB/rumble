package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class ObjectIntersectFunctionIterator extends ObjectFunctionIterator {
    public ObjectIntersectFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.INTERSECT, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            RuntimeIterator sequenceIterator = this._children.get(0);
            List<Item> items = getItemsFromIteratorWithCurrentContext(sequenceIterator);
            LinkedHashMap<String, List<Item>> keyValuePairs = new LinkedHashMap<>();
            boolean firstItem = true;
            for (Item item : items) {
                // ignore non-object items
                if (item.isObject()) {
                    try {
                        if (firstItem) {
                            // add all key-value pairs of the first item
                            for (String key:item.getKeys()) {
                                Item value = item.getItemByKey(key);
                                ArrayList<Item> valueList = new ArrayList<>();
                                valueList.add(value);
                                keyValuePairs.put(key, valueList);
                            }
                            firstItem = false;
                        }
                        else {
                            // iterate over existing keys in the map of results
                            Iterator<String> keyIterator= keyValuePairs.keySet().iterator();
                            while (keyIterator.hasNext()) {
                                String key = keyIterator.next();
                                // if the new item doesn't contain the same keys
                                if (!item.getKeys().contains(key)){
                                    // remove the key from the map
                                    keyIterator.remove();
                                }
                                else {
                                    // add the matching key's value to the list
                                    Item value = item.getItemByKey(key);
                                    keyValuePairs.get(key).add(value);
                                }
                            }
                        }
                    } catch (OperationNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }

            ObjectItem result = new ObjectItem(keyValuePairs, ItemMetadata.fromIteratorMetadata(getMetadata()));

            this._hasNext = false;
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " INTERSECT function",
                getMetadata());
    }
}
