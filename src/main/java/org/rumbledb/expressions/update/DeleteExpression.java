package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class DeleteExpression extends Expression {

    private Expression mainExpression;
    private Expression finalExpression;
    public DeleteExpression(Expression mainExpression,
                               Expression finalExpression,
                               ExceptionMetadata metadata
    ) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("Main expression cannot be null in a delete expression.");
        }
        if (finalExpression == null) {
            throw new OurBadException("Final expression cannot be null in a delete expression.");
        }
        this.mainExpression = mainExpression;
        this.finalExpression = finalExpression;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.mainExpression, this.finalExpression);
    }

    public Expression getMainExpression() {
        return mainExpression;
    }

    public Expression getFinalExpression() {
        return finalExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitDeleteExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        this.mainExpression.serializeToJSONiq(sb, 0);
        sb.append("(");
        this.finalExpression.serializeToJSONiq(sb,0);
        sb.append(")\n");
    }
}
