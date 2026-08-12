package org.rumbledb.runtime.functions.datetime;

import java.io.Serial;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class DateTimeFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan dateIterator;
    private final ItemRuntimePlan timeIterator;

    public DateTimeFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        this.dateIterator = arguments.get(0);
        this.timeIterator = arguments.get(1);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item dateItem = this.dateIterator.materializeFirstOrNull(context);
        Item timeItem = this.timeIterator.materializeFirstOrNull(context);
        if (dateItem == null || timeItem == null) {
            return null;
        }
        return evaluate(dateItem, timeItem, getMetadata());
    }

    private static Item evaluate(Item dateItem, Item timeItem, ExceptionMetadata metadata) {
        OffsetDateTime dt;
        OffsetDateTime dateDt = dateItem.getDateTimeValue();
        OffsetTime timeDt = timeItem.getTimeValue();

        if (dateItem.hasTimeZone() && timeItem.hasTimeZone()) {
            if (dateDt.getOffset() == timeDt.getOffset()) {
                dt = OffsetDateTime.of(dateDt.toLocalDate(), timeDt.toLocalTime(), dateDt.getOffset());
                return ItemFactory.getInstance().createDateTimeItem(dt, true);
            } else {
                throw new InconsistentTimezonesException("The two arguments have inconsistent timezones", metadata);
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
