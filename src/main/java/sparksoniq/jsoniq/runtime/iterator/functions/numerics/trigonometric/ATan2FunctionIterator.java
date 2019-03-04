package sparksoniq.jsoniq.runtime.iterator.functions.numerics.trigonometric;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class ATan2FunctionIterator extends LocalFunctionCallIterator {
    public ATan2FunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item y;
            RuntimeIterator yIterator = this._children.get(0);
            yIterator.open(_currentDynamicContext);
            if (yIterator.hasNext()) {
                y = yIterator.next();
            } else {
                throw new UnexpectedTypeException("Type error; y parameter can't be empty sequence ", getMetadata());
            }

            Item x;
            RuntimeIterator xIterator = this._children.get(1);
            xIterator.open(_currentDynamicContext);
            if (xIterator.hasNext()) {
                x = xIterator.next();
            } else {
                throw new UnexpectedTypeException("Type error; x parameter can't be empty sequence ", getMetadata());
            }

            if (Item.isNumeric(y) && Item.isNumeric(x)) {
                try {
                    Double result = Math.atan2(Item.getNumericValue(y, Double.class)
                            , Item.getNumericValue(x, Double.class));
                    this._hasNext = false;
                    return new DoubleItem(result);

                } catch (IteratorFlowException e)
                {
                    throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
                }
            } else {
                throw new UnexpectedTypeException("ATan2 expression has non numeric args " +
                        y.serialize() + ", " + x.serialize(), getMetadata());
            }
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " atan2 function", getMetadata());
    }
}