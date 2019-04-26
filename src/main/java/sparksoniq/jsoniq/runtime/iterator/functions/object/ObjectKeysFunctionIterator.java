package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemUtil;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ObjectKeysFunctionIterator extends ObjectFunctionIterator {

    private RuntimeIterator _iterator;
    private Queue<Item> _nextResults;   // queue that holds the results created by the current item in inspection
    private List<Item> _prevResults;

    public ObjectKeysFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.KEYS, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);
        _prevResults = new ArrayList<>();
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
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " KEYS function",
                getMetadata());
    }

    public void setNextResult() {
        while(_iterator.hasNext()) {
            Item item = _iterator.next();
            // ignore non-object items
            if (item.isObject()) {
                StringItem result;
                for (String key : item.getKeys()) {
                    result = new StringItem(key);
                    // check if key was met earlier
                    if (!ItemUtil.listContainsItem(_prevResults, result))
                    {
                        _prevResults.add(result);
                        _nextResults.add(result);
                    }
                }
                // if some results are found from the current item, break out of while loop
                if (_nextResults.isEmpty()) {
                    break;
                }
            }
        }

        if (_nextResults.isEmpty()) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }
}
