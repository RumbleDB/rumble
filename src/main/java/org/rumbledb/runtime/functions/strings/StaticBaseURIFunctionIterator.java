package org.rumbledb.runtime.functions.strings;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class StaticBaseURIFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public StaticBaseURIFunctionIterator(List<ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return ItemFactory.getInstance().createAnyURIItem(this.staticContext.getStaticURIString());
    }
}
