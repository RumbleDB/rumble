package org.rumbledb.runtime.functions.datetime.components;

import org.rumbledb.runtime.plan.ItemRuntimePlan;


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

public class AdjustTimeToTimezone extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public AdjustTimeToTimezone(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return adjust(context);
    }

    private Item adjust(DynamicContext context) {
        Item timeItem = this.getChild(0).materializeFirstOrNull(context);
        if (timeItem == null) {
            return null;
        }
        Item timezone = this.getChildren().size() == 2 ? this.getChild(1).materializeFirstOrNull(context) : null;
        if (timezone == null && this.getChildren().size() == 1) {
            return ItemFactory.getInstance()
                .createTimeItem(timeItem.getTimeValue().withOffsetSameInstant(ZoneOffset.UTC), true);
        }
        if (timezone == null) {
            return ItemFactory.getInstance()
                .createTimeItem(timeItem.getTimeValue().withOffsetSameLocal(ZoneOffset.UTC), false);
        } else {
            if (checkTimeZoneArgument(timezone)) {
                throw new InvalidTimezoneException("Invalid timezone", getMetadata());
            }
            Duration timezoneDuration = timezone.getDurationValue();
            int hours = (int) timezoneDuration.toHours();
            int minutes = (int) timezoneDuration.toMinutes() % 60;
            if (timeItem.hasTimeZone()) {
                return ItemFactory.getInstance()
                    .createTimeItem(
                        timeItem.getTimeValue().withOffsetSameInstant(ZoneOffset.ofHoursMinutes(hours, minutes)),
                        true
                    );
            }
            return ItemFactory.getInstance()
                .createTimeItem(
                    timeItem.getTimeValue().withOffsetSameLocal(ZoneOffset.ofHoursMinutes(hours, minutes)),
                    true
                );
        }
    }

    private static boolean checkTimeZoneArgument(Item timezone) {
        Duration timezoneDuration = timezone.getDurationValue();
        return (Math.abs(timezoneDuration.toMinutes()) > 840) || (Double.compare(timezoneDuration.getNano(), 0) != 0);
    }
}
