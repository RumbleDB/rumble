package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class InsertSearchIntoCollectionExpression extends Expression {
    private Expression targetExpression;
    private Expression contentExpression;
    private boolean isBefore;

    public InsertSearchIntoCollectionExpression(
            Expression targetExpression,
            Expression contentExpression,
            boolean isBefore,
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
        this.isBefore = isBefore;
    }

    public Expression getTargetExpression() {
        return this.targetExpression;
    }

    public Expression getContentExpression() {
        return this.contentExpression;
    }

    public boolean isBefore() {
        return this.isBefore;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.contentExpression, this.targetExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitInsertSearchIntoCollectionExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("insert ");
        this.contentExpression.serializeToJSONiq(sb, 1);
        sb.append(this.isBefore ? " before" : " after");
        this.targetExpression.serializeToJSONiq(sb, 0);
        sb.append(" into collection\n");
    }

}
