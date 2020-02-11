package org.rumbledb.expressions.control;


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import sparksoniq.semantics.visitor.AbstractNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class TypeSwitchExpression extends Expression {

    private final Expression testCondition;
    private final List<TypeSwitchCaseExpression> cases;
    private final Expression defaultExpression;
    private final VariableReferenceExpression varRefDefault;

    public TypeSwitchExpression(
            Expression testCondition,
            List<TypeSwitchCaseExpression> cases,
            Expression defaultExpression,
            VariableReferenceExpression varRefDefault,
            ExceptionMetadata metadataFromContext
    ) {

        super(metadataFromContext);
        this.testCondition = testCondition;
        this.cases = cases;
        this.defaultExpression = defaultExpression;
        this.varRefDefault = varRefDefault;
    }

    public Expression getTestCondition() {
        return this.testCondition;
    }

    public List<TypeSwitchCaseExpression> getCases() {
        return this.cases;
    }

    public Expression getDefaultExpression() {
        return this.defaultExpression;
    }

    public VariableReferenceExpression getVarRefDefault() {
        return this.varRefDefault;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.testCondition);
        result.addAll(this.cases);
        result.add(this.defaultExpression);
        if (this.varRefDefault != null)
            result.add(this.varRefDefault);
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTypeSwitchExpression(this, argument);
    }

    @Override
    // TODO implement serialization for switch expr
    public String serializationString(boolean prefix) {
        return "";
    }
}
