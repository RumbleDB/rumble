package org.rumbledb.runtime.functions.datetime.components;

import java.time.Duration;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidTimezoneException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class AdjustDateToTimezone extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item timezone = null;

    public AdjustDateToTimezone(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item dateItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (this.children.size() == 2) {
            this.timezone = this.children.get(1)
                .materializeFirstItemOrNull(context);
        }
        if (dateItem == null) {
            return null;
        }
        if (this.timezone == null && this.children.size() == 1) {
            return ItemFactory.getInstance()
                .createDateItem(dateItem.getDateTimeValue(), true);
        }
        if (this.timezone == null) {
            if (dateItem.hasTimeZone()) {
                return ItemFactory.getInstance()
                    .createDateItem(dateItem.getDateTimeValue(), false);
            }
            return ItemFactory.getInstance()
                .createDateItem(dateItem.getDateTimeValue(), dateItem.hasTimeZone());
        } else {
            if (this.checkTimeZoneArgument()) {
                throw new InvalidTimezoneException("Invalid timezone", getMetadata());
            }
            Duration timezoneDuration = this.timezone.getDurationValue();
            int hours = (int) timezoneDuration.toHours();
            int minutes = (int) (timezoneDuration.toMinutes() % 60);

            if (dateItem.hasTimeZone()) {
                return ItemFactory.getInstance()
                    .createDateItem(
                        dateItem.getDateTimeValue().withHour(hours).withMinute(minutes),
                        true
                    );
            }
            return ItemFactory.getInstance()
                .createDateItem(
                    dateItem.getDateTimeValue().withHour(hours).withMinute(minutes),
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
