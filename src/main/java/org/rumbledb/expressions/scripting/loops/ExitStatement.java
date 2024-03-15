package org.rumbledb.expressions.scripting.loops;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.Collections;
import java.util.List;

public class ExitStatement extends Statement {
    private Expression exitControlledExpression;
    private final Expression exitExpression;

    public ExitStatement(Expression exitExpression, ExceptionMetadata metadata) {
        super(metadata);
        this.exitExpression = exitExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitExitStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.exitExpression);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        this.exitExpression.serializeToJSONiq(sb, 0);
        sb.append("Expression controlled by exit statement:\n");
        this.exitControlledExpression.serializeToJSONiq(sb, 0);
    }

    public Expression getExitExpression() {
        return exitExpression;
    }

    /**
     * @return the Statement that should be exited.
     */
    public Expression getExitControlledExpression() {
        return exitControlledExpression;
    }

    public void setExitControlledExpression(Expression exitControlledExpression) {
        this.exitControlledExpression = exitControlledExpression;
    }
}
