package org.rumbledb.runtime.functions.random;


import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.List;

public class RandomSequenceWithBoundsAndSeedIterator extends LocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
    private Item low;
    private Item high;
    private Item type;
    private int seed;
    private int size;
    private GeneratedRandomsIterator generatedRandomsIterator;

    public RandomSequenceWithBoundsAndSeedIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> createRandomNumberStream(
                    context
                ),
                getMetadata()
        );
    }

    @Override
    public void open(DynamicContext context) {
        this.low = this.getChild(0).materializeFirstOrNull(context);
        this.high = this.getChild(1).materializeFirstOrNull(context);
        this.size = this.getChild(2).materializeFirstOrNull(context).castToIntValue();
        this.type = this.getChild(3).materializeFirstOrNull(context);
        this.seed = this.getChild(4).materializeFirstOrNull(context).castToIntValue();
        this.generatedRandomsIterator = createRandomNumberStream(
            this.low,
            this.high,
            this.size,
            this.type,
            this.seed
        );
    }

    private GeneratedRandomsIterator createRandomNumberStream(DynamicContext context) {
        return createRandomNumberStream(
            this.getChild(0).materializeFirstOrNull(context),
            this.getChild(1).materializeFirstOrNull(context),
            this.getChild(2).materializeFirstOrNull(context).castToIntValue(),
            this.getChild(3).materializeFirstOrNull(context),
            this.getChild(4).materializeFirstOrNull(context).castToIntValue()
        );
    }

    private GeneratedRandomsIterator createRandomNumberStream(Item low, Item high, int size, Item type, int seed) {
        if (type.getStringValue().equals("integer")) {
            return new GeneratedRandomIntegersIterator(
                    size,
                    low.castToIntValue(),
                    high.castToIntValue(),
                    seed
            );
        } else {
            // Generate doubles otherwise
            return new GeneratedRandomDoublesIterator(
                    size,
                    low.castToDoubleValue(),
                    high.castToDoubleValue(),
                    seed
            );
        }
    }

    @Override
    public Item next() {
        return this.generatedRandomsIterator.getNextRandom();
    }

    @Override
    public boolean hasNext() {
        return this.generatedRandomsIterator.hasNext();
    }
}
