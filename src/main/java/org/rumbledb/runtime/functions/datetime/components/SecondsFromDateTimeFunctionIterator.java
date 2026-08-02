package org.rumbledb.runtime.functions.datetime.components;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.TemporalComponentFunctionIterator;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

public class SecondsFromDateTimeFunctionIterator extends TemporalComponentFunctionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public SecondsFromDateTimeFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext, Component.SECOND);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item dateTimeItem = this.getChild(0)
            .materializeFirstOrNull(context);
        if (dateTimeItem == null) {
            return null;
        }
        return ItemFactory.getInstance().createDecimalItem(BigDecimal.valueOf(dateTimeItem.getSecond()));
    }
}
