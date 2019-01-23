package sparksoniq.jsoniq.runtime.iterator.functions.sequences.general;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class SubsequenceFunctionIterator extends LocalFunctionCallIterator {

    private RuntimeIterator _sequenceIterator;
    private Item _nextResult;
    private int _currentPosition;
    private int _startPosition;
    private int _length;

    public SubsequenceFunctionIterator(List<RuntimeIterator> parameters, IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _currentPosition = 1;   // JSONiq indices start from 1

        _length = -1;   // unassigned
        // if length param is given, process it
        RuntimeIterator lengthIterator;
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
            // round double to nearest int
            try {
                _length = (int) Math.round((Item.getNumericValue(lengthItem, Double.class)));

            } catch (IteratorFlowException e)
            {
                throw new IteratorFlowException(e.getMessage(), getMetadata());

            }
        }

        // process start position param
        RuntimeIterator positionIterator = this._children.get(1);
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
        // round double to nearest int
        _startPosition = (int) Math.round((Item.getNumericValue(positionItem, Double.class)));

        // first, perform all parameter checks (above)
        // if length is 0, just return empty sequence
        if (_length == 0) {
            this._hasNext = false;
            return;
        } else {
            _sequenceIterator = this._children.get(0);
            _sequenceIterator.open(context);

            // find the start of the subsequence
            while (_sequenceIterator.hasNext()) {
                if (_currentPosition < _startPosition) {
                    _sequenceIterator.next();   // skip item
                } else {
                    _nextResult = _sequenceIterator.next();
                    // if length is specified, decrement it
                    if (_length != -1) {
                        _length--;
                    }
                    break;
                }
                _currentPosition++;
            }
        }

        // if _startPosition overshoots, return empty sequence
        if (_nextResult == null) {
            this._hasNext = false;
            _sequenceIterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "subsequence function", getMetadata());
    }

    public void setNextResult() {
        _nextResult = null;

        if (_length != 0) {
            if (_sequenceIterator.hasNext()) {
                if (_length > 0) {      // take _length many items -> decrement the value for each item until 0
                    _nextResult = _sequenceIterator.next();
                    _length--;
                } else if (_length == -1) {     // _length not specified -> take all items until the end
                    _nextResult = _sequenceIterator.next();
                } else {
                    throw new SparksoniqRuntimeException("Unexpected length value found. Please report the bug with subsequence function iterator.");
                }
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
