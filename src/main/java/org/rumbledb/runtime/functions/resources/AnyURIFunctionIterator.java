package org.rumbledb.runtime.functions.resources;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.AtomicItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.ExecutionMode;

import org.rumbledb.types.ItemType;


import java.util.List;

public class AnyURIFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item anyItem = null;

    public AnyURIFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            AtomicItem atomicItem = (AtomicItem) this.anyItem;
            String message;
            if (atomicItem.isAnyURI()) {
                return atomicItem.castAs(ItemType.anyURIItem);
            } else if (atomicItem.isString()) {
                try {
                    return ItemFactory.getInstance().createAnyURIItem(atomicItem.getStringValue());
                } catch (IllegalArgumentException e) {
                    message = String.format(
                        "\"%s\": value of type String is not castable to type anyURI",
                        this.anyItem.serialize()
                    );
                    throw new CastException(message, getMetadata());
                }
            }
            message = String.format(
                "\"%s\": value of type %s is not castable to type anyURI",
                atomicItem.serialize(),
                atomicItem.getDynamicType().getName()
            );
            throw new CastException(message, getMetadata());
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " anyURI function",
                    getMetadata()
            );
    }


    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.anyItem = this.children.get(0)
            .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.anyItem != null;
    }
}
