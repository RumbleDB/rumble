package org.rumbledb.runtime.scripting;

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
import java.util.List;

public class ProgramIterator extends HybridRuntimeIterator {
    private final RuntimeIterator statementsAndExprIterator;
    private PendingUpdateList pendingUpdateList;
    private int nextLocalResult;
    private List<Item> localResult;
    private JavaRDD<Item> rddResult;
    private JSoundDataFrame dataFrameResult;

    private boolean encounteredExitStatement;

    public ProgramIterator(RuntimeIterator statementsAndExprIterator, RuntimeStaticContext staticContext) {
        super(Collections.singletonList(statementsAndExprIterator), staticContext);
        this.encounteredExitStatement = false;
        this.nextLocalResult = 0;
        this.localResult = null;
        this.rddResult = null;
        this.dataFrameResult = null;
        this.statementsAndExprIterator = statementsAndExprIterator;
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        if (!encounteredExitStatement) {
            return this.statementsAndExprIterator.getRDD(context);
        }
        return this.rddResult;
    }

    @Override
    protected void openLocal() {
        try {
            this.statementsAndExprIterator.open(this.currentDynamicContextForLocalExecution);
        } catch (ExitStatementException exitStatementException) {
            setResultFieldsFromExitStatement(exitStatementException);
        }
    }

    @Override
    protected void closeLocal() {
        this.statementsAndExprIterator.close();
    }

    @Override
    protected void resetLocal() {
        this.statementsAndExprIterator.reset(this.currentDynamicContextForLocalExecution);
    }

    @Override
    protected boolean hasNextLocal() {
        if (!this.encounteredExitStatement) {
            return this.statementsAndExprIterator.hasNext();
        }
        return this.nextLocalResult < this.localResult.size();
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        if (!encounteredExitStatement) {
            return this.statementsAndExprIterator.getDataFrame(dynamicContext);
        }
        return this.dataFrameResult;
    }

    private void setResultFieldsFromExitStatement(ExitStatementException exitStatementException) {
        this.encounteredExitStatement = true;
        this.pendingUpdateList = exitStatementException.getPendingUpdateList();
        if (exitStatementException.hasLocalResult()) {
            this.localResult = exitStatementException.getLocalResult();
        } else if (exitStatementException.hasRDDResult()) {
            this.rddResult = exitStatementException.getRddResult();
        } else if (exitStatementException.hasDataFrameResult()) {
            this.dataFrameResult = exitStatementException.getDataFrameResult();
        }
    }

    @Override
    protected Item nextLocal() {
        if (!encounteredExitStatement) {
            try {
                return this.statementsAndExprIterator.next();
            } catch (ExitStatementException exitStatementException) {
                // Encountering an exit statement sets the result and PUL for this iterator.
                setResultFieldsFromExitStatement(exitStatementException);
            }
        }
        // If we encountered an exit with local result, return the next item.
        if (this.localResult != null) {
            return this.localResult.get(this.nextLocalResult++);
        }
        return null;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!encounteredExitStatement) {
            return this.statementsAndExprIterator.getPendingUpdateList(context);
        }
        return this.pendingUpdateList;
    }
}
