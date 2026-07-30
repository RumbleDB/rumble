package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.List;
import java.util.Random;

/**
 * Body of the "next" entry of a random-number-generator map: deterministically derives the next seed in
 * the chain and builds a fresh three-entry map from it.
 */
public class RandomNumberGeneratorNextBodyIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    private final long seed;

    public RandomNumberGeneratorNextBodyIterator(long seed, RuntimeStaticContext staticContext) {
        super(List.of(), staticContext);
        this.seed = seed;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(() -> materializeFirstItemOrNull(context), getMetadata());
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        long nextSeed = new Random(this.seed).nextLong();
        return RandomNumberGeneratorMapBuilder.build(
            nextSeed,
            this.staticContext,
            new DynamicContext(context.getRumbleRuntimeConfiguration()),
            getMetadata()
        );
    }
}
