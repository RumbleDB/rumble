package org.rumbledb.runtime.scripting.mutation;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.Collections;

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
        context.getVariableValues()
            .addVariableValue(
                this.variableName,
                this.assignExpression.materialize(context)
            );
        return null;
    }
}
