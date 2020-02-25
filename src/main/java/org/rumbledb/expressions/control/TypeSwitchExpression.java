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
    private final TypeswitchCase defaultCase;

    public TypeSwitchExpression(
            Expression testCondition,
            List<TypeswitchCase> cases,
            TypeswitchCase defaultCase,
            ExceptionMetadata metadataFromContext
    ) {

        super(metadataFromContext);
        this.testCondition = testCondition;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    public Expression getTestCondition() {
        return this.testCondition;
    }

    public List<TypeswitchCase> getCases() {
        return this.cases;
    }

    public TypeswitchCase getDefaultCase() {
        return this.defaultCase;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.testCondition);
        for (TypeswitchCase c : cases) {
            result.add(c.getReturnExpression());
        }
        result.add(this.defaultCase.getReturnExpression());
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTypeSwitchExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        StringBuilder result = new StringBuilder();
        result.append("(TypeswitchExpression switch ");
        result.append(this.testCondition.serializationString(false));
        for (TypeswitchCase c : this.cases) {
            result.append("case ");
            result.append(c.getVariableName());
            result.append(" ");
            result.append("return ");
            result.append(c.getReturnExpression().serializationString(false));
        }
        result.append(" default ");
        result.append(this.defaultCase.getReturnExpression().serializationString(false));
        result.append(")");
        return result.toString();
    }
}
