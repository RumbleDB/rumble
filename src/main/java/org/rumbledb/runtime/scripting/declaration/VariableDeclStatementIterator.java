package org.rumbledb.runtime.scripting.declaration;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.VariableValues;
import org.rumbledb.exceptions.VariableAlreadyExistsException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class VariableDeclStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    private final Name variableName;

    public VariableDeclStatementIterator(
            Name variableName,
            List<RuntimeIterator> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
        this.variableName = variableName;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        VariableValues variableValues = dynamicContext.getVariableValues();
        if (variableValues.containsLocally(variableValues, this.variableName)) {
            throw new VariableAlreadyExistsException(this.variableName, this.getMetadata());
        }
        if (this.children != null && !this.children.isEmpty()) {
            RuntimeIterator exprIterator = this.children.get(0);
            exprIterator.bindToVariableInDynamicContext(dynamicContext, this.variableName, dynamicContext);
        } else {
            // Casting needed to distinguish between local and RDD variables.
            dynamicContext.getVariableValues()
                .addVariableValue(this.variableName, (List<Item>) null);
        }
        return null;
    }
}
