package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import javax.naming.OperationNotSupportedException;
import java.util.*;

public class ObjectAccumulateFunctionIterator extends ObjectFunctionIterator {

    private ObjectItem result;

    public ObjectAccumulateFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.ACCUMULATE, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        RuntimeIterator sequenceIterator = this._children.get(0);
        List<Item> items = getItemsFromIteratorWithCurrentContext(sequenceIterator);
        LinkedHashMap<String, List<Item>> keyValuePairs = new LinkedHashMap<>();
        for (Item item : items) {
            // ignore non-object items
            if (item.isObject()) {
                try {
                    for (String key : item.getKeys()) {
                        Item value = item.getItemByKey(key);
                        if (!keyValuePairs.containsKey(key)) {
                            List<Item> valueList = new ArrayList<>();
                            valueList.add(value);
                            keyValuePairs.put(key, valueList);
                        }
                        // store values for key collisions in a list
                        else {
                            keyValuePairs.get(key).add(value);
                        }
                    }
                } catch (OperationNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }

        this.result = new ObjectItem(keyValuePairs, ItemMetadata.fromIteratorMetadata(getMetadata()));
        this._hasNext = true;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " ACCUMULATE function",
                getMetadata());
    }
}
