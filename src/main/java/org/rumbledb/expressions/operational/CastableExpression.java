package org.rumbledb.expressions.operational;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.flowr.FlworVarSingleType;
import org.rumbledb.expressions.operational.base.UnaryExpressionBase;

public class CastableExpression extends UnaryExpressionBase {

    private FlworVarSingleType atomicType;

    public CastableExpression(Expression mainExpression, FlworVarSingleType atomicType, ExceptionMetadata metadata) {
        super(mainExpression, Operator.CASTABLE, metadata);
        this.atomicType = atomicType;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitCastableExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(castableExpr ";
        result += this.mainExpression.serializationString(true);
        result += this.atomicType != null ? " castable as " + this.atomicType.serializationString(prefix) : "";
        result += ")";
        return result;
    }

    public FlworVarSingleType getAtomicType() {
        return this.atomicType;
    }
}
