package sparksoniq.jsoniq.runtime.iterator.functions.strings;

import java.util.List;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class SubstringFunctionIterator extends LocalFunctionCallIterator {
    public SubstringFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            String result;
            StringItem stringItem = this.getSingleItemOfTypeFromIterator(this._children.get(0), StringItem.class);
            if (stringItem == null) {
                return new StringItem("", ItemMetadata.fromIteratorMetadata(getMetadata()));
            }
            IntegerItem indexItem = this.getSingleItemOfTypeFromIterator(this._children.get(1), IntegerItem.class);
            if (indexItem == null) {
                throw new UnexpectedTypeException("Type error; Start index parameter can't be empty sequence ", getMetadata());
            }
            int index = sanitizeIndexItem(indexItem);
            if (this._children.size() > 2) {
                IntegerItem endIndexItem = this.getSingleItemOfTypeFromIterator(this._children.get(2), IntegerItem.class);
                if (endIndexItem == null) {
                    throw new UnexpectedTypeException("Type error; End index parameter can't be empty sequence ", getMetadata());
                }
                int endIndex = sanitizeEndIndex(stringItem, endIndexItem, index);
                result = stringItem.getStringValue().substring(index, endIndex);
            } else {
                result = stringItem.getStringValue().substring(index);
            }

            return new StringItem(result, ItemMetadata.fromIteratorMetadata(getMetadata()));
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " substring function", getMetadata());
    }

    private int sanitizeEndIndex(StringItem stringItem, IntegerItem endIndexItem, int startIndex) {
        //char indexing starts from 1 in JSONiq
        return Math.min(stringItem.getStringValue().length(), startIndex + endIndexItem.getIntegerValue());
    }

    private int sanitizeIndexItem(IntegerItem indexItem) {
        //char indexing starts from 1 in JSONiq
        return indexItem.getIntegerValue() - 1 > 0 ? indexItem.getIntegerValue() - 1 : 0;
    }
}
