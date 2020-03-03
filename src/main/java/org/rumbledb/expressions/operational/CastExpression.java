package org.rumbledb.expressions.operational;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.operational.base.UnaryExpressionBase;
import org.rumbledb.types.SequenceType;

public class CastExpression extends UnaryExpressionBase {

    private SequenceType type;

    public CastExpression(Expression mainExpression, SequenceType type, ExceptionMetadata metadata) {
        super(mainExpression, Operator.CAST, metadata);
        this.type = type;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitCastExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(castExpr ";
        result += this.mainExpression.serializationString(true);
        result += " cast as" + this.type.toString();
        result += ")";
        return result;
    }

    public SequenceType getSequenceType() {
        return this.type;
    }
}
