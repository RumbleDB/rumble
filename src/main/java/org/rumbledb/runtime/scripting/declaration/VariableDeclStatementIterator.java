package org.rumbledb.runtime.scripting.declaration;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.VariableValues;
import org.rumbledb.exceptions.VariableAlreadyExistsException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

import java.io.Serial;
import java.util.List;

public class VariableDeclStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
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
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> declare(
                    this.getChildren().isEmpty()
                        ? null
                        : this.getChild(0).materialize(context),
                    context
                ),
                getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        if (!this.getChildren().isEmpty() && !this.getChild(0).isLocal()) {
            return declareDistributed(dynamicContext);
        }
        return declare(
            this.getChildren().isEmpty() ? null : this.getChild(0).materialize(dynamicContext),
            dynamicContext
        );
    }

    private Item declare(List<Item> value, DynamicContext dynamicContext) {
        VariableValues variableValues = dynamicContext.getVariableValues();
        if (variableValues.containsLocally(variableValues, this.variableName)) {
            throw new VariableAlreadyExistsException(this.variableName, this.getMetadata());
        }
        dynamicContext.getVariableValues().addVariableValue(this.variableName, value);
        return null;
    }

    private Item declareDistributed(DynamicContext dynamicContext) {
        VariableValues variableValues = dynamicContext.getVariableValues();
        if (variableValues.containsLocally(variableValues, this.variableName)) {
            throw new VariableAlreadyExistsException(this.variableName, this.getMetadata());
        }
        this.getChild(0).bindToVariableInDynamicContext(dynamicContext, this.variableName, dynamicContext);
        return null;
    }
}
