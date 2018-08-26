package sparksoniq.jsoniq.runtime.iterator.functions.object;

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

public class ObjectRemoveKeysFunctionIterator extends ObjectFunctionIterator {
    public ObjectRemoveKeysFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.REMOVEKEYS, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            return getResult();
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " REMOVE-KEYS function",
                getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;
        _currentIndex = 0;
        results = new ArrayList<>();
        RuntimeIterator sequenceIterator = this._children.get(0);
        RuntimeIterator keysIterator = this._children.get(1);
        List<Item> items = getItemsFromIteratorWithCurrentContext(sequenceIterator);
        List<Item> keys = getItemsFromIteratorWithCurrentContext(keysIterator);
        removeKeys(items, keys);

        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    public void removeKeys(List<Item> items, List<Item> keysRemoveItems) {
        for (Item item : items) {
            if (item.isObject()) {
                ArrayList<String> finalKeylist = new ArrayList<>();
                ArrayList<Item> finalValueList = new ArrayList<>();
                ArrayList<String> keysToRemove = new ArrayList<>();

                for (Item keyRemoveItem : keysRemoveItems) {
                    try {
                        String keyToRemove = keyRemoveItem.getStringValue();
                        keysToRemove.add(keyToRemove);
                    } catch (OperationNotSupportedException e) {
                        throw new UnexpectedTypeException("Project function has non-string key args.", getMetadata());
                    }
                }

                try {
                    for (String objectKey : item.getKeys()) {
                        if (!keysToRemove.contains(objectKey)) {
                            finalKeylist.add(objectKey);
                            finalValueList.add(item.getItemByKey(objectKey));
                        }
                    }
                } catch (OperationNotSupportedException e) {
                    e.printStackTrace();
                }

                results.add(new ObjectItem(finalKeylist, finalValueList, ItemMetadata.fromIteratorMetadata(getMetadata())));
            } else {
                results.add(item);
            }
        }

    }
}
