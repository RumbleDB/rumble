package org.rumbledb.runtime.scripting.loops;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.BreakStatementException;
import org.rumbledb.exceptions.ContinueStatementException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.Arrays;

public class WhileStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    private final RuntimeIterator testConditionIterator;
    private final RuntimeIterator bodyIterator;

    public WhileStatementIterator(
            RuntimeIterator testConditionIterator,
            RuntimeIterator bodyIterator,
            boolean isSequential,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(testConditionIterator, bodyIterator), staticContext);
        this.testConditionIterator = testConditionIterator;
        this.bodyIterator = bodyIterator;
        this.isSequential = isSequential;
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
