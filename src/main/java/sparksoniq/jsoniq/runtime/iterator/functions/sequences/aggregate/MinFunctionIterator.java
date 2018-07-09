package sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.math.BigDecimal;
import java.util.List;

public class MinFunctionIterator extends ArithmeticFunctionIterator {
    public MinFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ArithmeticFunctionOperator.MIN, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            RuntimeIterator sequenceIterator = this._children.get(0);
            List<Item> results = getItemsFromIteratorWithCurrentContext(sequenceIterator);
            this._hasNext = false;
            results.forEach(r -> {
                if (!Item.isNumeric(r))
                    throw new IllegalArgumentException("Arithmetic function argument is non numerics");
            });
            //TODO refactor empty items
            if (results.size() == 0)
                return null;
            Item itemResult = results.get(0);
            BigDecimal min = Item.getNumericValue(results.get(0), BigDecimal.class);
            for (Item r : results) {
                BigDecimal current = Item.getNumericValue(r, BigDecimal.class);
                if (min.compareTo(current) > 0) {
                    min = current;
                    itemResult = r;
                }
            }
            return itemResult;
        } else
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "MIN function",
                    getMetadata());
    }
}
