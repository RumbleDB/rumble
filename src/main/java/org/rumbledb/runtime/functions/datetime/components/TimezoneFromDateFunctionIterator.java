package org.rumbledb.runtime.functions.datetime.components;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import java.io.Serial;
import java.time.Duration;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.TemporalComponentFunctionIterator;

import java.util.List;

public class TimezoneFromDateFunctionIterator extends TemporalComponentFunctionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public TimezoneFromDateFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext, Component.TIMEZONE);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item dateItem = this.getChild(0).materializeFirstOrNull(context);
        if (dateItem == null || !dateItem.hasTimeZone()) {
            return null;
        }
        return ItemFactory.getInstance().createDayTimeDurationItem(Duration.ofMinutes(dateItem.getOffset()));
    }
}
