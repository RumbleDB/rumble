package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;

import java.io.Serial;
import java.util.List;

public class RandomSequenceGeneratorIterator extends LocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
    private GeneratedRandomsIterator generatedRandomsIterator;

    public RandomSequenceGeneratorIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> createRandoms(context), getMetadata());
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
        this.generatedRandomsIterator = createRandomsLegacy(context);
    }

    private GeneratedRandomsIterator createRandoms(DynamicContext context) {
        if (this.getChildren().size() == 2) {
            // Seed is present as first argument
            int seed = LocalCursorUtils.materializeFirst(this.getChild(0), context).castToIntValue();
            int sequenceLength = LocalCursorUtils.materializeFirst(this.getChild(1), context).castToIntValue();
            return new GeneratedRandomDoublesIterator(
                    sequenceLength,
                    seed
            );
        } else {
            int sequenceLength = LocalCursorUtils.materializeFirst(this.getChild(0), context).castToIntValue();
            return new GeneratedRandomDoublesIterator(
                    sequenceLength
            );
        }
    }

    private GeneratedRandomsIterator createRandomsLegacy(DynamicContext context) {
        if (this.getChildren().size() == 2) {
            int seed = this.getChild(0).materializeFirstItemOrNull(context).castToIntValue();
            int sequenceLength = this.getChild(1).materializeFirstItemOrNull(context).castToIntValue();
            return new GeneratedRandomDoublesIterator(sequenceLength, seed);
        }
        int sequenceLength = this.getChild(0).materializeFirstItemOrNull(context).castToIntValue();
        return new GeneratedRandomDoublesIterator(sequenceLength);
    }
}
