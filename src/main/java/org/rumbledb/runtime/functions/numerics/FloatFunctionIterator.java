package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidLexicalValueException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.types.ItemType;

import java.util.List;

public class FloatFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item item = null;

    public FloatFunctionIterator(
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
                        this.item.getDynamicType().toString()
                    );
                    throw new NonAtomicKeyException(message, getMetadata());
                }
                String message = this.item.serialize()
                    +
                    ": value of type "
                    + this.item.getDynamicType().toString()
                    + " is not castable to type float.";
                if (this.item.isNull()) {
                    throw new InvalidLexicalValueException(message, getMetadata());
                }
                if (this.item.isCastableAs(ItemType.floatItem)) {
                    try {
                        return this.item.castAs(ItemType.floatItem);
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
                    "float"
                );
                throw new CastException(message, getMetadata());
            }
        } else {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " float function",
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
