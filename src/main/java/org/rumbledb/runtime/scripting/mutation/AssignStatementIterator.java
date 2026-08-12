package org.rumbledb.runtime.scripting.mutation;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class AssignStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan assignExpression;
    private final Name variableName;

    public AssignStatementIterator(
            ItemRuntimePlan assignExpression, Name variableName, RuntimeStaticContext staticContext) {
        super(Collections.singletonList(assignExpression), staticContext);
        this.assignExpression = assignExpression;
        this.variableName = variableName;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        List<Item> exprItems = this.assignExpression.materialize(context);
        context.getVariableValues().changeVariableValue(this.variableName, exprItems);
        return null;
    }
}
