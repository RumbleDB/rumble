package org.rumbledb.runtime.scripting.loops;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ExitStatementException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

public class ExitStatementIterator extends HybridRuntimeIterator implements DataFrameRuntimePlan<Item> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator childIterator;
    private PendingUpdateList pendingUpdateList;

    public ExitStatementIterator(
            RuntimeIterator childIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(childIterator), staticContext);
        this.childIterator = childIterator;
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ExitLocalCursor(this.childIterator, context, getMetadata());
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.childIterator.getRDD(dynamicContext);
        this.pendingUpdateList = new PendingUpdateList();
        if (this.childIterator.isUpdating()) {
            this.pendingUpdateList = this.childIterator.getPendingUpdateList(dynamicContext);
        }
        throw new ExitStatementException(
                this.pendingUpdateList,
                null,
                childRDD,
                null,
                this.getMetadata()
        );
    }

    /*
     * Opening exit statement should compute all results from child iterator.
     * This is expected as the ExitStatement will throw an exception when invoking nextLocal that passes this result up
     * to the program or function containing the exit statement.
     */
    @Override
    protected void openLocal() {

    }

    @Override
    protected void closeLocal() {
    }

    @Override
    protected boolean hasNextLocal() {
        return true;
    }

    @Override
    protected Item nextLocal() {
        this.result = this.childIterator.materialize(this.currentDynamicContextForLocalExecution);
        this.pendingUpdateList = new PendingUpdateList();
        if (this.childIterator.isUpdating()) {
            this.pendingUpdateList = this.childIterator.getPendingUpdateList(
                this.currentDynamicContextForLocalExecution
            );
        }
        throw new ExitStatementException(
                this.pendingUpdateList,
                this.result,
                null,
                null,
                this.getMetadata()
        );
    }

    @Override
    public HomogeneousItemDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        HomogeneousItemDataFrame childDataFrame = this.childIterator.getDataFrame(dynamicContext);
        this.pendingUpdateList = new PendingUpdateList();
        if (this.childIterator.isUpdating()) {
            this.pendingUpdateList = this.childIterator.getPendingUpdateList(dynamicContext);
        }
        throw new ExitStatementException(
                this.pendingUpdateList,
                null,
                null,
                childDataFrame,
                this.getMetadata()
        );
    }

    private static final class ExitLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimeIterator childPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private boolean hasNext;

        private ExitLocalCursor(
                RuntimeIterator childPlan,
                DynamicContext context,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.childPlan = childPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.hasNext = true;
        }

        @Override
        protected boolean hasNextLocal() {
            return this.hasNext;
        }

        @Override
        protected Item nextLocal() {
            if (!this.hasNext) {
                throw invalidState("No more values are available.");
            }
            this.hasNext = false;
            List<Item> result = this.childPlan.materialize(this.context);
            PendingUpdateList updates = this.childPlan.isUpdating()
                ? this.childPlan.getPendingUpdateList(this.context)
                : new PendingUpdateList();
            throw new ExitStatementException(
                    updates,
                    result,
                    null,
                    null,
                    this.metadata
            );
        }

        @Override
        protected void closeLocal() {
            this.hasNext = false;
        }
    }
}
