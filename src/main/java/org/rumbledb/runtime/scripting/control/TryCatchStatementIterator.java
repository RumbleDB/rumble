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
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class TryCatchStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimePlan<Item> tryStatementIterator;
    private final Map<CatchPattern, RuntimePlan<Item>> catchStatements;

    public TryCatchStatementIterator(
            RuntimePlan<Item> tryStatement,
            Map<CatchPattern, RuntimePlan<Item>> catchStatements,
            RuntimeStaticContext staticContext
    ) {
        super(
            Stream.concat(Stream.of(tryStatement), catchStatements.values().stream()).toList(),
            staticContext
        );
        this.tryStatementIterator = tryStatement;
        this.catchStatements = catchStatements;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return execute(context, RuntimePlan<Item>::materialize);
    }

    private Item execute(
            DynamicContext context,
            BiConsumer<RuntimePlan<Item>, DynamicContext> materialize
    ) {
        try {
            DynamicContext childContext = new DynamicContext(context);
            materialize.accept(this.tryStatementIterator, childContext);
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
            RuntimePlan<Item> catchingStatementIterator = findMatchingCatch(
                unnestedException
            );
            if (catchingStatementIterator != null) {
                DynamicContext childContext = new DynamicContext(context);
                ErrorVariables.injectDynamicContext(childContext, unnestedException);
                materialize.accept(catchingStatementIterator, childContext);
            } else {
                throw throwable;
            }
        }
        return null;
    }

    private RuntimePlan<Item> findMatchingCatch(RumbleException exception) {
        for (Map.Entry<CatchPattern, RuntimePlan<Item>> entry : this.catchStatements.entrySet()) {
            if (entry.getKey().matches(exception.getErrorCode())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
