package org.rumbledb.expressions.xml;

import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Collections;
import java.util.List;

public class StepExpr extends Node {
    private Expression postFixExpr;
    private AxisStep axisStep;

    public StepExpr(Expression postFixExpr) {
        this.postFixExpr = postFixExpr;
        this.axisStep = null;
    }

    public StepExpr(AxisStep axisStep) {
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
        sb.append("\n");
    }
}
