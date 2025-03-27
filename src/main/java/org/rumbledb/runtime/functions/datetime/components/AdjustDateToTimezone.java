package org.rumbledb.runtime.functions.datetime.components;

import java.time.Duration;
import java.time.ZoneOffset;
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
    private Item dateItem = null;
    private Item timezone = null;

    public AdjustDateToTimezone(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.dateItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (this.children.size() == 2) {
            this.timezone = this.children.get(1)
                .materializeFirstItemOrNull(context);
        }
        if (this.dateItem == null) {
            return null;
        }
        if (this.timezone == null && this.children.size() == 1) {
            return ItemFactory.getInstance()
                .createDateItem(this.dateItem.getDateTimeValue().withZoneSameInstant(ZoneOffset.UTC), true);
        }
        if (this.timezone == null) {
            if (this.dateItem.hasTimeZone()) {
                return ItemFactory.getInstance()
                    .createDateItem(
                        this.dateItem.getDateTimeValue()
                            .withZoneSameLocal(this.dateItem.getDateTimeValue().getZone()),
                        false
                    );
            }
            return ItemFactory.getInstance()
                .createDateItem(this.dateItem.getDateTimeValue(), this.dateItem.hasTimeZone());
        } else {
            if (this.checkTimeZoneArgument()) {
                throw new InvalidTimezoneException("Invalid timezone", getMetadata());
            }
            Duration timezoneDuration = Duration.from(this.timezone.getPeriodValue());
            int hours = (int) timezoneDuration.toHours();
            int minutes = (int) (timezoneDuration.toMinutes() % 60);

            if (this.dateItem.hasTimeZone()) {
                return ItemFactory.getInstance()
                    .createDateItem(
                        this.dateItem.getDateTimeValue().withZoneSameInstant(ZoneOffset.ofHoursMinutes(hours, minutes)),
                        true
                    );
            }
            return ItemFactory.getInstance()
                .createDateItem(
                    this.dateItem.getDateTimeValue()
                        .withZoneSameLocal(ZoneOffset.ofHoursMinutes(hours, minutes)),
                    true
                );
        }
    }

    private boolean checkTimeZoneArgument() {
        Duration timezoneDuration = Duration.from(this.timezone.getPeriodValue());
        return (Math.abs(timezoneDuration.toMillis()) > 50400000)
            ||
            (Double.compare(
                timezoneDuration.getSeconds()
                    + (timezoneDuration.toMillis() / 1000.0),
                0
            ) != 0);
    }
}
