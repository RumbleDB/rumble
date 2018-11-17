package sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MinFunctionIterator extends AggregateFunctionIterator {

    private RuntimeIterator _iterator;

    public MinFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, AggregateFunctionOperator.MIN, iteratorMetadata);
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
                if (!r.isAtomic())
                    throw new UnexpectedTypeException("Min expression has non numeric args " +
                            r.serialize(), getMetadata());
            });

            return Collections.min(results);
        } else
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "MIN function",
                    getMetadata());
    }
}
