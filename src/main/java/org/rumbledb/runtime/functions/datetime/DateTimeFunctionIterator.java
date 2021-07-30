package org.rumbledb.runtime.functions.datetime;

import org.joda.time.DateTime;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class DateTimeFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item dateItem = null;
    private Item timeItem = null;

    public DateTimeFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.dateItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        this.timeItem = this.children.get(1)
            .materializeFirstItemOrNull(context);
        if (this.dateItem == null || this.timeItem == null) {
            return null;
        }
        DateTime dt = new DateTime(this.dateItem.getDateTimeValue()).withTime(
            this.timeItem.getDateTimeValue().toLocalTime()
        );
        if (this.dateItem.hasTimeZone() && this.timeItem.hasTimeZone()) {
            if (this.dateItem.getDateTimeValue().getZone() == this.timeItem.getDateTimeValue().getZone()) {
                return ItemFactory.getInstance().createDateTimeItem(dt, true);
            } else {
                throw new InvalidTimezoneException("The two arguments have inconsistent timezones", getMetadata());
            }
        } else if (this.dateItem.hasTimeZone() && !this.timeItem.hasTimeZone()) {
            dt = dt.withZone(this.dateItem.getDateTimeValue().getZone());
            return ItemFactory.getInstance().createDateTimeItem(dt, true);
        } else if (!this.dateItem.hasTimeZone() && this.timeItem.hasTimeZone()) {
            dt = dt.withZone(this.dateItem.getDateTimeValue().getZone());
            return ItemFactory.getInstance().createDateTimeItem(dt, true);
        }

        return ItemFactory.getInstance().createDateTimeItem(dt, false);

    }


}
