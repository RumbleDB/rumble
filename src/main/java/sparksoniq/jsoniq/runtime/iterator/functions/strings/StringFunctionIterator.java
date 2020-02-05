package sparksoniq.jsoniq.runtime.iterator.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.StringOfJSONiqItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemTypes;

import java.util.List;

public class StringFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item item = null;

    public StringFunctionIterator(
            List<RuntimeIterator> parameters,
            ExecutionMode executionMode,
            IteratorMetadata iteratorMetadata
    ) {
        super(parameters, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            try {
                if (!item.isAtomic()) {
                    throw new StringOfJSONiqItemException(
                            ItemTypes.getItemTypeName(item.getClass().getSimpleName())
                                +
                                " items do not have string value",
                            getMetadata()
                    );
                }
                AtomicItem atomicItem = (AtomicItem) item;
                String message = atomicItem.serialize()
                    +
                    ": value of type "
                    + ItemTypes.getItemTypeName(item.getClass().getSimpleName())
                    + " is not castable to type string.";
                if (atomicItem.isCastableAs(AtomicTypes.StringItem)) {
                    try {
                        return atomicItem.castAs(AtomicTypes.StringItem);
                    } catch (ClassCastException e) {
                        throw new UnexpectedTypeException(message, getMetadata());
                    }

                }
                throw new UnexpectedTypeException(message, getMetadata());
            } catch (IllegalArgumentException e) {
                String message = String.format(
                    "\"%s\": value of type %s is not castable to type %s",
                    item.serialize(),
                    "string",
                    "string"
                );
                throw new CastException(message, getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " string function",
                    getMetadata()
            );
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        item = this._children.get(0).materializeFirstItemOrNull(_currentDynamicContextForLocalExecution);
        this._hasNext = item != null;
    }
}
