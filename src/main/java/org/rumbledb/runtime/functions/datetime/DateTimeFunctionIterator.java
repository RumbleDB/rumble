package org.rumbledb.runtime.functions.datetime;

import java.time.OffsetDateTime;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.time.OffsetTime;
import java.util.List;

public class DateTimeFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public DateTimeFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item dateItem = this.children.get(0).materializeFirstItemOrNull(context);
        Item timeItem = this.children.get(1).materializeFirstItemOrNull(context);
        if (dateItem == null || timeItem == null) {
            return null;
        }
        OffsetDateTime dt;
        OffsetDateTime dateDt = dateItem.getDateTimeValue();
        OffsetTime timeDt = timeItem.getTimeValue();

        if (dateItem.hasTimeZone() && timeItem.hasTimeZone()) {
            if (dateDt.getOffset() == timeDt.getOffset()) {
                dt = OffsetDateTime.of(dateDt.toLocalDate(), timeDt.toLocalTime(), dateDt.getOffset());
                return ItemFactory.getInstance().createDateTimeItem(dt, true);
            } else {
                throw new InconsistentTimezonesException(
                        "The two arguments have inconsistent timezones",
                        getMetadata()
                );
            }
        } else if (dateItem.hasTimeZone() && !timeItem.hasTimeZone()) {
            dt = OffsetDateTime.of(dateDt.toLocalDate(), timeDt.toLocalTime(), dateDt.getOffset());
            return ItemFactory.getInstance().createDateTimeItem(dt, true);
        } else if (!dateItem.hasTimeZone() && timeItem.hasTimeZone()) {
            dt = OffsetDateTime.of(dateDt.toLocalDate(), timeDt.toLocalTime(), timeDt.getOffset());
            return ItemFactory.getInstance().createDateTimeItem(dt, true);
        }
        dt = OffsetDateTime.of(dateDt.toLocalDate(), timeDt.toLocalTime(), dateDt.getOffset());
        return ItemFactory.getInstance().createDateTimeItem(dt, false);
    }
}
