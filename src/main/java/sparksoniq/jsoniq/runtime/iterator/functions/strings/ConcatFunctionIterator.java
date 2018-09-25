package sparksoniq.jsoniq.runtime.iterator.functions.strings;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class ConcatFunctionIterator extends LocalFunctionCallIterator {
    public ConcatFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            StringBuilder builder = new StringBuilder("");
            for (RuntimeIterator iterator : this._children) {
                StringItem stringItem = this.getSingleItemOfTypeFromIterator(iterator, StringItem.class);
                if (stringItem != null) {
                    builder.append(stringItem.getStringValue());
                }
            }
            this._hasNext = false;
            return new StringItem(builder.toString(), ItemMetadata.fromIteratorMetadata(getMetadata()));
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " substring function", getMetadata());
    }
}
