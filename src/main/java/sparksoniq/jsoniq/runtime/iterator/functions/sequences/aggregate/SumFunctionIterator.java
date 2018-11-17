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

public class SumFunctionIterator extends AggregateFunctionIterator {


    public SumFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, AggregateFunctionOperator.SUM, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            RuntimeIterator sequenceIterator = this._children.get(0);
            List<Item> results = getItemsFromIteratorWithCurrentContext(sequenceIterator);
            this._hasNext = false;
            results.forEach(r -> {
                if (!r.isNumeric())
                    throw new UnexpectedTypeException("Sum expression has non numeric args " +
                            r.serialize(), getMetadata());
            });

            BigDecimal sumResult = new BigDecimal(0);
            for (Item r : results) {
                BigDecimal current = r.getNumericValue(BigDecimal.class);
                sumResult = sumResult.add(current);
            }
            return new DecimalItem(sumResult, ItemMetadata.fromIteratorMetadata(getMetadata()));

        } else
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "SUM function",
                    getMetadata());
    }
}
