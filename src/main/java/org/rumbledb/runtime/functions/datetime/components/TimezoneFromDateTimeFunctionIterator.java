package org.rumbledb.runtime.functions.datetime.components;

import java.time.Duration;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class TimezoneFromDateTimeFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public TimezoneFromDateTimeFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item dateTimeItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (dateTimeItem == null || !dateTimeItem.hasTimeZone()) {
            return null;
        }
        return ItemFactory.getInstance().createDayTimeDurationItem(Duration.ofMinutes(dateTimeItem.getOffset()));
    }
}
