package org.rumbledb.runtime.scripting.mutation;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.Collections;
import java.util.List;

public class AssignStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    private final RuntimeIterator assignExpression;
    private final Name variableName;

    public AssignStatementIterator(
            RuntimeIterator assignExpression,
            Name variableName,
            boolean isSequential,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(assignExpression), staticContext);
        this.assignExpression = assignExpression;
        this.variableName = variableName;
        this.isSequential = isSequential;
    }


    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        List<Item> exprItems = this.assignExpression.materialize(context);
        // we expect exactly one item for the value.
        assert exprItems.size() == 1;
        // Check sequence type
        context.getVariableValues()
            .addVariableValue(
                this.variableName,
                exprItems
            );
        return null;
    }
}
