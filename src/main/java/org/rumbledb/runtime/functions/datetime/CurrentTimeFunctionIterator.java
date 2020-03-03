package org.rumbledb.runtime.functions.datetime;

import org.joda.time.DateTime;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.TimeItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import sparksoniq.jsoniq.ExecutionMode;

import java.util.List;

public class CurrentTimeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public CurrentTimeFunctionIterator(
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
            return ItemFactory.getInstance().createTimeItem(new DateTime(), true);
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " current-time function",
                    getMetadata()
            );
    }
}
