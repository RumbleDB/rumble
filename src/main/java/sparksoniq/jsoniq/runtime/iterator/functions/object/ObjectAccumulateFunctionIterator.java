package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import javax.naming.OperationNotSupportedException;
import java.util.*;

public class ObjectAccumulateFunctionIterator extends ObjectFunctionIterator {
    public ObjectAccumulateFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.ACCUMULATE, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            ObjectItem result = null;
            RuntimeIterator sequenceIterator = this._children.get(0);
            List<Item> items = getItemsFromIteratorWithCurrentContext(sequenceIterator);
            Map<String, ArrayList<Item>> keyValuePairs = new LinkedHashMap<>();
            for (Item item : items) {
                // ignore non-object items
                if (item.isObject()) {
                    try {
                        for (String key:item.getKeys()) {
                            Item value = item.getItemByKey(key);
                            if (!keyValuePairs.containsKey(key)) {
                                ArrayList<Item> valueList = new ArrayList<>();
                                valueList.add(value);
                                keyValuePairs.put(key, valueList);
                            }
                            // store values for key collisions in a list
                            else {
                                keyValuePairs.get(key).add(value);
                            }
                        }
                        ArrayList<String> keyList = new ArrayList<>();
                        ArrayList<Item> valueList = new ArrayList<>();
                        // keySet on LinkedHashMap preserves order
                        for (String key:keyValuePairs.keySet()) {
                            // add all keys to the keyList
                            keyList.add(key);
                            ArrayList<Item> values = keyValuePairs.get(key);
                            // convert values of keys with collisions into arrayItems
                            if (values.size() > 1) {
                                ArrayItem valuesArray = new ArrayItem(values
                                        , ItemMetadata.fromIteratorMetadata(getMetadata()));
                                valueList.add(valuesArray);
                            }
                            else if (values.size() == 1) {
                                Item value = values.get(0);
                                valueList.add(value);
                            }
                            else {
                                throw new OperationNotSupportedException("Unexpected list size found");
                            }
                        }
                        result = new ObjectItem(keyList, valueList, ItemMetadata.fromIteratorMetadata(getMetadata()));
                    } catch (OperationNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
            this._hasNext = false;
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " ACCUMULATE function",
                getMetadata());
    }
}
