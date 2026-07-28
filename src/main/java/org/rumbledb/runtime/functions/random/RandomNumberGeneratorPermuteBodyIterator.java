package org.rumbledb.runtime.functions.random;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Body of the "permute" entry of a random-number-generator map: a seed-deterministic Fisher-Yates shuffle
 * of the bound "arg" parameter.
 */
public class RandomNumberGeneratorPermuteBodyIterator extends HybridRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    private final long seed;

    public RandomNumberGeneratorPermuteBodyIterator(long seed, RuntimeStaticContext staticContext) {
        super(List.of(), staticContext);
        this.seed = seed;
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> shuffledItems(context).iterator(), getMetadata());
    }

    private List<Item> shuffledItems(DynamicContext context) {
        List<Item> items = new ArrayList<>(
                context.getVariableValues()
                    .getLocalVariableValue(RandomNumberGeneratorMapBuilder.PERMUTE_PARAM_NAME, getMetadata())
        );
        Random random = new Random(this.seed);
        for (int i = items.size() - 1; i > 0; --i) {
            int j = random.nextInt(i + 1);
            Item temp = items.get(i);
            items.set(i, items.get(j));
            items.set(j, temp);
        }
        return items;
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException(
                "random-number-generator permute is currently supported only in local execution mode."
        );
    }
}
