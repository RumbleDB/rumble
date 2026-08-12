package org.rumbledb.runtime.scripting.declaration;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.VariableValues;
import org.rumbledb.exceptions.VariableAlreadyExistsException;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlanBindings;

public class VariableDeclStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Name variableName;

    public VariableDeclStatementIterator(
            Name variableName, List<? extends ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
        this.variableName = variableName;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        if (!this.getChildren().isEmpty()
                && !this.getChild(0)
                        .getRuntimeStaticContext()
                        .getExecutionMode()
                        .isLocal()) {
            return declareDistributed(dynamicContext);
        }
        return declare(
                this.getChildren().isEmpty() ? null : this.getChild(0).materialize(dynamicContext), dynamicContext);
    }

    private Item declare(List<Item> value, DynamicContext dynamicContext) {
        VariableValues variableValues = dynamicContext.getVariableValues();
        if (variableValues.containsLocally(variableValues, this.variableName)) {
            throw new VariableAlreadyExistsException(
                    this.variableName, this.getRuntimeStaticContext().getMetadata());
        }
        dynamicContext.getVariableValues().addVariableValue(this.variableName, value);
        return null;
    }

    private Item declareDistributed(DynamicContext dynamicContext) {
        VariableValues variableValues = dynamicContext.getVariableValues();
        if (variableValues.containsLocally(variableValues, this.variableName)) {
            throw new VariableAlreadyExistsException(
                    this.variableName, this.getRuntimeStaticContext().getMetadata());
        }
        RuntimePlanBindings.bind(this.getChild(0), dynamicContext, this.variableName, dynamicContext);
        return null;
    }
}
