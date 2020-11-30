package org.rumbledb.expressions.control;


import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.ExecutionMode;
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
        for (TypeswitchCase c : this.cases) {
            result.add(c.getReturnExpression());
        }
        result.add(this.defaultCase.getReturnExpression());
        return result;
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        this.highestExecutionMode = this.defaultCase.getReturnExpression().getHighestExecutionMode(visitorConfig);

        if (this.highestExecutionMode == ExecutionMode.RDD) {
            for (TypeswitchCase c : this.cases) {
                if (!c.getReturnExpression().getHighestExecutionMode(visitorConfig).isRDDOrDataFrame()) {
                    this.highestExecutionMode = ExecutionMode.LOCAL;
                    break;
                }
            }
        }
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTypeSwitchExpression(this, argument);
    }
}
