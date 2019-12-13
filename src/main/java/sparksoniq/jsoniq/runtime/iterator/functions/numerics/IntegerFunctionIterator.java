package sparksoniq.jsoniq.runtime.iterator.functions.numerics;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.CastException;
import sparksoniq.exceptions.InvalidLexicalValueException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemTypes;

import java.util.List;

public class IntegerFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item item = null;

    public IntegerFunctionIterator(
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
                if (!item.isAtomic()) {
                    String message = String.format(
                        "Can not atomize an %1$s item: an %1$s has probably been passed where an atomic value is expected.",
                        ItemTypes.getItemTypeName(item.getClass().getSimpleName())
                    );
                    throw new NonAtomicKeyException(message, getMetadata().getExpressionMetadata());
                }

                AtomicItem atomicItem = (AtomicItem) item;
                String message = atomicItem.serialize()
                    +
                    ": value of type "
                    + ItemTypes.getItemTypeName(item.getClass().getSimpleName())
                    + " is not castable to type integer.";
                if (atomicItem.isNull())
                    throw new InvalidLexicalValueException(message, getMetadata());
                if (atomicItem.isCastableAs(AtomicTypes.IntegerItem)) {
                    try {
                        return atomicItem.castAs(AtomicTypes.IntegerItem);
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
                    "integer"
                );
                throw new CastException(message, getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " integer function",
                    getMetadata()
            );
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        item = this.getSingleItemFromIterator(this._children.get(0));
        this._hasNext = item != null;
    }
}
