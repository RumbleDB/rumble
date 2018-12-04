package sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SequenceExceptionOneOrMore;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class OneOrMoreIterator extends CardinalityFunctionIterator {

    private RuntimeIterator _iterator;
    private Item _nextResult;

    public OneOrMoreIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);
        if (!_iterator.hasNext()) {
            throw new SequenceExceptionOneOrMore("fn:one-or-more() called with a sequence containing less than 1 item", getMetadata());
        }
        setNextResult();
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " ONE-OR-MORE function",
                getMetadata());
    }

    public void setNextResult() {
        _nextResult = null;

        if (_iterator.hasNext()) {
            _nextResult = _iterator.next();
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }
}
