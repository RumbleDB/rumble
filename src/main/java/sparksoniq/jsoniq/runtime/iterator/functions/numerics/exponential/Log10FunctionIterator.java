package sparksoniq.jsoniq.runtime.iterator.functions.numerics.exponential;

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

public class Log10FunctionIterator extends LocalFunctionCallIterator {

    private RuntimeIterator _iterator;

    public Log10FunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
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
            Item value = this.getSingleItemOfTypeFromIterator(_iterator, Item.class);
            if (Item.isNumeric(value)) {
                try {
                    Double result = Math.log10(Item.getNumericValue(value, Double.class));
    
                    return new DoubleItem(result);
                
                } catch (IteratorFlowException e)
                {
                    throw new IteratorFlowException(e.getMessage(), getMetadata());

                }
            } else {
                throw new UnexpectedTypeException("Log10 expression has non numeric args " +
                        value.serialize(), getMetadata());
            }
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " log10 function", getMetadata());
    }
}
