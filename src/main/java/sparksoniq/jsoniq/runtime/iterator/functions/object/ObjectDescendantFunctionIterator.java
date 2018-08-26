package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

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
            return getResult();
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " DESCENDANT-OBJECTS function",
                getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;
        _currentIndex = 0;
        results = new ArrayList<>();

        RuntimeIterator sequenceIterator = this._children.get(0);
        List<Item> items = getItemsFromIteratorWithCurrentContext(sequenceIterator);
        getDescendantObjects(items);

        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
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
