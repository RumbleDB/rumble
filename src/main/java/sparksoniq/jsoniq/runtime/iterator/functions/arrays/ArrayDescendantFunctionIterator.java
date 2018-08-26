package sparksoniq.jsoniq.runtime.iterator.functions.arrays;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class ArrayDescendantFunctionIterator extends ArrayFunctionIterator {
    public ArrayDescendantFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ArrayFunctionOperators.DESCENDANT, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            return getResult();
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " DESCENDANT-ARRAYS function",
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
        getDescendantArrays(items);
        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    public void getDescendantArrays(List<Item> items) {
        for (Item item : items) {
            if (item.isArray()) {
                results.add(item);
                try {
                    getDescendantArrays(item.getItems());
                } catch (OperationNotSupportedException e) {
                    e.printStackTrace();
                }
            } else if (item.isObject()) {
                try {
                    getDescendantArrays((List<Item>) item.getValues());
                } catch (OperationNotSupportedException e) {
                    e.printStackTrace();
                }
            } else {
                // do nothing
            }
        }
    }
}
