package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class DeleteSearchFromCollectionExpression extends Expression {
    private Expression contentExpression;

    public DeleteSearchFromCollectionExpression(
            Expression contentExpression,
            ExceptionMetadata metadata
    ) {
        // TODO: The current implementations only accounts for two callening modes- table, and delta-file
        // Extension to other modes can be done by increasing flags for using enum instead
        super(metadata);
        if (contentExpression == null) {
            throw new OurBadException("Content must be specified for deletion.");
        }
        this.contentExpression = contentExpression;
    }

    public Expression getContentExpression() {
        return this.contentExpression;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.contentExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitDeleteSearchFromCollectionExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("delete ");
        this.contentExpression.serializeToJSONiq(sb, 1);
        sb.append(" from collection ");
        sb.append("\n");
    }

}
