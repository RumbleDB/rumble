package org.rumbledb.runtime.functions.binaries;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.util.List;

public class HexBinaryFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item hexBinaryStringItem = null;

    public HexBinaryFunctionIterator(
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
            try {
                return ItemFactory.getInstance().createHexBinaryItem(this.hexBinaryStringItem.getStringValue());
            } catch (IllegalArgumentException e) {
                String message = String.format(
                    "\"%s\": value of type %s is not castable to type %s",
                    this.hexBinaryStringItem.serialize(),
                    "string",
                    "hexBinary"
                );
                throw new CastException(message, getMetadata());
            }
        } else {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " hexBinary function",
                    getMetadata()
            );
        }
    }


    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.hexBinaryStringItem = this.children.get(0)
            .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.hexBinaryStringItem != null;
    }
}
