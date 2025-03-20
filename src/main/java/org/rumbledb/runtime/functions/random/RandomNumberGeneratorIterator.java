package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;
import java.util.Random;

/**
 * Implementation based on W3C spec: <a href="https://www.w3.org/TR/xpath-functions-31/#random-numbers">...</a>
 */
public class RandomNumberGeneratorIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public RandomNumberGeneratorIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        Random random = new Random();
        return ItemFactory.getInstance().createDoubleItem(random.nextDouble());
    }
}
