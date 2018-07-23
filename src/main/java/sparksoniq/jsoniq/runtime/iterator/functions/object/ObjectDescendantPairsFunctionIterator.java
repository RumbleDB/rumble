package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectDescendantPairsFunctionIterator extends ObjectFunctionIterator {
    public ObjectDescendantPairsFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.DESCENDANTPAIRS, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            if (results == null) {
                _currentIndex = 0;
                results = new ArrayList<>();
                RuntimeIterator sequenceIterator = this._children.get(0);
                List<Item> items = getItemsFromIteratorWithCurrentContext(sequenceIterator);
                getDescendantPairs(items);
            }
            if (_currentIndex == results.size() - 1)
                this._hasNext = false;
            return results.get(_currentIndex++);
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " DESCENDANT-PAIRS function",
                getMetadata());
    }

    public void getDescendantPairs(List<Item> items) {
        for (Item item:items) {
            if (item.isArray()) {
                try {
                    getDescendantPairs(item.getItems());
                } catch (OperationNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            else if (item.isObject()) {
                try {
                    List<String> keys = item.getKeys();
                    for (String key:keys) {
                        Item value = item.getItemByKey(key);

                        List<String> keyList = Collections.singletonList(key);
                        List<Item> valueList = Collections.singletonList(value);

                        ObjectItem result = new ObjectItem(keyList, valueList, ItemMetadata.fromIteratorMetadata(getMetadata()));
                        results.add(result);
                        getDescendantPairs(valueList);
                    }
                } catch (OperationNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            else {
                // do nothing
            }
        }
    }
}
