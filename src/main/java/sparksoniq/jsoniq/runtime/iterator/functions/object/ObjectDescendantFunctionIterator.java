package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class ObjectDescendantFunctionIterator extends ObjectFunctionIterator {
    public ObjectDescendantFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.DESCENDANT, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            if (results == null) {
                _currentIndex = 0;
                results = new ArrayList<>();
                RuntimeIterator sequenceIterator = this._children.get(0);
                List<Item> items = getItemsFromIteratorWithCurrentContext(sequenceIterator);
                getDescendantObjects(items);
            }
            if (_currentIndex == results.size() - 1)
                this._hasNext = false;
            return results.get(_currentIndex++);
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " KEYS function",
                getMetadata());
    }

    public void getDescendantObjects(List<Item> items) {
        for (Item item:items) {
            if (item.isArray()) {
                try {
                    getDescendantObjects(item.getItems());
                } catch (OperationNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            else if (item.isObject()) {
                results.add(item);
                try {
                    getDescendantObjects((List<Item>) item.getValues());
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
