package org.rumbledb.runtime.functions.datetime.components;

import java.time.Duration;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidTimezoneException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.time.ZoneOffset;
import java.util.List;

public class AdjustTimeToTimezone extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item timezone = null;

    public AdjustTimeToTimezone(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item timeItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (this.children.size() == 2) {
            this.timezone = this.children.get(1)
                .materializeFirstItemOrNull(context);
        }
        if (timeItem == null) {
            return null;
        }
        if (this.timezone == null && this.children.size() == 1) {
            return ItemFactory.getInstance()
                .createTimeItem(timeItem.getTimeValue().withOffsetSameInstant(ZoneOffset.UTC), true);
        }
        if (this.timezone == null) {
            if (timeItem.hasTimeZone()) {
                return ItemFactory.getInstance()
                    .createTimeItem(timeItem.getTimeValue().withOffsetSameLocal(ZoneOffset.UTC), false);
            }
            return ItemFactory.getInstance()
                .createTimeItem(timeItem.getTimeValue().withOffsetSameLocal(ZoneOffset.UTC), timeItem.hasTimeZone());
        } else {
            if (this.checkTimeZoneArgument()) {
                throw new InvalidTimezoneException("Invalid timezone", getMetadata());
            }
            Duration timezoneDuration = this.timezone.getDurationValue();
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

    private boolean checkTimeZoneArgument() {
        Duration timezoneDuration = this.timezone.getDurationValue();
        return (Math.abs(timezoneDuration.toMinutes()) > 840)
            || (Double.compare(timezoneDuration.getNano(), 0) != 0);
    }
}
