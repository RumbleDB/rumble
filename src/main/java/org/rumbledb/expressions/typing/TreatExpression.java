package org.rumbledb.expressions.typing;

import java.util.Collections;
import java.util.List;

import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
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
        if (mainExpression == null) {
            throw new OurBadException("Expression cannot be null.");
        }
        this.mainExpression = mainExpression;
        this.sequenceType = sequenceType;
    }

    public SequenceType getsequenceType() {
        return this.sequenceType;
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        this.highestExecutionMode = calculateIsRDDFromSequenceTypeAndExpression(
            this.sequenceType,
            this.mainExpression,
            visitorConfig
        );
    }

    public static ExecutionMode calculateIsRDDFromSequenceTypeAndExpression(
            SequenceType sequenceType,
            Expression expression,
            VisitorConfig visitorConfig
    ) {
        if (
            !sequenceType.isEmptySequence()
                && sequenceType.getArity() != SequenceType.Arity.One
                && sequenceType.getArity() != SequenceType.Arity.OneOrZero
                && expression.getHighestExecutionMode(visitorConfig).isRDD()
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
        return Collections.singletonList(this.mainExpression);
    }

}
