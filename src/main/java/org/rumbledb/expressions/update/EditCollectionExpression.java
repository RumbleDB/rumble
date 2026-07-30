package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class EditCollectionExpression extends Expression {
    private Expression targetExpression;
    private Expression contentExpression;

    public EditCollectionExpression(
            Expression targetExpression,
            Expression contentExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (targetExpression == null) {
            throw new OurBadException("Tarrget must be identified for edit.");
        }
        if (contentExpression == null) {
            throw new OurBadException("Content must be specified for edit.");
        }
        this.targetExpression = targetExpression;
        this.contentExpression = contentExpression;
    }

    public Expression getTargetExpression() {
        return this.targetExpression;
    }

    public Expression getContentExpression() {
        return this.contentExpression;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.contentExpression, this.targetExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitEditCollectionExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("edit ");
        this.targetExpression.serializeToJSONiq(sb, 0);
        sb.append(" by ");
        this.contentExpression.serializeToJSONiq(sb, 1);
        sb.append(" from collection\n");
    }

}
