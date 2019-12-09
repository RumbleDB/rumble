package sparksoniq.jsoniq.runtime.iterator.functions.binaries;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.CastException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class Base64BinaryFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item _base64BinaryStringItem = null;

    public Base64BinaryFunctionIterator(
            List<RuntimeIterator> parameters,
            IteratorMetadata iteratorMetadata
    ) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            try {
                return ItemFactory.getInstance().createBase64BinaryItem(_base64BinaryStringItem.getStringValue());
            } catch (IllegalArgumentException e) {
                String message = String.format(
                    "\"%s\": value of type %s is not castable to type %s",
                    _base64BinaryStringItem.serialize(),
                    "string",
                    "base64Binary"
                );
                throw new CastException(message, getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " base64Binary function",
                    getMetadata()
            );
    }


    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _base64BinaryStringItem = this.getSingleItemFromIterator(
            this._children.get(0)
        );
        this._hasNext = _base64BinaryStringItem != null;
    }
}
