package org.rumbledb.runtime.functions.random;


import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;

public class RandomSequenceWithBoundsIterator extends AbstractItemRuntimePlan implements LocalRuntimePlan<Item> {
    @Serial
    private static final long serialVersionUID = 1L;

    public RandomSequenceWithBoundsIterator(
            List<RuntimePlan<Item>> children,
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

    private GeneratedRandomsIterator createRandomNumberStream(DynamicContext context) {
        return createRandomNumberStream(
            this.getChild(0).materializeFirstOrNull(context),
            this.getChild(1).materializeFirstOrNull(context),
            this.getChild(2).materializeFirstOrNull(context).castToIntValue(),
            this.getChild(3).materializeFirstOrNull(context)
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

}
