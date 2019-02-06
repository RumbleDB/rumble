package sparksoniq.jsoniq.runtime.iterator.functions.sequences.value;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class DeepEqualFunctionIterator extends LocalFunctionCallIterator {

    public DeepEqualFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;

            RuntimeIterator sequenceIterator1 = this._children.get(0);
            RuntimeIterator sequenceIterator2 = this._children.get(1);

            Item[] items1 = getItemsFromIteratorWithCurrentContext(sequenceIterator1);
            Item[] items2 = getItemsFromIteratorWithCurrentContext(sequenceIterator2);

            boolean res = checkDeepEqual(items1, items2);
            return new BooleanItem(res);
        } else {
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "deep-equal function", getMetadata());
        }
    }


    public boolean checkDeepEqual(Item[] items1, Item[] items2) {
        if (items1.length != items2.length) {
            return false;
        } else {
            for (int i = 0; i < items1.length; i++){
                Item item1 = items1[i];
                Item item2 = items2[i];

                if (item1 instanceof ArrayItem) {
                    // if item types don't match
                    if (!(item2 instanceof ArrayItem)) {
                        return false;
                    } else {
                        // if types match, recursively check if array is deep-equal
                        ArrayItem arrItem1 = (ArrayItem)item1;
                        ArrayItem arrItem2 = (ArrayItem)item2;

                        if (!checkDeepEqual(arrItem1.getItems(), arrItem2.getItems())) {
                            return false;
                        }
                    }
                } else if (item1 instanceof ObjectItem) {
                    // if item types don't match
                    if (!(item2 instanceof ObjectItem)) {
                        return false;
                    } else {
                        // if types match, recursively check if object is deep-equal
                        ObjectItem objItem1 = (ObjectItem)item1;
                        ObjectItem objItem2 = (ObjectItem)item2;

                        if (objItem1.getKeys().equals(objItem2.getKeys())) {
                            if (!checkDeepEqual(objItem1.getValues(), objItem2.getValues())) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                } else if (Item.compareItems(item1, item2) != 0){
                    // if atomic items' values are not equal
                    return false;
                } else {
                    // do nothing
                }
            }
            return true;
        }
    }
}
