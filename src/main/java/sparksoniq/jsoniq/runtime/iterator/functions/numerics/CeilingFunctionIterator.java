package sparksoniq.jsoniq.runtime.iterator.functions.numerics;

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

public class CeilingFunctionIterator extends LocalFunctionCallIterator {

    private Item result;

    public CeilingFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " ceiling function", getMetadata());
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
        } else {
            Item value = this.getSingleItemOfTypeFromIterator(iterator, Item.class);
            if (Item.isNumeric(value)) {
                Double result = Math.ceil(Item.getNumericValue(value, Double.class));
                this._hasNext = true;
                this.result = new DoubleItem(result,
                        ItemMetadata.fromIteratorMetadata(getMetadata()));
            } else {
                throw new UnexpectedTypeException("Ceiling expression has non numeric args " +
                        value.serialize(), getMetadata());
            }
        }
    }
}
