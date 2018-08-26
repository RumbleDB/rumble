package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

public class ObjectValuesFunctionIterator extends ObjectFunctionIterator {
    public ObjectValuesFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.VALUES, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            return getResult();
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " VALUES function",
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
        RuntimeIterator objectIterator = this._children.get(0);
        ObjectItem object = getSingleItemOfTypeFromIterator(objectIterator, ObjectItem.class);
        for (Item item : object.getValues())
            results.add(item);

        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }
}
