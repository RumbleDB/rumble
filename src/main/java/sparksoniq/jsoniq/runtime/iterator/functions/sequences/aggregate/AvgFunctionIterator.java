package sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.math.BigDecimal;
import java.util.List;

public class AvgFunctionIterator extends AggregateFunctionIterator {

    private RuntimeIterator _iterator;

    public AvgFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, AggregateFunctionOperator.AVG, iteratorMetadata);
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
            List<Item> results = getItemsFromIteratorWithCurrentContext(_iterator);
            this._hasNext = false;
            results.forEach(r -> {
                if (!r.isNumeric())
                    throw new UnexpectedTypeException("Average expression has non numeric args " +
                            r.serialize(), getMetadata());
            });
            //TODO check numeric types conversions
            BigDecimal sum = new BigDecimal(0);
            for (Item r : results)
                sum = sum.add(r.getNumericValue(BigDecimal.class));
            return new DecimalItem(sum.divide(new BigDecimal(results.size())),
                    ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "AVG function",
                    getMetadata());
    }
}
