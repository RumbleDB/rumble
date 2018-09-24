package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import javax.naming.OperationNotSupportedException;
import java.util.*;

public class ObjectDescendantPairsFunctionIterator extends ObjectFunctionIterator {

    private RuntimeIterator _iterator;
    private Queue<Item> _nextResults;   // queue that holds the results created by the current item in inspection

    public ObjectDescendantPairsFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.DESCENDANTPAIRS, iteratorMetadata);
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
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " DESCENDANT-PAIRS function",
                getMetadata());
    }

    public void setNextResult() {
        while (_iterator.hasNext()) {
            Item item = _iterator.next();
            List<Item> singleItemList = new ArrayList<>();
            singleItemList.add(item);

            getDescendantPairs(singleItemList);
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

    public void getDescendantPairs(List<Item> items) {
        for (Item item:items) {
            try {
                if (item.isArray()) {
                    getDescendantPairs(item.getItems());
                } else if (item.isObject()) {
                    List<String> keys = item.getKeys();
                    for (String key : keys) {
                        Item value = item.getItemByKey(key);

                        List<String> keyList = Collections.singletonList(key);
                        List<Item> valueList = Collections.singletonList(value);

                        ObjectItem result = new ObjectItem(keyList, valueList, ItemMetadata.fromIteratorMetadata(getMetadata()));
                        _nextResults.add(result);
                        getDescendantPairs(valueList);
                    }
                } else {
                    // do nothing
                }
            } catch (OperationNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }
}
