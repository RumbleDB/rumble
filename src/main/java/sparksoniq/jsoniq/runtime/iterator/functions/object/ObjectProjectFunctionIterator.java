package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.InvalidSelectorException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class ObjectProjectFunctionIterator extends ObjectFunctionIterator {

    private RuntimeIterator _iterator;
    private Item _nextResult;
    private List<Item> _projKeys;

    public ObjectProjectFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.PROJECT, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);

        _projKeys = getItemsFromIteratorWithCurrentContext(this._children.get(1));
        if (_projKeys.isEmpty()) {
            throw new InvalidSelectorException("Invalid Projection Key; Object projection can't be performed with zero keys: "
                    , getMetadata());
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
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " PROJECT function",
                getMetadata());
    }

    public void setNextResult() {
        _nextResult = null;

        if (_iterator.hasNext()) {
            Item item = _iterator.next();
            if (item instanceof ObjectItem) {
                ObjectItem objItem = (ObjectItem) item;
                _nextResult = getProjection(objItem, _projKeys);
            } else {
                _nextResult = item;
            }
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }

    public ObjectItem getProjection(ObjectItem objItem, List<Item> keys) {
        ArrayList<String> finalKeylist = new ArrayList<>();
        ArrayList<Item> finalValueList = new ArrayList<>();
        for (Item keyItem : keys) {
            try {
                String key = keyItem.getStringValue();
                Item value = objItem.getItemByKey(key);
                if (value != null) {
                    finalKeylist.add(key);
                    finalValueList.add(value);
                }
            } catch (OperationNotSupportedException e) {
                throw new UnexpectedTypeException("Project function has non-string key args.", getMetadata());
            }
        }
        return new ObjectItem(finalKeylist, finalValueList, ItemMetadata.fromIteratorMetadata(getMetadata()));

    }
}
