package org.rumbledb.runtime.functions.datetime;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class TimeInMillis extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public TimeInMillis(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return currentTime();
    }

    private Item currentTime() {
        return ItemFactory.getInstance().createLongItem(System.currentTimeMillis());
    }
}
