package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class ObjectProjectFunctionIterator extends ObjectFunctionIterator {
    public ObjectProjectFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.PROJECT, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            if (results == null) {
                _currentIndex = 0;
                results = new ArrayList<>();
                RuntimeIterator sequenceIterator = this._children.get(0);
                RuntimeIterator keysIterator = this._children.get(1);
                List<Item> items = getItemsFromIteratorWithCurrentContext(sequenceIterator);
                List<Item> keys = getItemsFromIteratorWithCurrentContext(keysIterator);
                getProjection(items, keys);
            }
            if (_currentIndex == results.size() - 1)
                this._hasNext = false;
            return results.get(_currentIndex++);
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " KEYS function",
                getMetadata());
    }

    public void getProjection(List<Item> items, List<Item> keys) {
        for (Item item:items) {
            if (item.isObject()) {
                ArrayList<String> keylist = new ArrayList<>();
                ArrayList<Item> valueList = new ArrayList<>();
                for (Item keyItem:keys) {
                    try {
                        String key = keyItem.getStringValue();
                        Item value = item.getItemByKey(key);
                        if (value != null) {
                            keylist.add(key);
                            valueList.add(value);
                        }
                    } catch (OperationNotSupportedException e) {
                        throw new UnexpectedTypeException("Project function has non-string key args.", getMetadata());
                    }
                }
                results.add(new ObjectItem(keylist, valueList, ItemMetadata.fromIteratorMetadata(getMetadata())));
            }
            else {
                results.add(item);
            }
        }

    }
}
