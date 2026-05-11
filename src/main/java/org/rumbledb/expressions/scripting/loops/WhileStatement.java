package org.rumbledb.expressions.scripting.loops;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class WhileStatement extends Statement {
    private final Expression testCondition;
    private final Statement statement;

    public WhileStatement(Expression testCondition, Statement statement, ExceptionMetadata metadata) {
        super(metadata);
        this.testCondition = testCondition;
        this.statement = statement;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitWhileStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.testCondition);
        result.add(this.statement);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("while (");
        this.testCondition.serializeToJSONiq(sb, 0);
        sb.append(")\n");
        this.statement.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }

    public Expression getTestCondition() {
        return this.testCondition;
    }

    public Statement getStatement() {
        return this.statement;
    }
}
