package org.rumbledb.runtime.scripting.mutation;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

import org.rumbledb.runtime.plan.UpdatingRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.io.Serial;
import java.util.Collections;

public class ApplyStatementIterator
        extends
            AbstractAtMostOneItemRuntimePlan
        implements
            UpdatingRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan exprIterator;

    public ApplyStatementIterator(
            ItemRuntimePlan exprIterator,
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
            UpdatingRuntimePlan.get(this.exprIterator, context)
                .applyUpdates(this.getRuntimeStaticContext().getMetadata());
        }
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        return new PendingUpdateList();
    }
}
