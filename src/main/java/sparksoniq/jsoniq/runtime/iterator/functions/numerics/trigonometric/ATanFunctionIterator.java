package sparksoniq.jsoniq.runtime.iterator.functions.numerics.trigonometric;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class ATanFunctionIterator extends LocalFunctionCallIterator {

    private RuntimeIterator _iterator;

    public ATanFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
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
            this._hasNext = false;
            Item radians = this.getSingleItemOfTypeFromIterator(_iterator, Item.class);
            if (Item.isNumeric(radians)) {
                try {
                    Double result = Math.atan(Item.getNumericValue(radians, Double.class));
                    return new DoubleItem(result);

                } catch (IteratorFlowException e)
                {
                    throw new IteratorFlowException(e.getMessage(), getMetadata());

                }
            } else {
                throw new UnexpectedTypeException("ATan expression has non numeric args " +
                        radians.serialize(), getMetadata());
            }
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " atan function", getMetadata());
    }


}
