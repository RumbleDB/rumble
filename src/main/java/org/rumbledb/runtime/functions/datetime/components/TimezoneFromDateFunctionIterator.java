package org.rumbledb.runtime.functions.datetime.components;

import java.time.Duration;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class TimezoneFromDateFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item dateItem = null;

    public TimezoneFromDateFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.dateItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (this.dateItem == null || !this.dateItem.hasTimeZone()) {
            return null;
        }
        return ItemFactory.getInstance().createDayTimeDurationItem(Duration.ofMinutes(this.dateItem.getOffset()));
    }
}
