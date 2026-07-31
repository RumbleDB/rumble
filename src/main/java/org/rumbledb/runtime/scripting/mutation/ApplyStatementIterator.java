package org.rumbledb.runtime.scripting.mutation;

import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;

import org.rumbledb.runtime.plan.UpdatingRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.io.Serial;
import java.util.Collections;

public class ApplyStatementIterator
        extends
            AtMostOneItemLocalRuntimeIterator
        implements
            UpdatingRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> exprIterator;

    public ApplyStatementIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> exprIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(exprIterator), staticContext);
        this.exprIterator = exprIterator;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        this.exprIterator.materialize(context);
        applyUpdates(context);
        return null;
    }

    private void applyUpdates(DynamicContext context) {
        // Immediately apply pul if applicable
        if (this.exprIterator.getRuntimeStaticContext().isUpdating()) {
            org.rumbledb.runtime.plan.UpdatingRuntimePlan.get(this.exprIterator, context)
                .applyUpdates(this.getRuntimeStaticContext().getMetadata());
        }
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        return new PendingUpdateList();
    }
}
