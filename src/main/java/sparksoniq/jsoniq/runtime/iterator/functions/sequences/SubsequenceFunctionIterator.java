package sparksoniq.jsoniq.runtime.iterator.functions.sequences;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.*;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

public class SubsequenceFunctionIterator extends LocalFunctionCallIterator {

    private List<Item> results;
    private int _currentIndex = 0;

    public SubsequenceFunctionIterator(List<RuntimeIterator> parameters, IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            return getResult();
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "insert-before function", getMetadata());
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
        RuntimeIterator lengthIterator = null;
        Item lengthItem = null;
        if (this._children.size() == 3) {
             lengthIterator = this._children.get(2);

            lengthIterator.open(context);
            if (!lengthIterator.hasNext()) {
                throw new UnexpectedTypeException(
                        "Invalid args. subsequence can't be performed with empty sequence as the length",
                        getMetadata());
            }
            lengthItem = lengthIterator.next();
            if (lengthItem instanceof ArrayItem) {
                throw new NonAtomicKeyException(
                        "Invalid args. subsequence can't be performed with an array parameter as the length",
                        getMetadata().getExpressionMetadata());
            } else if (lengthItem instanceof ObjectItem) {
                throw new NonAtomicKeyException(
                        "Invalid args. subsequence can't be performed with an object parameter as the length",
                        getMetadata().getExpressionMetadata());
            } else if (!(Item.isNumeric(lengthItem))) {
                throw new UnexpectedTypeException(
                        "Invalid args. Length parameter should be an numeric(Integer/Decimal/Double)",
                        getMetadata());
            }
            lengthIterator.close();
        }


        positionIterator.open(context);
        if (!positionIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. subsequence can't be performed with empty sequence as the position",
                    getMetadata());
        }
        Item positionItem = positionIterator.next();
        if (positionItem instanceof ArrayItem) {
            throw new NonAtomicKeyException(
                    "Invalid args. subsequence can't be performed with an array parameter as the position",
                    getMetadata().getExpressionMetadata());
        } else if (positionItem instanceof ObjectItem) {
            throw new NonAtomicKeyException(
                    "Invalid args. subsequence can't be performed with an object parameter as the position",
                    getMetadata().getExpressionMetadata());
        } else if (!(Item.isNumeric(positionItem))) {
            throw new UnexpectedTypeException(
                    "Invalid args. Position parameter should be a numeric(Integer/Decimal/Double)",
                    getMetadata());
        }
        positionIterator.close();

        List<Item> items = getItemsFromIteratorWithCurrentContext(sequenceIterator);
        // round the double values to nearest integers for position and length
        int position = (int) Math.round((Item.getNumericValue(positionItem, Double.class)));

        // if starting position doesn't over shoot the sequence size (otherwise return empty sequence)
        if (position <= items.size()) {
            // in JSONiq indices start from 1, if position is 0 or negative, start from the beginning
            if (position < 1) {
                position = 1;
            }
            int length = items.size() - position + 1;

            // if length is given, use that value instead
            if (lengthItem != null) {
                length = (int) Math.round((Item.getNumericValue(lengthItem, Double.class)));
            }

            if (length > 0) {
                for (int i = position; i < position + length && i <= items.size(); i++) {
                    results.add(items.get(i-1));
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
