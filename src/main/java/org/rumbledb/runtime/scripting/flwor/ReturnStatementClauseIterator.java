package org.rumbledb.runtime.scripting.flwor;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.BreakStatementException;
import org.rumbledb.exceptions.ContinueStatementException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.io.Serial;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReturnStatementClauseIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeTupleIterator clauseIterator;
    private final RuntimeIterator expression;

    public ReturnStatementClauseIterator(
            RuntimeTupleIterator clauseIterator,
            RuntimeIterator expression,
            RuntimeStaticContext context
    ) {
        super(Collections.singletonList(expression), context);
        this.clauseIterator = clauseIterator;
        this.expression = expression;
        setInputAndOutputTupleVariableDependencies();
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> executeLocally(context),
                getMetadata()
        );
    }

    private Item executeLocally(DynamicContext context) {
        DynamicContext tupleContext = new DynamicContext(context);
        try (LocalCursor<FlworTuple> tuples = this.clauseIterator.createLocalCursor(context)) {
            tuples.open();
            while (tuples.hasNext()) {
                FlworTuple tuple = tuples.next();
                tupleContext.getVariableValues().removeAllVariables();
                tupleContext.getVariableValues().setBindingsFromTuple(tuple, getMetadata());
                try (LocalCursor<Item> results = this.expression.createLocalCursor(tupleContext)) {
                    results.open();
                    while (results.hasNext()) {
                        results.next();
                    }
                } catch (BreakStatementException ignored) {
                    break;
                } catch (ContinueStatementException ignored) {
                    // Continue with the next tuple.
                }
            }
        }
        return null;
    }

    private void setInputAndOutputTupleVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> dependencies = this.expression.getVariableDependencies();
        Set<Name> allTupleNames = this.clauseIterator.getOutputTupleVariableNames();
        Map<Name, DynamicContext.VariableDependency> projection = new HashMap<>();
        for (Name n : dependencies.keySet()) {
            if (allTupleNames.contains(n)) {
                projection.put(n, dependencies.get(n));
            }
        }
        this.clauseIterator.setInputAndOutputTupleVariableDependencies(projection);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.currentDynamicContextForLocalExecution = new DynamicContext(context);
        materializeWithLocalTuple();
        return null;
    }

    private void materializeWithLocalTuple() {
        this.clauseIterator.open(this.currentDynamicContextForLocalExecution);
        while (this.clauseIterator.hasNext()) {
            try {
                FlworTuple tuple = this.clauseIterator.next();
                this.currentDynamicContextForLocalExecution.getVariableValues().removeAllVariables(); // clear the
                                                                                                      // previous
                // variables
                this.currentDynamicContextForLocalExecution.getVariableValues()
                    .setBindingsFromTuple(tuple, getMetadata()); // assign new variables
                this.expression.materialize(this.currentDynamicContextForLocalExecution);
            } catch (BreakStatementException ignored) {
                break;
            } catch (ContinueStatementException ignored) {
            }
        }
        this.clauseIterator.close();
    }
}
