package org.rumbledb.expressions.typing;

import java.util.Collections;
import java.util.List;

import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;


public class TreatExpression extends Expression {

    private Expression mainExpression;
    private SequenceType sequenceType;
    private ErrorCode errorCode;

    public TreatExpression(
            Expression mainExpression,
            SequenceType sequenceType,
            ErrorCode errorCode,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("Expression cannot be null.");
        }
        this.errorCode = errorCode;
        this.mainExpression = mainExpression;
        this.sequenceType = sequenceType;
    }

    public SequenceType getsequenceType() {
        return this.sequenceType;
    }

    public ErrorCode errorCodeThatShouldBeThrown() {
        return this.errorCode;
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        this.highestExecutionMode = calculateIsRDDFromSequenceTypeAndExpression(
            this.sequenceType,
            this.mainExpression,
            visitorConfig
        );
    }

    private static ExecutionMode calculateIsRDDFromSequenceTypeAndExpression(
            SequenceType sequenceType,
            Expression expression,
            VisitorConfig visitorConfig
    ) {
        if (
            !sequenceType.isEmptySequence()
                && sequenceType.getArity() != SequenceType.Arity.One
                && sequenceType.getArity() != SequenceType.Arity.OneOrZero
                && expression.getHighestExecutionMode(visitorConfig).isRDDOrDataFrame()
        ) {
            return ExecutionMode.RDD;
        }
        return ExecutionMode.LOCAL;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTreatExpression(this, argument);
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
        this.mainExpression.serializeToJSONiq(sb, 0);
        sb.append(" treat as " + this.sequenceType.toString() + "\n");
    }

}
