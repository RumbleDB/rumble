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

public class AdjustDateTimeToTimezone extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item timeItem = null;
    private Item timezone = null;

    public AdjustDateTimeToTimezone(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.timeItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (this.children.size() == 2) {
            this.timezone = this.children.get(1)
                .materializeFirstItemOrNull(context);
        }
        if (this.timeItem == null) {
            return null;
        }
        if (this.timezone == null && this.children.size() == 1) {
            return ItemFactory.getInstance()
                .createDateItem(this.timeItem.getDateTimeValue().withZoneSameInstant(ZoneOffset.UTC), true);
        }
        if (this.timezone == null) {
            if (this.timeItem.hasTimeZone()) {
                return ItemFactory.getInstance()
                    .createDateTimeItem(
                        this.timeItem.getDateTimeValue()
                            .withZoneSameLocal(this.timeItem.getDateTimeValue().getZone()),
                        false
                    );
            }
            return ItemFactory.getInstance()
                .createDateTimeItem(this.timeItem.getDateTimeValue(), this.timeItem.hasTimeZone());
        } else {
            if (this.checkTimeZoneArgument()) {
                throw new InvalidTimezoneException("Invalid timezone", getMetadata());
            }
            Duration timezoneDuration = Duration.from(this.timezone.getPeriodValue());
            int hours = (int) timezoneDuration.toHours();
            int minutes = (int) (timezoneDuration.toMinutes() % 60);

            if (this.timeItem.hasTimeZone()) {
                return ItemFactory.getInstance()
                    .createDateTimeItem(
                        this.timeItem.getDateTimeValue()
                            .withZoneSameLocal(ZoneOffset.ofHoursMinutes(hours, minutes)),
                        true
                    );
            }
            return ItemFactory.getInstance()
                .createDateTimeItem(
                    this.timeItem.getDateTimeValue()
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
