package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidLexicalValueException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.AtomicItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemTypes;

import java.util.List;

public class IntegerFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item item = null;

    public IntegerFunctionIterator(
            List<RuntimeIterator> parameters,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(parameters, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            try {
                if (!this.item.isAtomic()) {
                    String message = String.format(
                        "Can not atomize an %1$s item: an %1$s has probably been passed where an atomic value is expected.",
                        ItemTypes.getItemTypeName(this.item.getClass().getSimpleName())
                    );
                    throw new NonAtomicKeyException(message, getMetadata());
                }

                AtomicItem atomicItem = (AtomicItem) this.item;
                String message = atomicItem.serialize()
                    +
                    ": value of type "
                    + ItemTypes.getItemTypeName(this.item.getClass().getSimpleName())
                    + " is not castable to type integer.";
                if (atomicItem.isNull()) {
                    throw new InvalidLexicalValueException(message, getMetadata());
                }
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
                    this.item.serialize(),
                    "string",
                    "integer"
                );
                throw new CastException(message, getMetadata());
            }
        } else {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " integer function",
                    getMetadata()
            );
        }
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.item = this.children.get(0).materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.item != null;
    }
}
