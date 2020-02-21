package org.rumbledb.expressions.control;


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class TypeSwitchExpression extends Expression {

    private final Expression testCondition;
    private final List<TypeswitchCase> cases;
    private final Expression defaultExpression;
    private final String defaultVariableName;

    public TypeSwitchExpression(
            Expression testCondition,
            List<TypeswitchCase> cases,
            Expression defaultExpression,
            String defaultVariableName,
            ExceptionMetadata metadataFromContext
    ) {

        super(metadataFromContext);
        this.testCondition = testCondition;
        this.cases = cases;
        this.defaultExpression = defaultExpression;
        this.defaultVariableName = defaultVariableName;
    }

    public Expression getTestCondition() {
        return this.testCondition;
    }

    public List<TypeswitchCase> getCases() {
        return this.cases;
    }

    public Expression getDefaultExpression() {
        return this.defaultExpression;
    }

    public String getDefaultVariableName() {
        return this.defaultVariableName;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.testCondition);
        for (TypeswitchCase c : cases) {
            result.addAll(c.getAllExpressions());
        }
        result.add(this.defaultExpression);
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
