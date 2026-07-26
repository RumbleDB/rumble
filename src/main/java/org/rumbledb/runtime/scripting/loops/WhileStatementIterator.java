package org.rumbledb.runtime.scripting.loops;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.BreakStatementException;
import org.rumbledb.exceptions.ContinueStatementException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;

import java.io.Serial;
import java.util.Arrays;

public class WhileStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator testConditionIterator;
    private final RuntimeIterator bodyIterator;

    public WhileStatementIterator(
            RuntimeIterator testConditionIterator,
            RuntimeIterator bodyIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            Arrays.asList(testConditionIterator, bodyIterator),
            staticContext
        );
        this.testConditionIterator = testConditionIterator;
        this.bodyIterator = bodyIterator;
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> {
                    while (EffectiveBooleanValue.evaluate(this.testConditionIterator, context)) {
                        try {
                            LocalCursorUtils.materialize(this.bodyIterator, new DynamicContext(context));
                        } catch (BreakStatementException exception) {
                            break;
                        } catch (ContinueStatementException ignored) {
                        }
                    }
                    return null;
                },
                getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        while (this.testConditionIterator.getEffectiveBooleanValue(context)) {
            try {
                DynamicContext childContext = new DynamicContext(context);
                this.bodyIterator.materialize(childContext);
            } catch (BreakStatementException breakStatementException) {
                break;
            } catch (ContinueStatementException ignored) {
            }
        }

        return null;
    }
}
