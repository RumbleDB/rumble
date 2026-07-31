package org.rumbledb.runtime.functions.durations.components;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.TemporalComponentFunctionIterator;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;

public class HoursFromDurationFunctionIterator extends TemporalComponentFunctionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public HoursFromDurationFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext, Component.HOUR);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item durationItem = this.getChild(0)
            .materializeFirstOrNull(context);
        if (durationItem == null) {
            return null;
        }
        return ItemFactory.getInstance().createIntItem(durationItem.getHour());

    }

}
