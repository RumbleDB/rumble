package sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate;

import java.math.BigDecimal;
import java.util.List;

import sparksoniq.exceptions.InvalidArgumentTypeException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

public class SumFunctionIterator extends AggregateFunctionIterator {

    private RuntimeIterator _iterator;
    private Item _zeroItem;

    public SumFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, AggregateFunctionOperator.SUM, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);
        if (!_iterator.hasNext()) {
            if (this._children.size() > 1) {
                RuntimeIterator zeroIterator = this._children.get(1);
                zeroIterator.open(_currentDynamicContext);
                if (!zeroIterator.hasNext()) {
                    this._hasNext = false;
                    return;
                } else {
                    _zeroItem = zeroIterator.next();
                    if (!_zeroItem.isAtomic()) {
                        throw new NonAtomicKeyException("Invalid args. Zero item has to be of an atomic type", getMetadata().getExpressionMetadata());
                    }
                }
            }
        }
        _iterator.close();
        this._hasNext= true;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            List<Item> results = getItemsFromIteratorWithCurrentContext(_iterator);

            // if input is empty sequence and _zeroItem is given
            if (results.size() == 0 && _zeroItem != null) {
                return _zeroItem;
            }

            results.forEach(r -> {
                if (!Item.isNumeric(r))
                    throw new InvalidArgumentTypeException("Sum expression has non numeric args " +
                            r.serialize(), getMetadata());
            });

            // if input is empty sequence and _zeroItem is not given 0 is returned
            BigDecimal sumResult = new BigDecimal(0);
            for (Item r : results) {
                BigDecimal current = Item.getNumericValue(r, BigDecimal.class);
                sumResult = sumResult.add(current);
            }
            return new DecimalItem(sumResult, ItemMetadata.fromIteratorMetadata(getMetadata()));

        } else
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "SUM function",
                    getMetadata());
    }
}
