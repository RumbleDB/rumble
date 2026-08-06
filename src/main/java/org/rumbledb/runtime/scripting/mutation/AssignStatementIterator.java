package org.rumbledb.runtime.scripting.mutation;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

public class AssignStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan assignExpression;
    private final Name variableName;

    public AssignStatementIterator(
            ItemRuntimePlan assignExpression,
            Name variableName,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(assignExpression), staticContext);
        this.assignExpression = assignExpression;
        this.variableName = variableName;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return assign(this.assignExpression.materialize(context), context);
    }

    private Item assign(List<Item> exprItems, DynamicContext context) {
        context.getVariableValues()
            .changeVariableValue(
                this.variableName,
                exprItems
            );
        return null;
    }
}
