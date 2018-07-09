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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class RoundFunctionIterator extends LocalFunctionCallIterator {
    public RoundFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            RuntimeIterator iterator = this._children.get(0);
            //TODO refactor empty items
            if (iterator.getClass() == EmptySequenceIterator.class) {
                return null;
            }
            else {
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
                    bd = bd.setScale(prec, RoundingMode.HALF_UP);
                    Double result = bd.doubleValue();

                    this._hasNext = false;
                    return new DoubleItem(result,
                            ItemMetadata.fromIteratorMetadata(getMetadata()));
                }
                else {
                    throw new UnexpectedTypeException("Round expression has non numerics args " +
                            value.serialize() + ", " + precision.serialize(), getMetadata());
                }
            }
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " round function", getMetadata());
    }
}
