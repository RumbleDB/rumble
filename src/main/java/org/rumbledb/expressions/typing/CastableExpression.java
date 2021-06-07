package org.rumbledb.expressions.typing;

import java.util.Collections;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

public class CastableExpression extends Expression {

    private static final long serialVersionUID = 1L;

    protected Expression mainExpression;
    private SequenceType sequenceType;

    public CastableExpression(Expression mainExpression, SequenceType type, ExceptionMetadata metadata) {
        super(metadata);
        this.mainExpression = mainExpression;
        this.sequenceType = type;
        if (mainExpression == null) {
            throw new OurBadException("Expression cannot be null.");
        }
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

    public SequenceType getSequenceType() {
        return this.sequenceType;
    }

    public Expression getMainExpression() {
        return this.mainExpression;
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.mainExpression);
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" (" + (this.sequenceType.toString()) + ") ");
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(" | " + (this.inferredSequenceType == null ? "not set" : this.inferredSequenceType));
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("(\n");

        this.mainExpression.serializeToJSONiq(sb, indent + 1);

        indentIt(sb, indent);
        sb.append(")\n");

        indentIt(sb, indent);
        sb.append("castable as\n");

        indentIt(sb, indent);
        sb.append("(\n");

        indentIt(sb, indent);
        this.sequenceType.toString();
        sb.append("\n");

        indentIt(sb, indent);
        sb.append(")\n");
    }
}
