package sparksoniq.jsoniq.runtime.iterator.functions.numerics.trigonometric;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.EmptySequenceIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class ATan2FunctionIterator extends LocalFunctionCallIterator {
    public ATan2FunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " atan2 function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;

        Item y = this.getSingleItemOfTypeFromIterator(this._children.get(0), Item.class);
        Item x = this.getSingleItemOfTypeFromIterator(this._children.get(1), Item.class);
        if (Item.isNumeric(y) && Item.isNumeric(x)) {
            Double result = Math.atan2(Item.getNumericValue(y, Double.class)
                    , Item.getNumericValue(x, Double.class));
            this._hasNext = true;
            this.result = new DoubleItem(result,
                    ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        else {
            throw new UnexpectedTypeException("ATan2 expression has non numeric args " +
                    y.serialize() + ", " + x.serialize(), getMetadata());

        }
    }

    Item result;
}