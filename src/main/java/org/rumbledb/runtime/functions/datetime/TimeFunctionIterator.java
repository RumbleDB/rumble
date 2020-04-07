package org.rumbledb.runtime.functions.datetime;

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

public class TimeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item timeStringItem = null;

    public TimeFunctionIterator(
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
                return ItemFactory.getInstance().createTimeItem(this.timeStringItem.getStringValue());
            } catch (UnsupportedOperationException | IllegalArgumentException e) {
                String message = String.format(
                    "\"%s\": value of type %s is not castable to type %s",
                    this.timeStringItem.serialize(),
                    "string",
                    "time"
                );
                throw new CastException(message, getMetadata());
            }
        } else {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " time function",
                    getMetadata()
            );
        }
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.timeStringItem = this.children.get(0)
            .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.timeStringItem != null;
    }
}
