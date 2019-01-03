package sparksoniq.jsoniq.runtime.iterator.functions.strings;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import javax.naming.OperationNotSupportedException;
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
                Item item = this.getSingleItemOfTypeFromIterator(iterator, Item.class);
                // if not empty sequence
                if (item != null) {
                    String stringValue = "";
                    if (item.isAtomic()) {
                        stringValue = item.serialize();       // for atomic items (not array or object) returns the equivalent string value
                    } else {
                        throw new UnexpectedTypeException("String concat function has arguments that can't be converted to a string " +
                                item.serialize(), getMetadata());
                    }
                    if (stringValue != "") {
                        builder.append(stringValue);
                    }
                }
            }
            this._hasNext = false;
            return new StringItem(builder.toString(), ItemMetadata.fromIteratorMetadata(getMetadata()));
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " substring function", getMetadata());
    }
}
