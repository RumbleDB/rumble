package org.rumbledb.runtime.functions.random;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

import java.io.Serial;
import java.util.List;
import java.util.Random;

/**
 * Implementation based on W3C spec: <a href="https://www.w3.org/TR/xpath-functions-31/#random-numbers">...</a>
 */
public class RandomNumberGeneratorIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public RandomNumberGeneratorIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        return generate();
    }

    private static Item generate() {
        Random random = new Random();
        return ItemFactory.getInstance().createDoubleItem(random.nextDouble());
    }
}
