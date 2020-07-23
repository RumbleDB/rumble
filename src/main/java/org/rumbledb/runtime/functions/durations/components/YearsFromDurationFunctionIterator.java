package org.rumbledb.runtime.functions.durations.components;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.List;

public class YearsFromDurationFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item durationItem = null;

    public YearsFromDurationFunctionIterator(
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
            return ItemFactory.getInstance().createIntegerItem(this.durationItem.getDurationValue().getYears());
        } else {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " years-from-duration function",
                    getMetadata()
            );
        }
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.durationItem = this.children.get(0)
            .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.durationItem != null;
    }
}
