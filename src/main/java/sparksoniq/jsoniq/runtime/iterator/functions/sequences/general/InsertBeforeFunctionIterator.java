package sparksoniq.jsoniq.runtime.iterator.functions.sequences.general;

import java.util.List;

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

public class InsertBeforeFunctionIterator extends LocalFunctionCallIterator {

    private RuntimeIterator _sequenceIterator;
    private RuntimeIterator _insertIterator;
    private Item _nextResult;
    private int _insertPosition;            // position to start inserting
    private int _currentPosition;           // current position
    private boolean _insertingNow;          // check if currently iterating over insertIterator
    private boolean _insertingCompleted;

    public InsertBeforeFunctionIterator(List<RuntimeIterator> parameters, IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "insert-before function", getMetadata());
    }


    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _currentPosition = 1;      // initialize index as the first item
        _insertingNow = false;
        _insertingCompleted = false;

        RuntimeIterator positionIterator = this._children.get(1);
        positionIterator.open(context);
        if (!positionIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. insert-before can't be performed with empty sequence as the position",
                    getMetadata());
        }
        Item positionItem = positionIterator.next();
        if (positionItem instanceof ArrayItem) {
            throw new NonAtomicKeyException(
                    "Invalid args. insert-before can't be performed with an array parameter as the position",
                    getMetadata().getExpressionMetadata());
        } else if (positionItem instanceof ObjectItem) {
            throw new NonAtomicKeyException(
                    "Invalid args. insert-before can't be performed with an object parameter as the position",
                    getMetadata().getExpressionMetadata());
        } else if (!(positionItem instanceof IntegerItem)) {
            throw new UnexpectedTypeException(
                    "Invalid args. Position parameter should be an integer",
                    getMetadata());
        }
        _insertPosition = ((IntegerItem) positionItem).getIntegerValue();
        positionIterator.close();

        _sequenceIterator = this._children.get(0);
        _insertIterator = this._children.get(2);

        _sequenceIterator.open(context);
        _insertIterator.open(context);

        setNextResult();
    }

    public void setNextResult() {
        _nextResult = null;

        // don't check for insertion triggers once insertion is completed
        if (_insertingCompleted == false) {
            if (!_insertingNow) {
                if (_insertPosition <= _currentPosition) {  // start inserting if condition is met
                    if (_insertIterator.hasNext()) {
                        _insertingNow = true;
                        _nextResult = _insertIterator.next();
                    } else {
                        _insertingNow = false;
                        _insertingCompleted = true;
                    }
                }
            } else { // if inserting
                if (_insertIterator.hasNext()) { // return an item from _insertIterator at each iteration
                    _nextResult = _insertIterator.next();
                } else {
                    _insertingNow = false;
                    _insertingCompleted = true;
                }
            }
        }

        // if not inserting, take the next element from input sequence
        if (_insertingNow == false) {
            if (_sequenceIterator.hasNext()) {
                _nextResult = _sequenceIterator.next();
                _currentPosition++;
            } else if (_insertIterator.hasNext()){
                _nextResult = _insertIterator.next();
            }
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _sequenceIterator.close();
            _insertIterator.close();
        } else {
            this._hasNext = true;
        }
    }
}
