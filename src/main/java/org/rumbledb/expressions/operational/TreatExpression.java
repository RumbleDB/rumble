package org.rumbledb.expressions.operational;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.flowr.FlworVarSequenceType;
import org.rumbledb.expressions.operational.base.UnaryExpressionBase;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.types.SequenceType;
import sparksoniq.semantics.visitor.AbstractNodeVisitor;


public class TreatExpression extends UnaryExpressionBase {

    private FlworVarSequenceType sequenceType;

    public TreatExpression(Expression mainExpression, ExceptionMetadata metadata) {
        super(mainExpression, metadata);
        this.isActive = false;
    }

    public TreatExpression(
            Expression mainExpression,
            FlworVarSequenceType sequenceType,
            ExceptionMetadata metadata
    ) {
        super(mainExpression, Operator.TREAT, true, metadata);
        this.sequenceType = sequenceType;
    }

    public FlworVarSequenceType getsequenceType() {
        return this.sequenceType;
    }

    @Override
    public void initHighestExecutionMode() {
        if (bypassCurrentExpressionForExecutionModeOperations()) {
            return;
        }
        SequenceType sequenceType = this.sequenceType.getSequence();
        this.highestExecutionMode = calculateIsRDDFromSequenceTypeAndExpression(sequenceType, this.mainExpression);
    }

    public static ExecutionMode calculateIsRDDFromSequenceTypeAndExpression(
            SequenceType sequenceType,
            Expression expression
    ) {
        if (
            sequenceType.getArity() != SequenceType.Arity.One
                && sequenceType.getArity() != SequenceType.Arity.OneOrZero
                && expression.getHighestExecutionMode().isRDD()
        ) {
            return ExecutionMode.RDD;
        }
        return ExecutionMode.LOCAL;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTreatExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(treatExpr ";
        result += this.mainExpression.serializationString(true);
        result += this.sequenceType != null ? " treat as " + this.sequenceType.serializationString(prefix) : "";
        result += ")";
        return result;
    }

}
