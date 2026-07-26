package org.rumbledb.runtime.scripting;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExitStatementException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

public class ProgramIterator extends HybridRuntimeIterator {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ProgramLocalCursor(this, context);
    }

    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator statementsAndExprIterator;
    private PendingUpdateList pendingUpdateList;
    private boolean encounteredExitStatement;

    public ProgramIterator(RuntimeIterator statementsAndExprIterator, RuntimeStaticContext staticContext) {
        super(Collections.singletonList(statementsAndExprIterator), staticContext);
        this.encounteredExitStatement = false;
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

    private static final class ProgramLocalCursor extends AbstractLocalCursor<Item> {

        private final ProgramIterator plan;
        private final DynamicContext context;
        private LocalCursor<Item> delegate;
        private List<Item> exitResults;
        private int exitIndex;

        private ProgramLocalCursor(ProgramIterator plan, DynamicContext context) {
            super(plan.getMetadata());
            this.plan = plan;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.delegate = this.plan.statementsAndExprIterator.createLocalCursor(this.context);
            try {
            } catch (ExitStatementException e) {
                captureExit(e);
            }
        }

        private void captureExit(ExitStatementException exception) {
            this.plan.setPULFromExitStatement(exception);
            this.exitResults = exception.getLocalResult();
            if (this.delegate != null) {
                this.delegate.close();
                this.delegate = null;
            }
        }

        @Override
        protected boolean hasNextLocal() {
            return this.exitResults == null
                ? this.delegate.hasNext()
                : this.exitIndex < this.exitResults.size();
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
}
