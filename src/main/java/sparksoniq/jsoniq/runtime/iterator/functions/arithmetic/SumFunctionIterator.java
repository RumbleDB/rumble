package sparksoniq.jsoniq.runtime.iterator.functions.arithmetic;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.math.BigDecimal;
import java.util.List;

public class SumFunctionIterator extends ArithmeticFunctionIterator {
    public SumFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ArithmeticFunctionOperator.SUM, iteratorMetadata);
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
            BigDecimal sumResult = new BigDecimal(0);
            for (Item r : results) {
                BigDecimal current = Item.getNumericValue(r, BigDecimal.class);
                sumResult = sumResult.add(current);
            }
            return new DecimalItem(sumResult, ItemMetadata.fromIteratorMetadata(getMetadata()));

        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + "SUM function",
                    getMetadata());
    }
}
