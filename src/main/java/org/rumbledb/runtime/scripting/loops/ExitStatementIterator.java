package org.rumbledb.runtime.scripting.loops;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExitStatementException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.util.Collections;

public class ExitStatementIterator extends HybridRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator childIterator;
    private PendingUpdateList pendingUpdateList;

    public ExitStatementIterator(
            RuntimeIterator childIterator,
            boolean isSequential,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(childIterator), staticContext);
        this.isSequential = isSequential;
        this.childIterator = childIterator;
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
    protected void resetLocal() {
        this.childIterator.reset(this.currentDynamicContextForLocalExecution);
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
            pendingUpdateList = this.childIterator.getPendingUpdateList(this.currentDynamicContextForLocalExecution);
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
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        JSoundDataFrame childDataFrame = this.childIterator.getDataFrame(dynamicContext);
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
}
