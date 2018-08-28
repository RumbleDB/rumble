package sparksoniq.jsoniq.runtime.iterator.functions.sequences.value;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.EmptySequenceIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class IndexOfFunctionIterator extends LocalFunctionCallIterator {

    private List<IntegerItem> results;
    private int _currentIndex;

    public IndexOfFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            return getResult();
        } else {
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "index-of function", getMetadata());
        }
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
        results = new ArrayList<>();
        _currentIndex = 0;

        RuntimeIterator sequenceIterator = this._children.get(0);
        RuntimeIterator searchIterator = this._children.get(1);

        if (searchIterator instanceof EmptySequenceIterator) {
            throw new UnexpectedTypeException("Invalid args. index-of can't be performed with empty sequences", getMetadata());
        }

        searchIterator.open(context);
        Item search = searchIterator.next();
        if (searchIterator.hasNext()) {
            throw new UnexpectedTypeException("Invalid args. index-of can't be performed with sequences with more than one items", getMetadata());
        }
        if (search instanceof ArrayItem) {
            throw new NonAtomicKeyException("Invalid args. index-of can't be performed with an array parameter", getMetadata().getExpressionMetadata());
        } else if (search instanceof ObjectItem) {
            throw new NonAtomicKeyException("Invalid args. index-of can't be performed with an object parameter", getMetadata().getExpressionMetadata());
        }

        List<Item> items = getItemsFromIteratorWithCurrentContext(sequenceIterator);
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            // TODO: learn if and how to check array and object distinctness
            if (item instanceof ArrayItem || item instanceof ObjectItem) {
                // do nothing
            } else {
                if (Item.compareItems(item, search) == 0) {
                    // +1 applied b/c JSONiq indexes start from 1
                    results.add(new IntegerItem(i+1, ItemMetadata.fromIteratorMetadata(getMetadata())));
                }
            }
        }

        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }
}
