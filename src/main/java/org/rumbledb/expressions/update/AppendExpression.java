package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.ExpressionClassification;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class AppendExpression extends Expression {

    private Expression arrayExpression;
    private Expression toAppendExpression;
    public AppendExpression(Expression arrayExpression,
                            Expression toAppendExpression,
                            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (arrayExpression == null) {
            throw new OurBadException("Array expression cannot be null in a append expression.");
        }
        if (toAppendExpression == null) {
            throw new OurBadException("Expression to append cannot be null in a append expression.");
        }
        this.arrayExpression = arrayExpression;
        this.toAppendExpression = toAppendExpression;
        this.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
    }

    public Expression getArrayExpression() {
        return arrayExpression;
    }

    public Expression getToAppendExpression() {
        return toAppendExpression;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.arrayExpression, this.toAppendExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitAppendExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("append json ");
        this.toAppendExpression.serializeToJSONiq(sb, 0);
        sb.append(" into ");
        this.arrayExpression.serializeToJSONiq(sb,0);
        sb.append("\n");
    }
}
