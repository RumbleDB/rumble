package sparksoniq.jsoniq.runtime.iterator.functions.arithmetic;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.math.BigDecimal;
import java.util.List;

public class AvgFunctionIterator extends ArithmeticFunctionIterator {
    public AvgFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ArithmeticFunctionOperator.AVG, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            RuntimeIterator sequenceIterator = this._children.get(0);
            List<Item> results = getItemsFromIteratorWithCurrentContext(sequenceIterator);
            this._hasNext = false;
            results.forEach(r -> {
                if (!Item.isNumeric(r))
                    throw new IllegalArgumentException("Arithmetic function argument is non numeric");
            });
            //TODO refactor empty items
            if (results.size() == 0)
                return null;
            //TODO check numeric types conversions
            BigDecimal sum = new BigDecimal(0);
            for (Item r : results)
                sum = sum.add(Item.getNumericValue(r, BigDecimal.class));
            return new DecimalItem(sum.divide(new BigDecimal(results.size())),
                    ItemMetadata.fromIteratorMetadata(getMetadata()));
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + "AVG function",
                    getMetadata());
    }
}
