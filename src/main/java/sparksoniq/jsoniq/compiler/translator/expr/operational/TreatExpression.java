package sparksoniq.jsoniq.compiler.translator.expr.operational;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarSequenceType;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.UnaryExpressionBase;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.types.SequenceType;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;


public class TreatExpression extends UnaryExpressionBase {

    private FlworVarSequenceType _sequenceType;

    public TreatExpression(Expression _mainExpression, ExpressionMetadata metadata) {
        super(_mainExpression, metadata);
        this._isActive = false;
    }

    public TreatExpression(
            Expression _mainExpression,
            FlworVarSequenceType sequenceType,
            ExpressionMetadata metadata
    ) {
        super(_mainExpression, Operator.TREAT, true, metadata);
        this._sequenceType = sequenceType;
    }

    public FlworVarSequenceType getsequenceType() {
        return _sequenceType;
    }

    @Override
    protected void initHighestExecutionMode() {
        SequenceType sequenceType = _sequenceType.getSequence();
        this._highestExecutionMode = calculateIsRDDFromSequenceTypeAndExpression(sequenceType, this._mainExpression);
    }

    public static ExecutionMode calculateIsRDDFromSequenceTypeAndExpression(SequenceType sequenceType, Expression expression) {
        if (sequenceType.getArity() != SequenceType.Arity.One
            && sequenceType.getArity() != SequenceType.Arity.OneOrZero
            && expression.getHighestExecutionMode().isRDD()) {
            return ExecutionMode.RDD;
        }
        return ExecutionMode.LOCAL;
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
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
