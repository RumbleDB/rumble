package sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class ZeroOrOneIterator extends CardinalityFunctionIterator {

    public ZeroOrOneIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            return getResult();
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " ZERO-OR-ONE function",
                getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        RuntimeIterator sequenceIterator = this._children.get(0);
        this.results = getItemsFromIteratorWithCurrentContext(sequenceIterator);

        if (!(results.size() == 0 || results.size() == 1)) {
            throw new IllegalArgumentException("fn:zero-or-one() called with a sequence containing more than one item");
        } else if (results.size() == 0) {
            this._hasNext = false;
        } else if (results.size() == 1) {
            this._hasNext = true;
        }
    }
}
