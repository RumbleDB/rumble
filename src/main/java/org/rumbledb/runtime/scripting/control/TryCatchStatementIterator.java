package org.rumbledb.runtime.scripting.control;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorVariables;
import org.rumbledb.expressions.control.CatchPattern;
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
    private final Map<CatchPattern, RuntimeIterator> catchStatements;

    public TryCatchStatementIterator(
            RuntimeIterator tryStatement,
            Map<CatchPattern, RuntimeIterator> catchStatements,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.children.add(tryStatement);
        this.children.addAll(catchStatements.values());
        this.tryStatementIterator = tryStatement;
        this.catchStatements = catchStatements;
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
            RuntimeIterator catchingStatementIterator = findMatchingCatch(unnestedException);
            if (catchingStatementIterator != null) {
                DynamicContext childContext = new DynamicContext(context);
                ErrorVariables.injectDynamicContext(childContext, unnestedException);
                catchingStatementIterator.materializeFirstItemOrNull(childContext);
            } else {
                throw throwable;
            }
        }
        return null;
    }

    private RuntimeIterator findMatchingCatch(RumbleException exception) {
        for (Map.Entry<CatchPattern, RuntimeIterator> entry : this.catchStatements.entrySet()) {
            if (entry.getKey().matches(exception.getErrorCode())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
