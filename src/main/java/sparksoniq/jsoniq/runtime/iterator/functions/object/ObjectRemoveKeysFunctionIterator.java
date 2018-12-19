package sparksoniq.jsoniq.runtime.iterator.functions.object;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import sparksoniq.exceptions.InvalidSelectorException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

public class ObjectRemoveKeysFunctionIterator extends ObjectFunctionIterator {

    private RuntimeIterator _iterator;
    private Item _nextResult;
    private List<String> _removalKeys;

    public ObjectRemoveKeysFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.REMOVEKEYS, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);

        List<Item> removalKeys = getItemsFromIteratorWithCurrentContext(this._children.get(1));
        if (removalKeys.isEmpty()) {
            throw new InvalidSelectorException("Invalid Key Removal Parameter; Object key removal can't be performed with zero keys: "
                    , getMetadata());
        }
        _removalKeys = new ArrayList<>();
        for (Item removalKeyItem : removalKeys) {
            try {
                String removalKey = removalKeyItem.getStringValue();
                _removalKeys.add(removalKey);
            } catch (OperationNotSupportedException e) {
                throw new UnexpectedTypeException("Remove-keys function has non-string key args.", getMetadata());
            }
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
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " REMOVE-KEYS function",
                getMetadata());
    }

    public void setNextResult() {
        _nextResult = null;

        if (_iterator.hasNext()) {
            Item item = _iterator.next();
            if (item instanceof ObjectItem) {
                ObjectItem objItem = (ObjectItem) item;
                _nextResult = removeKeys(objItem, _removalKeys);
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

    public ObjectItem removeKeys(ObjectItem objItem, List<String> removalKeys) {
        ArrayList<String> finalKeylist = new ArrayList<>();
        ArrayList<Item> finalValueList = new ArrayList<>();

        for (String objectKey : objItem.getKeys()) {
            if (!removalKeys.contains(objectKey)) {
                finalKeylist.add(objectKey);
                finalValueList.add(objItem.getItemByKey(objectKey));
            }
        }
        return new ObjectItem(finalKeylist, finalValueList, ItemMetadata.fromIteratorMetadata(getMetadata()));
    }
}
