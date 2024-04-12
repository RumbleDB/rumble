package org.rumbledb.runtime.scripting.mutation;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.util.Collections;

public class ApplyStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    private RuntimeIterator exprIterator;

    public ApplyStatementIterator(
            RuntimeIterator exprIterator,
            boolean isSequential,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(exprIterator), staticContext);
        this.isSequential = isSequential;
        this.exprIterator = exprIterator;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.exprIterator.materialize(context);
        // Immediately apply pul
        this.exprIterator.getPendingUpdateList(context).applyUpdates(this.getMetadata());
        return null;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!this.exprIterator.isUpdating()) {
            return new PendingUpdateList();
        }
        return this.exprIterator.getPendingUpdateList(context);
    }
}
