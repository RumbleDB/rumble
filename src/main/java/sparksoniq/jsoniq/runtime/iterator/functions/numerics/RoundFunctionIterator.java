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

public class RoundFunctionIterator extends LocalFunctionCallIterator {

    private RuntimeIterator _iterator;

    public RoundFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
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
            Item precision;
            if (this._children.size() > 1) {
                RuntimeIterator precisionIterator = this._children.get(1);
                precisionIterator.open(_currentDynamicContext);
                if (precisionIterator.hasNext()) {
                    precision = precisionIterator.next();
                } else {
                    throw new UnexpectedTypeException("Type error; Precision parameter can't be empty sequence ", getMetadata());
                }
            }
            // if second param is not given precision is set as 0 (rounds to a whole number)
            else {
                precision = new IntegerItem(0, ItemMetadata.fromIteratorMetadata(this.getMetadata()));
            }
            if (Item.isNumeric(value) && Item.isNumeric(precision)) {

                Double val = Item.getNumericValue(value, Double.class);
                Integer prec = Item.getNumericValue(precision, Integer.class);

                BigDecimal bd = new BigDecimal(val);
                bd = bd.setScale(prec, RoundingMode.HALF_UP);
                Double result = bd.doubleValue();

                return new DoubleItem(result,
                        ItemMetadata.fromIteratorMetadata(getMetadata()));
            } else {
                throw new UnexpectedTypeException("Round expression has non numeric args " +
                        value.serialize() + ", " + precision.serialize(), getMetadata());
            }

        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " round function", getMetadata());
    }
}
