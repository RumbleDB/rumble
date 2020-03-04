package org.rumbledb.expressions.operational;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.operational.base.UnaryExpressionBase;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

public class CastExpression extends UnaryExpressionBase {

    private SequenceType type;

    public CastExpression(Expression mainExpression, SequenceType type, ExceptionMetadata metadata) {
        super(mainExpression, Operator.CAST, metadata);
        this.type = type;
        if (type.getArity() != Arity.OneOrZero && type.getArity() != Arity.One) {
            throw new OurBadException(
                    "Cast expressions cannot have an arity of more than one, something went wrong with the parser."
            );
        }
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
