package sparksoniq.jsoniq.runtime.iterator.functions.numerics;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.*;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemTypes;

import java.util.List;

public class DoubleFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item item = null;

    public DoubleFunctionIterator(
            List<RuntimeIterator> parameters,
            IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override public Item next() {
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
                String message = atomicItem.serialize() +
                        ": value of type "
                        + ItemTypes.getItemTypeName(item.getClass().getSimpleName())
                        + " is not castable to type double.";
                if (atomicItem.isNull())
                    throw new InvalidLexicalValueException(message, getMetadata());
                if (atomicItem.isCastableAs(AtomicTypes.DoubleItem)) {
                    try {
                        return atomicItem.castAs(AtomicTypes.DoubleItem);
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
                        "double"
                );
                throw new CastException(message, getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " double function",
                    getMetadata()
            );
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        try {
            item = this.getSingleItemOfTypeFromIterator(
                    this._children.get(0),
                    Item.class,
                    new UnknownFunctionCallException("double", this._children.size(), getMetadata())
            );
        } catch (UnknownFunctionCallException e) {
            throw new UnexpectedTypeException(
                    " Sequence of more than one item can not be cast to type with quantifier '1' or '?'",
                    getMetadata()
            );
        }
        this._hasNext = item != null;
    }
}
