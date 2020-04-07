package org.rumbledb.runtime.functions.durations;

import org.joda.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.DurationItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.types.ItemType;

import sparksoniq.jsoniq.ExecutionMode;

import java.util.List;

public class DayTimeDurationFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item durationStringItem = null;

    public DayTimeDurationFunctionIterator(
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
                Period period = DurationItem.getDurationFromString(
                    this.durationStringItem.getStringValue(),
                    ItemType.dayTimeDurationItem
                );
                return ItemFactory.getInstance().createDayTimeDurationItem(period);
            } catch (UnsupportedOperationException | IllegalArgumentException e) {
                String message = String.format(
                    "\"%s\": value of type %s is not castable to type %s",
                    this.durationStringItem.serialize(),
                    "string",
                    "dayTimeDuration"
                );
                throw new CastException(message, getMetadata());
            }
        } else {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " dayTimeDuration function",
                    getMetadata()
            );
        }
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.durationStringItem = this.children.get(0)
            .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.durationStringItem != null;
    }
}
