package sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class OneOrMoreIterator extends CardinalityFunctionIterator {

    private RuntimeIterator _iterator;
    private Item _nextLocalResult;
    private List<Item> _rddResults;
    private int _rddResultIndex;

    public OneOrMoreIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);
        if (!_iterator.hasNext()) {
            throw new IllegalArgumentException("fn:one-or-more() called with a sequence containing less than 1 item");
        } else {
            if (!_iterator.isRDD()) {
                setNextLocalResult();
            }
        }
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            if (!_iterator.isRDD()) {
                Item result = _nextLocalResult;  // save the result to be returned
                setNextLocalResult();            // calculate and store the next result
                return result;
            } else {
                if (_rddResults == null) {
                    _rddResults = _iterator.getRDD().collect();
                    _rddResultIndex = 0;
                }
                Item result =_rddResults.get(_rddResultIndex++);
                if (_rddResultIndex == _rddResults.size()) {
                    this._hasNext = false;
                }
                return result;
            }
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " ONE-OR-MORE function",
                getMetadata());
    }

    public void setNextLocalResult() {
        _nextLocalResult = null;

        if (_iterator.hasNext()) {
            _nextLocalResult = _iterator.next();
        }

        if (_nextLocalResult == null) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }
}
