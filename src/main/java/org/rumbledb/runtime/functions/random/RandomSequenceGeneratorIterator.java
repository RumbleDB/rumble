package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;
import java.util.Random;

public class RandomSequenceGeneratorIterator extends LocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private GeneratedRandomsIterator generatedRandomsIterator;
    private Random random;
    private Item seed;
    private Item sequenceLength;

    public RandomSequenceGeneratorIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item next() {
        return this.generatedRandomsIterator.getNextRandom();
    }

    @Override
    public boolean hasNext() {
        return this.generatedRandomsIterator.hasNext();
    }

    @Override
    public void open(DynamicContext context) {
        this.random = new Random();
        initializeSeedAndLength(context);
        this.generatedRandomsIterator = new GeneratedRandomDoublesIterator(
                random.doubles(sequenceLength.castToIntValue()).iterator()
        );
    }

    private void initializeSeedAndLength(DynamicContext context) {
        if (this.children.size() == 2) {
            // Seed is present as first argument
            this.seed = this.children.get(0).materializeFirstItemOrNull(context);
            this.random.setSeed(seed.castToIntValue());
            this.sequenceLength = this.children.get(1).materializeFirstItemOrNull(context);
        } else {
            this.sequenceLength = this.children.get(0).materializeFirstItemOrNull(context);
        }
    }
}
