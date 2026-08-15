package org.rumbledb.runtime.functions.datetime.components;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.TemporalComponentFunctionIterator;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class HoursFromTimeFunctionIterator extends TemporalComponentFunctionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public HoursFromTimeFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext, Component.HOUR);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item timeItem = this.getChild(0).materializeFirstOrNull(context);
        if (timeItem == null) {
            return null;
        }
        return ItemFactory.getInstance().createIntItem(timeItem.getHour());
    }
}
