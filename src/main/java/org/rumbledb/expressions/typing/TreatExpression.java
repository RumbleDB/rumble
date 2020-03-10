package org.rumbledb.expressions.typing;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;


public class TreatExpression extends Expression {

    private Expression mainExpression;
    private SequenceType sequenceType;

    public TreatExpression(
            Expression mainExpression,
            SequenceType sequenceType,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.sequenceType = sequenceType;
    }

    public SequenceType getsequenceType() {
        return this.sequenceType;
    }

    @Override
    public void initHighestExecutionMode() {
        this.highestExecutionMode = calculateIsRDDFromSequenceTypeAndExpression(this.sequenceType, this.mainExpression);
    }

    public static ExecutionMode calculateIsRDDFromSequenceTypeAndExpression(
            SequenceType sequenceType,
            Expression expression
    ) {
        if (
            !sequenceType.isEmptySequence()
                && sequenceType.getArity() != SequenceType.Arity.One
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
        result += " treat as " + this.sequenceType.toString();
        result += ")";
        return result;
    }

    public SequenceType getSequenceType() {
        return this.sequenceType;
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
