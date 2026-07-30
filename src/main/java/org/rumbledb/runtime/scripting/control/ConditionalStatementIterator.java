package org.rumbledb.runtime.scripting.control;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;

import java.io.Serial;
import java.util.List;

public class ConditionalStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public ConditionalStatementIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    private org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> selectApplicableIterator(
            DynamicContext dynamicContext
    ) {
        org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> condition = this.getChild(0);
        boolean effectiveBooleanValue = org.rumbledb.runtime.EffectiveBooleanValue.evaluate(condition, dynamicContext);
        if (effectiveBooleanValue) {
            return this.getChild(1);
        } else {
            return this.getChild(2);
        }
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> selectedIterator = selectApplicableIterator(
            dynamicContext
        );
        DynamicContext childDynamicContext = new DynamicContext(dynamicContext);
        selectedIterator.materialize(childDynamicContext);
        return null;
    }
}
