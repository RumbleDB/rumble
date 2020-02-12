package org.rumbledb.expressions.control;


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.FlworVarSequenceType;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import sparksoniq.semantics.visitor.AbstractNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class TypeSwitchCaseExpression extends Expression {

    private VariableReferenceExpression variableReferenceExpression;
    private List<FlworVarSequenceType> union = new ArrayList<>();
    private final Expression returnExpression;

    public TypeSwitchCaseExpression(
            VariableReferenceExpression variableReferenceExpression,
            List<FlworVarSequenceType> union,
            Expression returnExpression,
            ExceptionMetadata metadataFromContext
    ) {
        super(metadataFromContext);
        this.variableReferenceExpression = variableReferenceExpression;
        this.union.addAll(union);
        this.returnExpression = returnExpression;
    }

    public VariableReferenceExpression getVariableReference() {
        return this.variableReferenceExpression;
    }

    public List<FlworVarSequenceType> getUnion() {
        return this.union;
    }

    public Expression getReturnExpression() {
        return this.returnExpression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.variableReferenceExpression != null)
            result.add(this.variableReferenceExpression);
        if (this.union != null && !this.union.isEmpty())
            result.addAll(this.union);
        if (this.returnExpression != null)
            result.add(this.returnExpression);
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTypeSwitchCaseExpression(this, argument);
    }

    @Override
    // TODO implement serialization for switch expr
    public String serializationString(boolean prefix) {
        return "";
    }
}
