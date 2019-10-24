package sparksoniq.jsoniq.runtime.iterator.functions.binaries;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.CastException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class Base64BinaryFunctionIterator extends LocalFunctionCallIterator {
    public Base64BinaryFunctionIterator(
            List<RuntimeIterator> parameters,
            IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;

            StringItem stringItem = this.getSingleItemOfTypeFromIterator(
                    this._children.get(0),
                    StringItem.class);

            if (stringItem == null) {
                throw new UnknownFunctionCallException("base64Binary", 0, getMetadata());
            }

            try {
                return ItemFactory.getInstance().createBase64BinaryItem(stringItem.getStringValue());
            } catch (IllegalArgumentException e) {
                String message = String.format("\"%s\": value of type %s is not castable to type %s",
                        stringItem.serialize(), "string", "base64Binary");
                throw new CastException(message, getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " base64Binary function",
                    getMetadata());
    }
}
