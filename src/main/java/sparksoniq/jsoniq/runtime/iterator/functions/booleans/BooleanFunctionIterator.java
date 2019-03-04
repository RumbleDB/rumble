package sparksoniq.jsoniq.runtime.iterator.functions.booleans;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class BooleanFunctionIterator extends LocalFunctionCallIterator {
    public BooleanFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            RuntimeIterator iterator = this._children.get(0);
            iterator.open(_currentDynamicContext);
            boolean effectiveBooleanValue = getEffectiveBooleanValue(iterator);
            iterator.close();
            return new BooleanItem(effectiveBooleanValue);
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " boolean function", getMetadata());
    }
}
