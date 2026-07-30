package org.rumbledb.runtime.scripting.mutation;

import org.rumbledb.runtime.plan.UpdatingRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.util.Collections;

public class ApplyStatementIterator extends AtMostOneItemLocalRuntimeIterator implements UpdatingRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator exprIterator;

    public ApplyStatementIterator(
            RuntimeIterator exprIterator,
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
        if (this.exprIterator.isUpdating()) {
            this.exprIterator.getPendingUpdateList(context).applyUpdates(this.getMetadata());
        }
    }
}
