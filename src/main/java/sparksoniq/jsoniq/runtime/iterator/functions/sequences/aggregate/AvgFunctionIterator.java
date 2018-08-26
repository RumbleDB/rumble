package sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.math.BigDecimal;
import java.util.List;

public class AvgFunctionIterator extends AggregateFunctionIterator {

    private Item result;

    public AvgFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, AggregateFunctionOperator.AVG, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        } else {
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "AVG function", getMetadata());
        }
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;

        RuntimeIterator sequenceIterator = this._children.get(0);
        List<Item> results = getItemsFromIteratorWithCurrentContext(sequenceIterator);
        results.forEach(r -> {
            if (!Item.isNumeric(r))
                throw new IllegalArgumentException("Aggregate function argument is non numeric");
        });
        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
        //TODO check numeric types conversions
        BigDecimal sum = new BigDecimal(0);
        for (Item r : results)
            sum = sum.add(Item.getNumericValue(r, BigDecimal.class));
        this.result = new DecimalItem(sum.divide(new BigDecimal(results.size())),
                ItemMetadata.fromIteratorMetadata(getMetadata()));
    }
}
