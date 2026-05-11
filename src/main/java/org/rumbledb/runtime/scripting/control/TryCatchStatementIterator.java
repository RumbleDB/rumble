package org.rumbledb.runtime.scripting.control;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.BreakStatementException;
import org.rumbledb.exceptions.ContinueStatementException;
import org.rumbledb.exceptions.ExitStatementException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.Map;

public class TryCatchStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator tryStatementIterator;
    private final Map<String, RuntimeIterator> catchStatements;
    private final RuntimeIterator catchAllStatements;

    public TryCatchStatementIterator(
            RuntimeIterator tryStatement,
            Map<String, RuntimeIterator> catchStatements,
            RuntimeIterator catchAllStatement,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.children.add(tryStatement);
        this.children.addAll(catchStatements.values());
        if (catchAllStatement != null) {
            this.children.add(catchAllStatement);
        }
        this.tryStatementIterator = tryStatement;
        this.catchStatements = catchStatements;
        this.catchAllStatements = catchAllStatement;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        try {
            DynamicContext childContext = new DynamicContext(context);
            this.tryStatementIterator.materialize(childContext);
        } catch (Throwable throwable) {
            // If we catch a break or continue exception, our catch should not be allowed to act on it
            if (
                throwable instanceof BreakStatementException
                    || throwable instanceof ContinueStatementException
                    || throwable instanceof ExitStatementException
            ) {
                throw throwable;
            }
            RumbleException unnestedException = RumbleException.unnestException(throwable);
            String errorCode = unnestedException.getErrorCode();
            if (this.catchStatements.containsKey(errorCode)) {
                RuntimeIterator catchingStatementIterator = this.catchStatements.get(errorCode);
                DynamicContext childContext = new DynamicContext(context);
                catchingStatementIterator.materializeFirstItemOrNull(childContext);
            } else if (this.catchAllStatements != null) {
                DynamicContext childContext = new DynamicContext(context);
                this.catchAllStatements.materializeFirstItemOrNull(childContext);
            } else {
                throw throwable;
            }
        }
        return null;
    }
}
