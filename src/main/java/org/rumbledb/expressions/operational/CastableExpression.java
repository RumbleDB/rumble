package org.rumbledb.expressions.operational;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.flowr.FlworVarSingleType;
import org.rumbledb.expressions.operational.base.UnaryExpressionBase;

import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

public class CastableExpression extends UnaryExpressionBase {

    private FlworVarSingleType _atomicType;

    public CastableExpression(Expression _mainExpression, ExceptionMetadata metadata) {
        super(_mainExpression, metadata);
        this._isActive = false;
    }

    public CastableExpression(Expression _mainExpression, FlworVarSingleType atomicType, ExceptionMetadata metadata) {
        super(_mainExpression, Operator.CASTABLE, true, metadata);
        this._atomicType = atomicType;
    }


    @Override
    public boolean isActive() {
        return this._isActive;
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitCastableExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(castableExpr ";
        result += _mainExpression.serializationString(true);
        result += _atomicType != null ? " castable as " + _atomicType.serializationString(prefix) : "";
        result += ")";
        return result;
    }

    public FlworVarSingleType get_atomicType() {
        return _atomicType;
    }
}
