package org.rumbledb.runtime.scripting.control;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;

public class ConditionalStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public ConditionalStatementIterator(
            List<RuntimePlan<Item>> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    private RuntimePlan<Item> selectApplicableIterator(
            DynamicContext dynamicContext
    ) {
        RuntimePlan<Item> condition = this.getChild(0);
        boolean effectiveBooleanValue = EffectiveBooleanValue.evaluate(condition, dynamicContext);
        if (effectiveBooleanValue) {
            return this.getChild(1);
        } else {
            return this.getChild(2);
        }
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        RuntimePlan<Item> selectedIterator = selectApplicableIterator(
            dynamicContext
        );
        DynamicContext childDynamicContext = new DynamicContext(dynamicContext);
        selectedIterator.materialize(childDynamicContext);
        return null;
    }
}
