package org.rumbledb.expressions.xml;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.xml.axis.AxisStep;

import java.util.Collections;
import java.util.List;

public class StepExpr extends Expression {
    private Expression postFixExpr;
    private AxisStep axisStep;

    public StepExpr(Expression postFixExpr, ExceptionMetadata exceptionMetadata) {
        super(exceptionMetadata);
        this.postFixExpr = postFixExpr;
        this.axisStep = null;
    }

    public StepExpr(AxisStep axisStep, ExceptionMetadata exceptionMetadata) {
        super(exceptionMetadata);
        this.postFixExpr = null;
        this.axisStep = axisStep;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitStepExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        if (this.postFixExpr != null) {
            return Collections.singletonList(this.postFixExpr);
        }
        return Collections.singletonList(this.axisStep);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        if (this.postFixExpr != null) {
            this.postFixExpr.serializeToJSONiq(sb, indent);
        } else {
            this.axisStep.serializeToJSONiq(sb, indent);
        }
    }

    public Node getPostFixExpr() {
        return this.postFixExpr;
    }

    public AxisStep getAxisStep() {
        return this.axisStep;
    }
}
