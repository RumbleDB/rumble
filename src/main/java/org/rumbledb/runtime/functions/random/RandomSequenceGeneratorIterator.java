package org.rumbledb.runtime.functions.random;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;

public class RandomSequenceGeneratorIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {
    @Serial
    private static final long serialVersionUID = 1L;

    public RandomSequenceGeneratorIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> createRandoms(context), getMetadata());
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

}
