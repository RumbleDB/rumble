package sparksoniq.jsoniq.runtime.iterator.functions.arrays;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArrayFlattenFunctionIterator extends ArrayFunctionIterator {

    private RuntimeIterator _iterator;
    private Queue<Item> _nextResults;   // queue that holds the results created by the current item in inspection

    public ArrayFlattenFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ArrayFunctionOperators.FLATTEN, iteratorMetadata);
    }

    @Override
    public Item next() {
        if(_hasNext == true){
            Item result = _nextResults.remove();  // save the result to be returned
            if (_nextResults.isEmpty()) {
                // if there are no more results left in the queue, trigger calculation for the next result
                setNextResult();
            }
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " FLATTEN function",
                getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);
        _nextResults = new LinkedList<>();

        setNextResult();
    }

    public void setNextResult() {
        while (_iterator.hasNext()) {
            Item item = _iterator.next();
            List<Item> singleItemList = new ArrayList<>();
            singleItemList.add(item);
            flatten(singleItemList);
            if (!(_nextResults.isEmpty())) {
                break;
            }
        }
        if (_nextResults.isEmpty()) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }

    public void flatten(List<Item> items) {
        for (Item item:items) {
            if (item.isArray()) {
                flatten(item.getItems());
            }
            else {
                _nextResults.add(item);
            }
        }
    }
}
