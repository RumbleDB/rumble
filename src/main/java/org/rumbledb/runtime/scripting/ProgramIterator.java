package org.rumbledb.runtime.scripting;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExitStatementException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ProgramIterator extends HybridRuntimeIterator implements DataFrameRuntimePlan<Item> {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ProgramLocalCursor(
                this.statementsAndExprIterator,
                this.executionState,
                context,
                getRuntimeStaticContext()
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator statementsAndExprIterator;
    private final ProgramExecutionState executionState;

    public ProgramIterator(RuntimeIterator statementsAndExprIterator, RuntimeStaticContext staticContext) {
        super(Collections.singletonList(statementsAndExprIterator), staticContext);
        this.statementsAndExprIterator = statementsAndExprIterator;
        this.executionState = new ProgramExecutionState();
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
    public JSoundDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        try {
            return this.statementsAndExprIterator.getDataFrame(dynamicContext);
        } catch (ExitStatementException exitStatementException) {
            setPULFromExitStatement(exitStatementException);
            return exitStatementException.getDataFrameResult();
        }
    }

    private void setPULFromExitStatement(ExitStatementException exitStatementException) {
        this.executionState.capture(exitStatementException);
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
        if (!this.executionState.encounteredExitStatement) {
            return this.statementsAndExprIterator.getPendingUpdateList(context);
        }
        return this.executionState.pendingUpdateList;
    }

    private static final class ProgramLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimeIterator statementsAndExprPlan;
        private final ProgramExecutionState executionState;
        private final DynamicContext context;
        private LocalCursor<Item> delegate;
        private List<Item> exitResults;
        private int exitIndex;

        private ProgramLocalCursor(
                RuntimeIterator statementsAndExprPlan,
                ProgramExecutionState executionState,
                DynamicContext context,
                RuntimeStaticContext staticContext
        ) {
            super(staticContext.getMetadata());
            this.statementsAndExprPlan = statementsAndExprPlan;
            this.executionState = executionState;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.delegate = this.statementsAndExprPlan.createLocalCursor(this.context);
        }

        private void captureExit(ExitStatementException exception) {
            this.executionState.capture(exception);
            this.exitResults = exception.getLocalResult();
            if (this.delegate != null) {
                this.delegate.close();
                this.delegate = null;
            }
        }

        @Override
        protected boolean hasNextLocal() {
            if (this.exitResults != null) {
                return this.exitIndex < this.exitResults.size();
            }
            try {
                return this.delegate.hasNext();
            } catch (ExitStatementException e) {
                captureExit(e);
                return this.exitIndex < this.exitResults.size();
            }
        }

        @Override
        protected Item nextLocal() {
            if (this.exitResults != null) {
                if (this.exitIndex >= this.exitResults.size()) {
                    throw invalidState("No more program results are available.");
                }
                return this.exitResults.get(this.exitIndex++);
            }
            try {
                return this.delegate.next();
            } catch (ExitStatementException e) {
                captureExit(e);
                return nextLocal();
            }
        }

        @Override
        protected void closeLocal() {
            if (this.delegate != null) {
                this.delegate.close();
            }
            this.delegate = null;
            this.exitResults = null;
            this.exitIndex = 0;
        }
    }

    private static final class ProgramExecutionState implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private PendingUpdateList pendingUpdateList;
        private boolean encounteredExitStatement;

        private void capture(ExitStatementException exception) {
            this.encounteredExitStatement = true;
            this.pendingUpdateList = exception.getPendingUpdateList();
        }
    }
}
