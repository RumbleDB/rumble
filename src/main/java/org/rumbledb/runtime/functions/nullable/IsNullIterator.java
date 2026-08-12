package org.rumbledb.runtime.functions.nullable;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

import java.io.Serial;
import java.util.List;

public class IsNullIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public IsNullIterator(
            List<ItemRuntimePlan> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        List<Item> items = this.getChild(0).materialize(context);
        if (items.isEmpty()) {
            return ItemFactory.getInstance().createBooleanItem(true);
        }
        if (items.size() > 1) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        return ItemFactory.getInstance().createBooleanItem(items.get(0).isNull());
    }


}
