package org.rumbledb.runtime.scripting.loops;

import java.io.Serial;
import java.util.Arrays;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.BreakStatementException;
import org.rumbledb.exceptions.ContinueStatementException;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class WhileStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan testConditionIterator;
    private final ItemRuntimePlan bodyIterator;

    public WhileStatementIterator(
            ItemRuntimePlan testConditionIterator, ItemRuntimePlan bodyIterator, RuntimeStaticContext staticContext) {
        super(Arrays.asList(testConditionIterator, bodyIterator), staticContext);
        this.testConditionIterator = testConditionIterator;
        this.bodyIterator = bodyIterator;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        while (EffectiveBooleanValue.evaluate(this.testConditionIterator, context)) {
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
