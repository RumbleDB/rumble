package sparksoniq.jsoniq.compiler.translator.expr.operational;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarSingleType;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.UnaryExpressionBase;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

public class CastExpression extends UnaryExpressionBase {

    private FlworVarSingleType _singleType;

    public CastExpression(Expression _mainExpression, ExceptionMetadata metadata) {
        super(_mainExpression, metadata);
        this._isActive = false;
    }

    public CastExpression(Expression _mainExpression, FlworVarSingleType singleType, ExceptionMetadata metadata) {
        super(_mainExpression, Operator.CAST, true, metadata);
        this._singleType = singleType;
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
        result += _singleType != null ? " cast as" + _singleType.serializationString(prefix) : "";
        result += ")";
        return result;
    }

    public FlworVarSingleType getFlworVarSingleType() {
        return _singleType;
    }
}
