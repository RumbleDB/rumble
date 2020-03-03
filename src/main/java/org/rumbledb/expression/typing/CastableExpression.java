package org.rumbledb.expression.typing;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

public class CastableExpression extends Expression {

    protected Expression mainExpression;
    private SequenceType type;

    public CastableExpression(Expression mainExpression, SequenceType type, ExceptionMetadata metadata) {
        super(metadata);
        this.mainExpression = mainExpression;
        this.type = type;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitCastableExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        StringBuilder result = new StringBuilder();
        result.append("(castableExpr ");
        result.append(this.mainExpression.serializationString(true));
        result.append(" castable as " + this.type.toString());
        result.append(")");
        return result.toString();
    }

    public SequenceType getSequenceType() {
        return this.type;
    }

    public Expression getMainExpression() {
        return this.mainExpression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.mainExpression != null) {
            result.add(this.mainExpression);
        }
        return result;
    }
}
