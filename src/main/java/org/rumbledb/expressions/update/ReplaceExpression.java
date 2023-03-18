package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class ReplaceExpression extends Expression {

    private Expression mainExpression;
    private Expression finalExpression;
    private Expression newExpression;
    public ReplaceExpression(Expression mainExpression,
                             Expression finalExpression,
                             Expression newExpression,
                             ExceptionMetadata metadata
    ) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("Main expression cannot be null in a replace expression.");
        }
        if (finalExpression == null) {
            throw new OurBadException("Final expression cannot be null in a replace expression.");
        }
        if (newExpression == null) {
            throw new OurBadException("New expression cannot be null in a replace expression.");
        }
        this.mainExpression = mainExpression;
        this.finalExpression = finalExpression;
        this.newExpression = newExpression;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.mainExpression, this.finalExpression, this.newExpression);
    }

    public Expression getMainExpression() {
        return mainExpression;
    }

    public Expression getFinalExpression() {
        return finalExpression;
    }

    public Expression getNewExpression() {
        return newExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitReplaceExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("replace value of ");
        this.mainExpression.serializeToJSONiq(sb, 0);
        sb.append("(");
        this.finalExpression.serializeToJSONiq(sb,0);
        sb.append(") with ");
        this.newExpression.serializeToJSONiq(sb,0);
        sb.append("\n");
    }
}
