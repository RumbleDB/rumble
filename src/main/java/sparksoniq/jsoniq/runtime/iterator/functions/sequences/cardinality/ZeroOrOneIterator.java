package sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class ZeroOrOneIterator extends CardinalityFunctionIterator {

    private Item _nextResult;

    public ZeroOrOneIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            return _nextResult;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " ZERO-OR-ONE function",
                getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        RuntimeIterator sequenceIterator = this._children.get(0);
        sequenceIterator.open(context);
        if (!sequenceIterator.hasNext()) {
            this._hasNext = false;
        } else {
            _nextResult = sequenceIterator.next();
            if (sequenceIterator.hasNext()) {
                throw new IllegalArgumentException("fn:zero-or-one() called with a sequence containing more than one item");
            } else {
                this._hasNext = true;
            }
        }
        sequenceIterator.close();
    }
}
