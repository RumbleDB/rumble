package org.rumbledb.runtime.functions.random;

import org.rumbledb.runtime.plan.EvaluationArguments;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.List;

public class RandomSequenceWithBoundsIterator extends LocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
    private Item low;
    private Item high;
    private int size;
    private Item type;
    private GeneratedRandomsIterator generatedRandomsIterator;

    public RandomSequenceWithBoundsIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> createRandomNumberStream(
                    EvaluationArguments.lazy(
                        this.getChildren().size(),
                        index -> this.getChild(index).materializeFirstOrNull(context)
                    )
                ),
                getMetadata()
        );
    }

    @Override
    public void open(DynamicContext context) {
        this.low = this.getChild(0).materializeFirstItemOrNull(context);
        this.high = this.getChild(1).materializeFirstItemOrNull(context);
        this.size = this.getChild(2).materializeFirstItemOrNull(context).castToIntValue();
        this.type = this.getChild(3).materializeFirstItemOrNull(context);
        this.generatedRandomsIterator = createRandomNumberStream(this.low, this.high, this.size, this.type);
    }

    private GeneratedRandomsIterator createRandomNumberStream(EvaluationArguments<Item> arguments) {
        return createRandomNumberStream(
            arguments.get(0),
            arguments.get(1),
            arguments.get(2).castToIntValue(),
            arguments.get(3)
        );
    }

    private GeneratedRandomsIterator createRandomNumberStream(Item low, Item high, int size, Item type) {
        if (type.getStringValue().equals("integer")) {
            return new GeneratedRandomIntegersIterator(
                    size,
                    low.castToIntValue(),
                    high.castToIntValue()
            );
        } else {
            // Generate doubles otherwise
            return new GeneratedRandomDoublesIterator(
                    size,
                    low.castToDoubleValue(),
                    high.castToDoubleValue()
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
