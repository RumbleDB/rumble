package sparksoniq.jsoniq.runtime.iterator.functions.numerics;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.EmptySequenceIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class RoundHalfToEvenFunctionIterator extends LocalFunctionCallIterator {

    private Item result;

    public RoundHalfToEvenFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " round-half-to-even function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;

        RuntimeIterator iterator = this._children.get(0);
        if (iterator.getClass() == EmptySequenceIterator.class) {
            this._hasNext = true;
        } else {
            Item value = this.getSingleItemOfTypeFromIterator(iterator, Item.class);
            Item precision;
            if (this._children.size() > 1) {
                precision = this.getSingleItemOfTypeFromIterator(this._children.get(1), Item.class);
            }
            // if second param is not given precision is set as 0 (rounds to a whole number)
            else {
                precision = new IntegerItem(0, ItemMetadata.fromIteratorMetadata(this.getMetadata()));
            }
            if (Item.isNumeric(value) && Item.isNumeric(precision)) {

                Double val = Item.getNumericValue(value, Double.class);
                Integer prec = Item.getNumericValue(precision, Integer.class);

                BigDecimal bd = new BigDecimal(val);
                bd = bd.setScale(prec, RoundingMode.HALF_EVEN);
                Double result = bd.doubleValue();

                this._hasNext = true;
                this.result = new DoubleItem(result,
                        ItemMetadata.fromIteratorMetadata(getMetadata()));
            } else {
                throw new UnexpectedTypeException("Round-half-to-even expression has non numeric args " +
                        value.serialize() + ", " + precision.serialize(), getMetadata());
            }
        }
    }
}
