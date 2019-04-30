package sparksoniq.jsoniq.runtime.iterator.functions.sequences.general;

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

import java.util.List;

public class RemoveFunctionIterator extends LocalFunctionCallIterator {

    private RuntimeIterator _sequenceIterator;
    private Item _nextResult;
    private int _removePosition;            // position to remove the item
    private int _currentPosition;           // current position


    public RemoveFunctionIterator(List<RuntimeIterator> parameters, IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "remove function", getMetadata());
    }


    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _currentPosition = 1;

        RuntimeIterator positionIterator = this._children.get(1);
        positionIterator.open(context);
        if (!positionIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. remove can't be performed with empty sequence as the position",
                    getMetadata());
        }
        Item positionItem = positionIterator.next();
        if (positionItem.isArray()) {
            throw new NonAtomicKeyException(
                    "Invalid args. remove can't be performed with an array parameter as the position",
                    getMetadata().getExpressionMetadata());
        } else if (positionItem.isObject()) {
            throw new NonAtomicKeyException(
                    "Invalid args. remove can't be performed with an object parameter as the position",
                    getMetadata().getExpressionMetadata());
        } else if (!(positionItem instanceof IntegerItem)) {
            throw new UnexpectedTypeException(
                    "Invalid args. Position parameter should be an integer",
                    getMetadata());
        }
        _removePosition = ((IntegerItem) positionItem).getIntegerValue();
        positionIterator.close();

        _sequenceIterator = this._children.get(0);
        _sequenceIterator.open(context);
        setNextResult();
    }

    public void setNextResult() {
        _nextResult = null;

        if (_sequenceIterator.hasNext()) {
            if (_currentPosition == _removePosition) {
                _sequenceIterator.next();   // skip item to be removed
                _currentPosition++;
                if (_sequenceIterator.hasNext()) {
                    _nextResult = _sequenceIterator.next();
                    _currentPosition++;
                }
            } else {
                _nextResult = _sequenceIterator.next();
                _currentPosition++;
            }
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _sequenceIterator.close();
        } else {
            this._hasNext = true;
        }
    }
}
