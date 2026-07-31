package org.rumbledb.runtime.functions.datetime.components;


import java.io.Serial;
import java.time.Duration;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidTimezoneException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

import java.time.ZoneOffset;
import java.util.List;

public class AdjustDateToTimezone extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public AdjustDateToTimezone(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return adjust(context);
    }

    private Item adjust(DynamicContext context) {
        Item dateItem = this.getChild(0).materializeFirstOrNull(context);
        if (dateItem == null) {
            return null;
        }
        Item timezone = this.getChildren().size() == 2 ? this.getChild(1).materializeFirstOrNull(context) : null;
        if (timezone == null && this.getChildren().size() == 1) {
            return ItemFactory.getInstance()
                .createDateItem(dateItem.getDateTimeValue().withOffsetSameInstant(ZoneOffset.UTC), true);
        }
        if (timezone == null) {
            return ItemFactory.getInstance()
                .createDateItem(dateItem.getDateTimeValue().withOffsetSameLocal(ZoneOffset.UTC), false);
        } else {
            if (checkTimeZoneArgument(timezone)) {
                throw new InvalidTimezoneException("Invalid timezone", getMetadata());
            }
            Duration timezoneDuration = timezone.getDurationValue();
            int hours = (int) timezoneDuration.toHours();
            int minutes = (int) (timezoneDuration.toMinutes() % 60);

            if (dateItem.hasTimeZone()) {
                return ItemFactory.getInstance()
                    .createDateItem(
                        dateItem.getDateTimeValue().withOffsetSameInstant(ZoneOffset.ofHoursMinutes(hours, minutes)),
                        true
                    );
            }
            return ItemFactory.getInstance()
                .createDateItem(
                    dateItem.getDateTimeValue().withOffsetSameLocal(ZoneOffset.ofHoursMinutes(hours, minutes)),
                    true
                );
        }
    }

    private static boolean checkTimeZoneArgument(Item timezone) {
        Duration timezoneDuration = timezone.getDurationValue();
        return (Math.abs(timezoneDuration.toMinutes()) > 840) || (Double.compare(timezoneDuration.getNano(), 0) != 0);
    }
}
