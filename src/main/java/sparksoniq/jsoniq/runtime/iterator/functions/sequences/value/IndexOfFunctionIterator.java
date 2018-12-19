package sparksoniq.jsoniq.runtime.iterator.functions.sequences.value;

import java.util.List;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

public class IndexOfFunctionIterator extends LocalFunctionCallIterator {

    private RuntimeIterator _sequenceIterator;
    private Item _search;
    private Item _nextResult;
    private int _currentIndex;

    public IndexOfFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "index-of function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _sequenceIterator = this._children.get(0);
        RuntimeIterator searchIterator = this._children.get(1);
        _currentIndex = 0;

        _sequenceIterator .open(context);
        searchIterator.open(context);

        if (!searchIterator.hasNext()) {
            throw new UnexpectedTypeException("Invalid args. index-of can't be performed with empty sequences", getMetadata());
        }
        _search = searchIterator.next();
        if (searchIterator.hasNext()) {
            throw new UnexpectedTypeException("Invalid args. index-of can't be performed with sequences with more than one items", getMetadata());
        }
        if (!_search.isAtomic()) {
            throw new NonAtomicKeyException("Invalid args. index-of can't be performed with a non-atomic parameter", getMetadata().getExpressionMetadata());
        }
        searchIterator.close();

        setNextResult();
    }

    public void setNextResult() {
        _nextResult = null;

        while(_sequenceIterator.hasNext()) {
            _currentIndex += 1;
            Item item = _sequenceIterator.next();
            if (!item.isAtomic()) {
                throw new NonAtomicKeyException("Invalid args. index-of can't be performed with a non-atomic in the input sequence", getMetadata().getExpressionMetadata());
            } else {
                if (Item.compareItems(item, _search) == 0) {
                    _nextResult = new IntegerItem(_currentIndex, ItemMetadata.fromIteratorMetadata(getMetadata()));
                    break;
                }
            }
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _sequenceIterator.close();
        } else {
            this._hasNext = true;
        }
    }
}
