package org.rumbledb.runtime.functions.datetime;

import java.time.OffsetDateTime;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
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
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
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
        OffsetDateTime dt = null;
        OffsetDateTime dateDt = this.dateItem.getDateTimeValue();
        OffsetDateTime timeDt = this.timeItem.getDateTimeValue();

        if (this.dateItem.hasTimeZone() && this.timeItem.hasTimeZone()) {
            if (dateDt.getOffset() == timeDt.getOffset()) {
                dt = OffsetDateTime.of(
                    dateDt.toLocalDate(),
                    timeDt.toLocalTime(),
                    dateDt.getOffset()
                );
                return ItemFactory.getInstance().createDateTimeItem(dt, true);
            } else {
                throw new InconsistentTimezonesException(
                        "The two arguments have inconsistent timezones",
                        getMetadata()
                );
            }
        } else if (this.dateItem.hasTimeZone() && !this.timeItem.hasTimeZone()) {
            dt = OffsetDateTime.of(
                dateDt.toLocalDate(),
                timeDt.toLocalTime(),
                dateDt.getOffset()
            );
            return ItemFactory.getInstance().createDateTimeItem(dt, true);
        } else if (!this.dateItem.hasTimeZone() && this.timeItem.hasTimeZone()) {
            dt = OffsetDateTime.of(
                dateDt.toLocalDate(),
                timeDt.toLocalTime(),
                dateDt.getOffset()
            );
            return ItemFactory.getInstance().createDateTimeItem(dt, true);
        }
        dt = OffsetDateTime.of(
            dateDt.toLocalDate(),
            timeDt.toLocalTime(),
            dateDt.getOffset()
        );
        return ItemFactory.getInstance().createDateTimeItem(dt, false);

    }


}
