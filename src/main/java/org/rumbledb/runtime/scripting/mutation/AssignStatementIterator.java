package org.rumbledb.runtime.scripting.mutation;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

public class AssignStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator assignExpression;
    private final Name variableName;

    public AssignStatementIterator(
            RuntimeIterator assignExpression,
            Name variableName,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(assignExpression), staticContext);
        this.assignExpression = assignExpression;
        this.variableName = variableName;
    }


    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        List<Item> exprItems = this.assignExpression.materialize(context);
        context.getVariableValues()
            .changeVariableValue(
                this.variableName,
                exprItems
            );
        return null;
    }
}
