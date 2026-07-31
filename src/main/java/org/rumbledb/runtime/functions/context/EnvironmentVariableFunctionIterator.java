package org.rumbledb.runtime.functions.context;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;

public class EnvironmentVariableFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public EnvironmentVariableFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return evaluate(this.getChild(0).materializeFirstOrNull(context));
    }

    private static Item evaluate(Item nameItem) {
        String value = System.getenv(nameItem.getStringValue());
        if (value == null) {
            return null;
        }
        return ItemFactory.getInstance().createStringItem(value);
    }
}
