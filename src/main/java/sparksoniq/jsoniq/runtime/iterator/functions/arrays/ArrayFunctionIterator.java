package sparksoniq.jsoniq.runtime.iterator.functions.arrays;

import sparksoniq.jsoniq.item.EmptySequenceItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public abstract class ArrayFunctionIterator extends LocalFunctionCallIterator {
    private final ArrayFunctionOperators _operator;
    protected List<Item> results = null;
    protected int _currentIndex;

    protected ArrayFunctionIterator(List<RuntimeIterator> arguments, ArrayFunctionOperators op,
                                    IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
        this._operator = op;
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        this.results = null;
    }

    public Item getResult() {
        // if no results return empty sequence
        if (results == null || results.size() == 0) {
            _hasNext = false;
            return new EmptySequenceItem(ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        if (_currentIndex == results.size() - 1)
            _hasNext = false;
        return results.get(_currentIndex++);
    }

    public enum ArrayFunctionOperators {
        SIZE,
        MEMBERS,
        DESCENDANT,
        FLATTEN
    }
}
