package sparksoniq.jsoniq.compiler.translator.expr.operational;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarSingleType;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.UnaryExpressionBase;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

public class CastExpression extends UnaryExpressionBase {

    private FlworVarSingleType _atomicType;

    public CastExpression(Expression _mainExpression, ExpressionMetadata metadata) {
        super(_mainExpression, metadata);
        this._isActive = false;
    }

    public CastExpression(Expression _mainExpression, FlworVarSingleType atomicType, ExpressionMetadata metadata) {
        super(_mainExpression, Operator.CAST, true, metadata);
        this._atomicType = atomicType;
    }


    @Override
    public boolean isActive() {
        return this._isActive;
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitCastExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(castExpr ";
        result += _mainExpression.serializationString(true);
        result += _atomicType != null ? " cast as" + _atomicType.serializationString(prefix) : "";
        result += ")";
        return result;
    }

    public FlworVarSingleType get_atomicType() {
        return _atomicType;
    }
}
