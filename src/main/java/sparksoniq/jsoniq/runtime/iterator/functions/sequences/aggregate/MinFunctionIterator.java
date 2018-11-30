package sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate;

import sparksoniq.exceptions.InvalidArgumentTypeException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemComparatorForSequences;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

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
            this._hasNext = false;
            ItemComparatorForSequences comparator = new ItemComparatorForSequences();
            if (!_iterator.isRDD()) {
                List<Item> results = getItemsFromIteratorWithCurrentContext(_iterator);

                try {
                    return Collections.min(results, comparator);
                } catch (SparksoniqRuntimeException e) {
                    throw new InvalidArgumentTypeException("Min expression input error. Input has to be non-null atomics of matching types: " + e.getMessage(), getMetadata());
                }
            } else {
                try {
                    return _iterator.getRDD().min(comparator);
                } catch (SparksoniqRuntimeException e) {
                    throw new InvalidArgumentTypeException("Min expression input error. Input has to be non-null atomics of matching types: " + e.getMessage(), getMetadata());
                }
            }
        } else
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "MIN function",
                    getMetadata());
    }
}
