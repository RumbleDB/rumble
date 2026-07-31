package org.rumbledb.runtime.scripting.flwor;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.BreakStatementException;
import org.rumbledb.exceptions.ContinueStatementException;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.TupleRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.plan.VariableDependencyRuntimePlan;
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.io.Serial;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReturnStatementClauseIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;
    private final TupleRuntimePlan clauseIterator;
    private final RuntimePlan<Item> expression;

    public ReturnStatementClauseIterator(
            TupleRuntimePlan clauseIterator,
            RuntimePlan<Item> expression,
            RuntimeStaticContext context
    ) {
        super(Collections.singletonList(expression), context);
        this.clauseIterator = clauseIterator;
        this.expression = expression;
        setInputAndOutputTupleVariableDependencies();
    }

    private Item executeLocally(DynamicContext context) {
        DynamicContext tupleContext = new DynamicContext(context);
        try (Cursor<FlworTuple> tuples = this.clauseIterator.getCursor(context)) {
            while (tuples.hasNext()) {
                FlworTuple tuple = tuples.next();
                tupleContext.getVariableValues().removeAllVariables();
                tupleContext.getVariableValues().setBindingsFromTuple(tuple, getMetadata());
                try (Cursor<Item> results = this.expression.getCursor(tupleContext)) {
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
        Map<Name, DynamicContext.VariableDependency> dependencies =
            VariableDependencyRuntimePlan.get(this.expression);
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
    public Item evaluateAtMostOne(DynamicContext context) {
        return executeLocally(context);
    }

}
