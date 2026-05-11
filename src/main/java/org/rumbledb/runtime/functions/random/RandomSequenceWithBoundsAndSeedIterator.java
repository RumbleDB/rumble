package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class RandomSequenceWithBoundsAndSeedIterator extends LocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private Item low;
    private Item high;
    private Item type;
    private int seed;
    private int size;
    private GeneratedRandomsIterator generatedRandomsIterator;

    public RandomSequenceWithBoundsAndSeedIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        this.low = this.children.get(0).materializeFirstItemOrNull(context);
        this.high = this.children.get(1).materializeFirstItemOrNull(context);
        this.size = this.children.get(2).materializeFirstItemOrNull(context).castToIntValue();
        this.type = this.children.get(3).materializeFirstItemOrNull(context);
        this.seed = this.children.get(4).materializeFirstItemOrNull(context).castToIntValue();
        this.generatedRandomsIterator = createRandomNumberStream();
    }

    private GeneratedRandomsIterator createRandomNumberStream() {
        if (this.type.getStringValue().equals("integer")) {
            return new GeneratedRandomIntegersIterator(
                    this.size,
                    this.low.castToIntValue(),
                    this.high.castToIntValue(),
                    this.seed
            );
        } else {
            // Generate doubles otherwise
            return new GeneratedRandomDoublesIterator(
                    this.size,
                    this.low.castToDoubleValue(),
                    this.high.castToDoubleValue(),
                    this.seed
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
