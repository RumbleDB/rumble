package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class RandomSequenceGeneratorIterator extends LocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private GeneratedRandomsIterator generatedRandomsIterator;

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
        if (this.children.size() == 2) {
            // Seed is present as first argument
            int seed = this.children.get(0).materializeFirstItemOrNull(context).castToIntValue();
            int sequenceLength = this.children.get(1).materializeFirstItemOrNull(context).castToIntValue();
            this.generatedRandomsIterator = new GeneratedRandomDoublesIterator(
                    sequenceLength,
                    seed
            );
        } else {
            int sequenceLength = this.children.get(0).materializeFirstItemOrNull(context).castToIntValue();
            this.generatedRandomsIterator = new GeneratedRandomDoublesIterator(
                    sequenceLength
            );
        }
    }
}
