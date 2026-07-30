package org.rumbledb.runtime.scripting.mutation;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.Collections;

public class ApplyStatementIterator extends AtMostOneItemLocalRuntimeIterator {
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
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> {
                    this.exprIterator.materialize(context);
                    applyUpdates(context);
                    return null;
                },
                getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
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
