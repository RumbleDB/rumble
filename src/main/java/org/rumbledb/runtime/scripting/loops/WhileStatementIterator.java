package org.rumbledb.runtime.scripting.loops;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.BreakStatementException;
import org.rumbledb.exceptions.ContinueStatementException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

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
