package org.rumbledb.runtime.functions.datetime.components;

import java.io.Serial;
import java.time.Duration;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.TemporalComponentFunctionIterator;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class TimezoneFromDateTimeFunctionIterator extends TemporalComponentFunctionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public TimezoneFromDateTimeFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext, Component.TIMEZONE);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item dateTimeItem = this.getChild(0).materializeFirstOrNull(context);
        if (dateTimeItem == null || !dateTimeItem.hasTimeZone()) {
            return null;
        }
        return ItemFactory.getInstance().createDayTimeDurationItem(Duration.ofMinutes(dateTimeItem.getOffset()));
    }
}
