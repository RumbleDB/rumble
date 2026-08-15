package org.rumbledb.runtime.scripting.block;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class StatementsOnlyIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public StatementsOnlyIterator(List<? extends ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext.toBuilder().isSequential(true).build());
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        for (ItemRuntimePlan statement : this.getChildren()) {
            statement.materialize(dynamicContext);
        }
        return null;
    }
}
