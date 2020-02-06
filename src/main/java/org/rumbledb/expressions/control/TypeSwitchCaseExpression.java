package org.rumbledb.expressions.control;

import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.ExpressionOrClause;
import org.rumbledb.expressions.flowr.FlworVarSequenceType;
import org.rumbledb.expressions.primary.VariableReference;

public class TypeSwitchCaseExpression extends Expression {

    private VariableReference variableReferenceNode;
    private List<FlworVarSequenceType> union = new ArrayList<>();
    private final Expression returnExpression;

    public TypeSwitchCaseExpression(
            VariableReference var,
            List<FlworVarSequenceType> union,
            Expression returnExpression,
            ExceptionMetadata metadataFromContext
    ) {
        super(metadataFromContext);
        this.variableReferenceNode = var;
        this.union.addAll(union);
        this.returnExpression = returnExpression;
    }

    public VariableReference getVariableReference() {
        return variableReferenceNode;
    }

    public List<FlworVarSequenceType> getUnion() {
        return this.union;
    }

    public Expression getReturnExpression() {
        return returnExpression;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result = new ArrayList<>();
        if (variableReferenceNode != null)
            result.add(variableReferenceNode);
        if (union != null && !union.isEmpty())
            result.addAll(union);
        if (this.returnExpression != null)
            result.add(returnExpression);
        return getDescendantsFromChildren(result, depthSearch);
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitTypeSwitchCaseExpression(this, argument);
    }

    @Override
    // TODO implement serialization for switch expr
    public String serializationString(boolean prefix) {
        return "";
    }
}
