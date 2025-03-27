package org.rumbledb.runtime.functions.datetime.components;

import java.time.Duration;
import java.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class TimezoneFromTimeFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item timeItem = null;

    public TimezoneFromTimeFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.timeItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (this.timeItem == null || !this.timeItem.hasTimeZone()) {
            return null;
        }
        return ItemFactory.getInstance()
            .createDayTimeDurationItem(
                Period.from(
                    Duration.ofSeconds(
                        this.timeItem.getDateTimeValue().getOffset().getTotalSeconds() * 1000L
                    )
                )
            );
    }

}
