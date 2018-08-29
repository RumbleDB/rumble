package sparksoniq.jsoniq.runtime.iterator.functions.sequences;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class EmptyFunctionIterator extends LocalFunctionCallIterator {

    private BooleanItem result;

    public EmptyFunctionIterator(List<RuntimeIterator> parameters, IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "empty function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        RuntimeIterator sequenceIterator = this._children.get(0);
        sequenceIterator.open(context);
        if (sequenceIterator.hasNext() == false) {
            this.result = new BooleanItem(true, ItemMetadata.fromIteratorMetadata(getMetadata()));
        } else {
            this.result = new BooleanItem(false, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        sequenceIterator.close();
        this._hasNext = true;
    }
}
