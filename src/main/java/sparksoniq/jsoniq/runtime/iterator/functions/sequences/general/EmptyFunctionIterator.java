package sparksoniq.jsoniq.runtime.iterator.functions.sequences.general;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class EmptyFunctionIterator extends LocalFunctionCallIterator {

    public EmptyFunctionIterator(List<RuntimeIterator> parameters, IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this._hasNext = false;
            RuntimeIterator sequenceIterator = this._children.get(0);
            sequenceIterator.open(_currentDynamicContext);
            Item result;
            if (sequenceIterator.hasNext()) {
                result = new BooleanItem(false);

            } else {
                result = new BooleanItem(true);
            }
            sequenceIterator.close();
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "empty function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
    }
}
