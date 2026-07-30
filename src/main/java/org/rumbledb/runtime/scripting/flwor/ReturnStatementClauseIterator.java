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
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReturnStatementClauseIterator extends AtMostOneItemLocalRuntimeIterator {
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
