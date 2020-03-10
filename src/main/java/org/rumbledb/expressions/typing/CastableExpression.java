package org.rumbledb.expressions.typing;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

public class CastableExpression extends Expression {

    protected Expression mainExpression;
    private SequenceType sequenceType;

    public CastableExpression(Expression mainExpression, SequenceType type, ExceptionMetadata metadata) {
        super(metadata);
        this.mainExpression = mainExpression;
        this.sequenceType = type;
        if (type.getArity() != Arity.OneOrZero && type.getArity() != Arity.One) {
            throw new OurBadException(
                    "Castable expressions cannot have an arity of more than one, something went wrong with the parser."
            );
        }
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
        result.append(" castable as " + this.sequenceType.toString());
        result.append(")");
        return result.toString();
    }

    public SequenceType getSequenceType() {
        return this.sequenceType;
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
