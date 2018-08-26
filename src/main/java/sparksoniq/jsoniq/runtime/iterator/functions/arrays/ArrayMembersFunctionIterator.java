package sparksoniq.jsoniq.runtime.iterator.functions.arrays;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class ArrayMembersFunctionIterator extends ArrayFunctionIterator {
    public ArrayMembersFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ArrayFunctionOperators.MEMBERS, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            return getResult();
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + "MEMBERS function",
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
        for (Item item : items) {
            if (item.isArray()) {
                try {
                    int size = item.getSize();
                    for (int i = 0; i < size; i++) {
                        results.add(item.getItemAt(i));
                    }
                } catch (OperationNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }
}
