package org.rumbledb.runtime.functions.datetime.components;

import org.joda.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.List;

public class TimezoneFromDateFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item dateItem = null;

    public TimezoneFromDateFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.dateItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (this.dateItem == null || !this.dateItem.hasTimeZone()) {
            return null;
        }
        return ItemFactory.getInstance()
                .createDayTimeDurationItem(
                        new Period(this.dateItem.getDateTimeValue().getZone().toTimeZone().getRawOffset())
                );
    }
}
