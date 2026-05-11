package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class InsertExpression extends Expression {

    private Expression mainExpression;
    private Expression toInsertExpression;
    private Expression positionExpression;

    public InsertExpression(
            Expression mainExpression,
            Expression toInsertExpression,
            Expression positionExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.mainExpression = mainExpression;
        this.toInsertExpression = toInsertExpression;
        this.positionExpression = positionExpression;
    }

    public boolean hasPositionExpression() {
        return this.positionExpression != null;
    }

    public Expression getMainExpression() {
        return this.mainExpression;
    }

    public Expression getToInsertExpression() {
        return this.toInsertExpression;
    }

    public Expression getPositionExpression() {
        if (this.positionExpression == null) {
            throw new OurBadException("No position expression present in Insert Expression");
        }
        return this.positionExpression;
    }

    @Override
    public List<Node> getChildren() {
        return this.positionExpression == null
            ? Arrays.asList(this.mainExpression, this.toInsertExpression)
            : Arrays.asList(this.mainExpression, this.toInsertExpression, this.positionExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitInsertExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("insert json ");
        this.toInsertExpression.serializeToJSONiq(sb, 0);
        sb.append(" into ");
        this.mainExpression.serializeToJSONiq(sb, 0);
        if (this.hasPositionExpression()) {
            sb.append(" at position ");
            this.positionExpression.serializeToJSONiq(sb, 0);
        }
        sb.append("\n");
    }
}
