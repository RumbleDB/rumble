package org.rumbledb.expressions.operational;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.flowr.FlworVarSingleType;
import org.rumbledb.expressions.operational.base.UnaryExpressionBase;

import sparksoniq.semantics.visitor.AbstractNodeVisitor;

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
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
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
