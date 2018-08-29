package sparksoniq.jsoniq.runtime.iterator.functions.sequences;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

public class TailFunctionIterator extends LocalFunctionCallIterator {

    private List<Item> results;
    private int _currentIndex = 0;

    public TailFunctionIterator(List<RuntimeIterator> parameters, IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            return getResult();
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "tail function", getMetadata());
    }

    public Item getResult() {
        if (results == null || results.size() == 0) {
            throw new IteratorFlowException("getResult called on an empty list of results", getMetadata());
        }
        if (_currentIndex == results.size() - 1)
            _hasNext = false;
        return results.get(_currentIndex++);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.results = new ArrayList<>();
        this._currentIndex = 0;

        RuntimeIterator sequenceIterator = this._children.get(0);
        sequenceIterator.open(context);
        if (sequenceIterator.hasNext()) {
            sequenceIterator.next();    // skip the first item
            while (sequenceIterator.hasNext()) {
                results.add(sequenceIterator.next());
            }
        }
        sequenceIterator.close();

        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }
}
