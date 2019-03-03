package sparksoniq.jsoniq.runtime.iterator.functions.numerics.exponential;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class PowFunctionIterator extends LocalFunctionCallIterator {

    private RuntimeIterator _iterator;

    public PowFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _iterator = this._children.get(0);
        _iterator.open(_currentDynamicContext);
        if (_iterator.hasNext()) {
            this._hasNext = true;
        } else {
            this._hasNext = false;
        }
        _iterator.close();
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item base = this.getSingleItemOfTypeFromIterator(_iterator, Item.class);
            Item exponent;
            RuntimeIterator exponentIterator = this._children.get(1);
            exponentIterator.open(_currentDynamicContext);
            if (exponentIterator.hasNext()) {
                exponent = exponentIterator.next();
            } else {
                throw new UnexpectedTypeException("Type error; Exponent parameter can't be empty sequence ", getMetadata());
            }
            if (Item.isNumeric(base) && Item.isNumeric(exponent)) {
                try {
                    Double result = Math.pow(Item.getNumericValue(base, Double.class)
                            , Item.getNumericValue(exponent, Double.class));
                    this._hasNext = false;
                    return new DoubleItem(result);
                } catch (IteratorFlowException e)
                {
                    throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
                }
            } else {
                throw new UnexpectedTypeException("Pow expression has non numeric args " +
                        base.serialize() + ", " + exponent.serialize(), getMetadata());
            }

        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " pow function", getMetadata());
    }


}
