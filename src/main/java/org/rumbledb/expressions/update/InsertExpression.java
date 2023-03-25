package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.ExpressionClassification;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class InsertExpression extends Expression {

    private Expression mainExpression;
    private Expression toInsertExpression;
    private Expression positionExpression;

    public InsertExpression(Expression mainExpression,
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
        return positionExpression != null;
    }

    public Expression getMainExpression() {
        return mainExpression;
    }

    public Expression getToInsertExpression() {
        return toInsertExpression;
    }

    public Expression getPositionExpression() {
        if (positionExpression == null) {
            throw new OurBadException("No position expression present in Insert Expression");
        }
        return positionExpression;
    }

    @Override
    public List<Node> getChildren() {
        return this.positionExpression == null ?
                Arrays.asList(mainExpression, toInsertExpression) :
                Arrays.asList(mainExpression, toInsertExpression, positionExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitInsertExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("insert json ");
        toInsertExpression.serializeToJSONiq(sb, 0);
        sb.append(" into ");
        mainExpression.serializeToJSONiq(sb, 0);
        if (this.hasPositionExpression()) {
            sb.append(" at position ");
            positionExpression.serializeToJSONiq(sb, 0);
        }
        sb.append("\n");
    }
}
