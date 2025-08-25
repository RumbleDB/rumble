package org.rumbledb.expressions.typing;

import java.util.Collections;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

public class ValidateTypeExpression extends Expression {

    private Expression mainExpression;
    private SequenceType sequenceType;
    private boolean isValidate;

    public ValidateTypeExpression(
            Expression mainExpression,
            boolean isValidate,
            SequenceType sequenceType,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("Expression cannot be null.");
        }
        this.mainExpression = mainExpression;
        this.isValidate = isValidate;
        this.sequenceType = sequenceType;
        if (sequenceType.isEmptySequence()) {
            throw new OurBadException(
                    "It is not possible to validate against the empty sequence type. Please use empty() instead to check for emptiness."
            );
        }
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitValidateTypeExpression(this, argument);
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
        buffer.append(
            " ("
                + (this.sequenceType.toString())
                + (this.getSequenceType().isResolved() ? " (resolved)" : " (unresolved)")
                + ") "
        );
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(" | " + this.expressionClassification);
        buffer.append(
            " | "
                + (this.staticSequenceType == null
                    ? "not set"
                    : this.staticSequenceType
                        + (this.staticSequenceType.isResolved() ? " (resolved)" : " (unresolved)"))
        );
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append(" validate type " + this.sequenceType.toString() + "\n");
        this.mainExpression.serializeToJSONiq(sb, 0);
        sb.append(" as {" + this.sequenceType.toString() + "\n}\n");
    }

    public boolean isValidate() {
        return this.isValidate;
    }
}
