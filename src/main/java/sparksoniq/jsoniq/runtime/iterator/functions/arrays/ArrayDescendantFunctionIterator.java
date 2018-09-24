package sparksoniq.jsoniq.runtime.iterator.functions.arrays;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ArrayDescendantFunctionIterator extends ArrayFunctionIterator {

    private RuntimeIterator _iterator;
    private Queue<Item> _nextResults;   // queue that holds the results created by the current item in inspection


    public ArrayDescendantFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ArrayFunctionOperators.DESCENDANT, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);
        _nextResults = new LinkedList<>();

        setNextResult();
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item result = _nextResults.remove();  // save the result to be returned
            if (_nextResults.isEmpty()) {
                // if there are no more results left in the queue, trigger calculation for the next result
                setNextResult();
            }
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " DESCENDANT-ARRAYS function",
                getMetadata());
    }

    public void setNextResult() {
        while (_iterator.hasNext()) {
            Item item = _iterator.next();
            List<Item> singleItemList = new ArrayList<>();
            singleItemList.add(item);

            getDescendantArrays(singleItemList);
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

    public void getDescendantArrays(List<Item> items) {
        for (Item item:items) {
            try {
                if (item.isArray()) {
                    _nextResults.add(item);
                    getDescendantArrays(item.getItems());
                } else if (item.isObject()) {
                    getDescendantArrays((List<Item>) item.getValues());
                } else {
                    // for atomic types: do nothing
                }
            } catch (OperationNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }
}
