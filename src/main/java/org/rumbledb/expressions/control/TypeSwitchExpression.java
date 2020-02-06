package org.rumbledb.expressions.control;

import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.ExpressionOrClause;
import org.rumbledb.expressions.primary.VariableReference;

public class TypeSwitchExpression extends Expression {

    private final Expression testCondition;
    private final List<TypeSwitchCaseExpression> cases;
    private final Expression defaultExpression;
    private final VariableReference varRefDefault;

    public TypeSwitchExpression(
            Expression testCondition,
            List<TypeSwitchCaseExpression> cases,
            Expression defaultExpression,
            VariableReference varRefDefault,
            ExceptionMetadata metadataFromContext
    ) {

        super(metadataFromContext);
        this.testCondition = testCondition;
        this.cases = cases;
        this.defaultExpression = defaultExpression;
        this.varRefDefault = varRefDefault;
    }

    public Expression getTestCondition() {
        return testCondition;
    }

    public List<TypeSwitchCaseExpression> getCases() {
        return cases;
    }

    public Expression getDefaultExpression() {
        return defaultExpression;
    }

    public VariableReference getVarRefDefault() {
        return varRefDefault;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result = new ArrayList<>();
        result.add(testCondition);
        result.addAll(cases);
        result.add(defaultExpression);
        if (varRefDefault != null)
            result.add(varRefDefault);
        return getDescendantsFromChildren(result, depthSearch);
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitTypeSwitchExpression(this, argument);
    }

    @Override
    // TODO implement serialization for switch expr
    public String serializationString(boolean prefix) {
        return "";
    }
}
