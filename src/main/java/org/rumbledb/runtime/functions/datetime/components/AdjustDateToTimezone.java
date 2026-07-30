package org.rumbledb.runtime.functions.datetime.components;

import java.io.Serial;
import java.time.Duration;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidTimezoneException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.time.ZoneOffset;
import java.util.List;

public class AdjustDateToTimezone extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public AdjustDateToTimezone(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ComputedLocalCursor.fromArguments(this.getChildren(), context, this::adjust, getMetadata());
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return adjust(
            ComputedLocalCursor.arguments(
                this.getChildren().size(),
                index -> this.getChild(index).materializeFirstItemOrNull(context)
            )
        );
    }

    private Item adjust(ComputedLocalCursor.Arguments<Item> arguments) {
        Item dateItem = arguments.get(0);
        if (dateItem == null) {
            return null;
        }
        Item timezone = arguments.size() == 2 ? arguments.get(1) : null;
        if (timezone == null && arguments.size() == 1) {
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
