package org.rumbledb.expressions.operational;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.FlworVarSingleType;

public class CastableExpression extends Expression {

    protected Expression mainExpression;
    private FlworVarSingleType atomicType;

    public CastableExpression(Expression mainExpression, FlworVarSingleType atomicType, ExceptionMetadata metadata) {
        super(metadata);
        this.mainExpression = mainExpression;
        this.atomicType = atomicType;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitCastableExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        StringBuilder result = new StringBuilder();
        result.append("(castableExpr ");
        result.append(this.mainExpression.serializationString(true));
        result.append(this.atomicType != null ? " castable as " + this.atomicType.serializationString(prefix) : "");
        result.append(")");
        return result.toString();
    }

    public FlworVarSingleType getAtomicType() {
        return this.atomicType;
    }

    public Expression getMainExpression() {
        return this.mainExpression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.mainExpression != null) {
            result.add(this.mainExpression);
        }
        return result;
    }
}
