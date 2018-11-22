package sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SequenceExceptionExactlyOne;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class ExactlyOneIterator extends CardinalityFunctionIterator {

    private Item _nextResult;

    public ExactlyOneIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            return _nextResult;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " EXACTLY-ONE function",
                getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        RuntimeIterator sequenceIterator = this._children.get(0);

        if (!sequenceIterator.isRDD()) {
            sequenceIterator.open(context);
            if (!sequenceIterator.hasNext()) {
                throw new SequenceExceptionExactlyOne("fn:exactly-one() called with a sequence that doesn't contain exactly one item", getMetadata());
            } else {
                _nextResult = sequenceIterator.next();
                if (sequenceIterator.hasNext()) {
                    throw new SequenceExceptionExactlyOne("fn:exactly-one() called with a sequence that doesn't contain exactly one item", getMetadata());
                } else {
                    this._hasNext = true;
                }
            }
            sequenceIterator.close();
        } else {
            if (sequenceIterator.getRDD().count() == 1) {
                this._hasNext = true;
                _nextResult = sequenceIterator.getRDD().collect().get(0);
            } else {
                throw new SequenceExceptionExactlyOne("fn:exactly-one() called with a sequence that doesn't contain exactly one item", getMetadata());
            }
        }
    }
}
