package sparksoniq.jsoniq.runtime.iterator.functions.sequences;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

public class RemoveFunctionIterator extends LocalFunctionCallIterator {

    private List<Item> results;
    private int _currentIndex = 0;

    public RemoveFunctionIterator(List<RuntimeIterator> parameters, IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            return getResult();
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "remove function", getMetadata());
    }

    public Item getResult() {
        if (results == null || results.size() == 0) {
            throw new IteratorFlowException("getResult called on an empty list of results", getMetadata());
        }
        if (_currentIndex == results.size() - 1)
            _hasNext = false;
        return results.get(_currentIndex++);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.results = new ArrayList<>();
        this._currentIndex = 0;

        RuntimeIterator sequenceIterator = this._children.get(0);
        RuntimeIterator positionIterator = this._children.get(1);

        positionIterator.open(context);
        if (!positionIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. remove can't be performed with empty sequence as the position",
                    getMetadata());
        }
        Item positionItem = positionIterator.next();
        if (positionItem instanceof ArrayItem) {
            throw new NonAtomicKeyException(
                    "Invalid args. remove can't be performed with an array parameter as the position",
                    getMetadata().getExpressionMetadata());
        } else if (positionItem instanceof ObjectItem) {
            throw new NonAtomicKeyException(
                    "Invalid args. remove can't be performed with an object parameter as the position",
                    getMetadata().getExpressionMetadata());
        } else if (!(positionItem instanceof IntegerItem)) {
            throw new UnexpectedTypeException(
                    "Invalid args. Position parameter should be an integer",
                    getMetadata());
        }
        positionIterator.close();

        List<Item> items = getItemsFromIteratorWithCurrentContext(sequenceIterator);
        int position = ((IntegerItem)positionItem).getIntegerValue();
        // in JSONiq indices start from 1

        for (int i = 1; i <= items.size(); i++) {
            if (i != position) {
                results.add(items.get(i-1));
            }
        }

        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }
}
