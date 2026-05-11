package org.rumbledb.expressions.scripting.control;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class ConditionalStatement extends Statement {
    private final Expression conditionExpression;
    private final Statement thenStatement;
    private final Statement elseStatement;

    public ConditionalStatement(
            Expression conditionExpression,
            Statement thenStatement,
            Statement elseStatement,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.conditionExpression = conditionExpression;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    public Expression getCondition() {
        return this.conditionExpression;
    }

    public Statement getBranch() {
        return this.thenStatement;
    }

    public Statement getElseBranch() {
        return this.elseStatement;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitConditionalStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.conditionExpression);
        result.add(this.thenStatement);
        result.add(this.elseStatement);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("if (");
        this.conditionExpression.serializeToJSONiq(sb, 0);
        sb.append(")\n");

        indentIt(sb, indent + 1);
        sb.append("then (");

        this.thenStatement.serializeToJSONiq(sb, 0);
        sb.append(")\n");

        indentIt(sb, indent + 1);
        sb.append("else (");

        this.elseStatement.serializeToJSONiq(sb, indent + 1);
        sb.append(")\n");
    }
}
