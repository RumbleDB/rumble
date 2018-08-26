package sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.math.BigDecimal;
import java.util.List;

public class MaxFunctionIterator extends AggregateFunctionIterator {

    private Item result;

    public MaxFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, AggregateFunctionOperator.MAX, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        } else {
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "MAX function", getMetadata());
        }
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        RuntimeIterator sequenceIterator = this._children.get(0);
        List<Item> results = getItemsFromIteratorWithCurrentContext(sequenceIterator);
        results.forEach(r -> {
            if (!Item.isNumeric(r))
                throw new IllegalArgumentException("Aggregate function argument is non numeric");
        });
        if (results.size() == 0) {
            this._hasNext = false;
        }
        else {
            Item itemResult = results.get(0);
            BigDecimal max  = Item.getNumericValue(results.get(0), BigDecimal.class);
            for(Item r: results) {
                BigDecimal current = Item.getNumericValue(r, BigDecimal.class);
                if(max.compareTo(current) < 0) {
                    max = current;
                    itemResult = r;
                }
            }
            this.result = itemResult;
            this._hasNext = true;
        }
    }
}
