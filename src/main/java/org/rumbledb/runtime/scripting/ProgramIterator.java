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
    private int nextExitStatementResult;
    private List<Item> exitStatementLocalResult;

    private boolean encounteredExitStatement;

    public ProgramIterator(RuntimeIterator statementsAndExprIterator, RuntimeStaticContext staticContext) {
        super(Collections.singletonList(statementsAndExprIterator), staticContext);
        this.encounteredExitStatement = false;
        this.nextExitStatementResult = 0;
        this.exitStatementLocalResult = null;
        this.statementsAndExprIterator = statementsAndExprIterator;
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        try {
            return this.statementsAndExprIterator.getRDD(context);
        } catch (ExitStatementException exitStatementException) {
            setPULFromExitStatement(exitStatementException);
            return exitStatementException.getRddResult();
        }
    }

    @Override
    protected void openLocal() {
        try {
            this.statementsAndExprIterator.open(this.currentDynamicContextForLocalExecution);
        } catch (ExitStatementException exitStatementException) {
            setPULFromExitStatement(exitStatementException);
            this.exitStatementLocalResult = exitStatementException.getLocalResult();
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
        return this.nextExitStatementResult < this.exitStatementLocalResult.size();
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        try {
            return this.statementsAndExprIterator.getDataFrame(dynamicContext);
        } catch (ExitStatementException exitStatementException) {
            setPULFromExitStatement(exitStatementException);
            return exitStatementException.getDataFrameResult();
        }
    }

    private void setPULFromExitStatement(ExitStatementException exitStatementException) {
        this.encounteredExitStatement = true;
        this.pendingUpdateList = exitStatementException.getPendingUpdateList();
    }

    @Override
    protected Item nextLocal() {
        if (!this.encounteredExitStatement) {
            try {
                return this.statementsAndExprIterator.next();
            } catch (ExitStatementException exitStatementException) {
                // Encountering an exit statement sets the result and PUL for this iterator.
                setPULFromExitStatement(exitStatementException);
                this.exitStatementLocalResult = exitStatementException.getLocalResult();
            }
        }
        // If we encountered an exit with local result, return the next item.
        if (this.exitStatementLocalResult != null) {
            return this.exitStatementLocalResult.get(this.nextExitStatementResult++);
        }
        return null;
    }

    @Override
    public boolean isSequential() {
        return this.statementsAndExprIterator.isSequential();
    }

    @Override
    public boolean isUpdating() {
        return this.statementsAndExprIterator.isUpdating();
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!this.encounteredExitStatement) {
            return this.statementsAndExprIterator.getPendingUpdateList(context);
        }
        return this.pendingUpdateList;
    }
}
