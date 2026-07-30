package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

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
    public Cursor<Item> createNativeCursor(DynamicContext context) {
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
            int seed = this.getChild(0).materializeFirstOrNull(context).castToIntValue();
            int sequenceLength = this.getChild(1).materializeFirstOrNull(context).castToIntValue();
            return new GeneratedRandomDoublesIterator(
                    sequenceLength,
                    seed
            );
        } else {
            int sequenceLength = this.getChild(0).materializeFirstOrNull(context).castToIntValue();
            return new GeneratedRandomDoublesIterator(
                    sequenceLength
            );
        }
    }

    private GeneratedRandomsIterator createRandomsLegacy(DynamicContext context) {
        if (this.getChildren().size() == 2) {
            int seed = this.getChild(0).materializeFirstOrNull(context).castToIntValue();
            int sequenceLength = this.getChild(1).materializeFirstOrNull(context).castToIntValue();
            return new GeneratedRandomDoublesIterator(sequenceLength, seed);
        }
        int sequenceLength = this.getChild(0).materializeFirstOrNull(context).castToIntValue();
        return new GeneratedRandomDoublesIterator(sequenceLength);
    }
}
