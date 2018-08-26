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

public class SumFunctionIterator extends AggregateFunctionIterator {

    private Item result;

    public SumFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, AggregateFunctionOperator.SUM, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        } else {
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "SUM function", getMetadata());
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
        if (results.size() == 0)
            this._hasNext = false;
        BigDecimal sumResult = new BigDecimal(0);
        for (Item r : results) {
            BigDecimal current = Item.getNumericValue(r, BigDecimal.class);
            sumResult = sumResult.add(current);
        }
        this.result = new DecimalItem(sumResult, ItemMetadata.fromIteratorMetadata(getMetadata()));
        this._hasNext = true;
    }
}
