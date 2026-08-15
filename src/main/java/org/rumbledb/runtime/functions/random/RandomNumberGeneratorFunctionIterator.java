package org.rumbledb.runtime.functions.random;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class RandomNumberGeneratorFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public RandomNumberGeneratorFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        long seed;
        Item seedItem = this.getChildren().isEmpty() ? null : this.getChild(0).materializeFirstOrNull(context);
        if (seedItem == null) {
            // No seed (or an empty-sequence seed) means an implementation-dependent default. Per F&O 3.1
            // 4.9.1, calling the function twice with the same (here: no) arguments within a single
            // execution scope must produce the same result, so the default seed is derived from the
            // RumbleRuntimeConfiguration instance, which is shared by every call within one query
            // execution and freshly created per execution.
            seed = System.identityHashCode(context.getRumbleConfiguration());
        } else {
            seed = seedItem.getStringValue().hashCode();
        }
        return RandomNumberGeneratorMapBuilder.build(
                seed, this.staticContext, new DynamicContext(context.getRumbleConfiguration()), getMetadata());
    }
}
