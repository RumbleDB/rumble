package org.rumbledb.expressions.operational;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.flowr.FlworVarSequenceType;
import org.rumbledb.expressions.operational.base.UnaryExpressionBase;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.types.SequenceType;
import sparksoniq.semantics.visitor.AbstractNodeVisitor;


public class TreatExpression extends UnaryExpressionBase {

    private FlworVarSequenceType _sequenceType;

    public TreatExpression(Expression _mainExpression, ExceptionMetadata metadata) {
        super(_mainExpression, metadata);
        this._isActive = false;
    }

    public TreatExpression(
            Expression _mainExpression,
            FlworVarSequenceType sequenceType,
            ExceptionMetadata metadata
    ) {
        super(_mainExpression, Operator.TREAT, true, metadata);
        this._sequenceType = sequenceType;
    }

    public FlworVarSequenceType getsequenceType() {
        return _sequenceType;
    }

    @Override
    public void initHighestExecutionMode() {
        if (bypassCurrentExpressionForExecutionModeOperations()) {
            return;
        }
        SequenceType sequenceType = _sequenceType.getSequence();
        this._highestExecutionMode = calculateIsRDDFromSequenceTypeAndExpression(sequenceType, this._mainExpression);
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
        result += _mainExpression.serializationString(true);
        result += _sequenceType != null ? " treat as " + _sequenceType.serializationString(prefix) : "";
        result += ")";
        return result;
    }

}
