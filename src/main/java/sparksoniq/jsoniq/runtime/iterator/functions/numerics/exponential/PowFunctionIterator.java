package sparksoniq.jsoniq.runtime.iterator.functions.numerics.exponential;

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

public class PowFunctionIterator extends LocalFunctionCallIterator {
    public PowFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " pow function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;

        RuntimeIterator iterator = this._children.get(0);
        if (iterator.getClass() == EmptySequenceIterator.class) {
            this._hasNext = false;
        }
        else {
            Item base = this.getSingleItemOfTypeFromIterator(iterator, Item.class);
            Item exponent = this.getSingleItemOfTypeFromIterator(this._children.get(1), Item.class);
            if (Item.isNumeric(base) && Item.isNumeric(exponent)) {
                Double result = Math.pow(Item.getNumericValue(base, Double.class)
                        , Item.getNumericValue(exponent, Double.class));
                this._hasNext = true;
                this.result = new DoubleItem(result,
                        ItemMetadata.fromIteratorMetadata(getMetadata()));
            }
            else {
                throw new UnexpectedTypeException("Pow expression has non numeric args " +
                        base.serialize() + ", " + exponent.serialize(), getMetadata());
            }
        }
    }

    Item result;
}
