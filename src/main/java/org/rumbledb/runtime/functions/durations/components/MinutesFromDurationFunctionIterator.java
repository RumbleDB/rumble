package org.rumbledb.runtime.functions.durations.components;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.TemporalComponentFunctionIterator;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class MinutesFromDurationFunctionIterator extends TemporalComponentFunctionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public MinutesFromDurationFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext, Component.MINUTE);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item durationItem = this.getChild(0).materializeFirstOrNull(context);
        if (durationItem == null) {
            return null;
        }
        return ItemFactory.getInstance().createIntItem(durationItem.getMinute());
    }
}
