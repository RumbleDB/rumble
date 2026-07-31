package org.rumbledb.runtime.scripting.loops;

import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.plan.UpdatingRuntimePlan;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ExitStatementException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

public class ExitStatementIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item>,
            UpdatingRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimePlan<Item> childIterator;
    private PendingUpdateList pendingUpdateList;

    public ExitStatementIterator(
            RuntimePlan<Item> childIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(childIterator), staticContext);
        this.childIterator = childIterator;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ExitLocalCursor(this.childIterator, context, getMetadata());
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.childIterator.getRDD(dynamicContext);
        this.pendingUpdateList = new PendingUpdateList();
        if (this.childIterator.getRuntimeStaticContext().isUpdating()) {
            this.pendingUpdateList = UpdatingRuntimePlan.get(
                this.childIterator,
                dynamicContext
            );
        }
        throw new ExitStatementException(
                this.pendingUpdateList,
                null,
                childRDD,
                null,
                this.getRuntimeStaticContext().getMetadata()
        );
    }

    /*
     * Opening exit statement should compute all results from child iterator.
     * This is expected as the ExitStatement will throw an exception when invoking nextLocal that passes this result up
     * to the program or function containing the exit statement.
     */



    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        HomogeneousItemDataFrame childDataFrame = ItemRuntimeDataFrameFactory.INSTANCE
            .fromPlan(this.childIterator, dynamicContext);
        this.pendingUpdateList = new PendingUpdateList();
        if (this.childIterator.getRuntimeStaticContext().isUpdating()) {
            this.pendingUpdateList = UpdatingRuntimePlan.get(
                this.childIterator,
                dynamicContext
            );
        }
        throw new ExitStatementException(
                this.pendingUpdateList,
                null,
                null,
                childDataFrame,
                this.getRuntimeStaticContext().getMetadata()
        );
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        return this.childIterator.getRuntimeStaticContext().isUpdating()
            ? UpdatingRuntimePlan.get(this.childIterator, context)
            : new PendingUpdateList();
    }

    private static final class ExitLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> childPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private boolean hasNext;

        private ExitLocalCursor(
                RuntimePlan<Item> childPlan,
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
            PendingUpdateList updates = this.childPlan.getRuntimeStaticContext().isUpdating()
                ? UpdatingRuntimePlan.get(this.childPlan, this.context)
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
