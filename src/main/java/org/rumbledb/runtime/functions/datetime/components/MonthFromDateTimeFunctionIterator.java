package org.rumbledb.runtime.functions.datetime.components;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.TemporalComponentFunctionIterator;

import java.io.Serial;
import java.util.List;

public class MonthFromDateTimeFunctionIterator extends TemporalComponentFunctionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public MonthFromDateTimeFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext, Component.MONTH);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item dateTimeItem = this.getChild(0)
            .materializeFirstOrNull(context);
        if (dateTimeItem == null) {
            return null;
        }
        return ItemFactory.getInstance().createIntItem(dateTimeItem.getMonth());
    }
}
