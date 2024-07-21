package org.rumbledb.expressions.xml;

import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.xml.axis.AxisStep;

import java.util.List;

public class StepExpr extends Node {
    private Expression postFixExpr;
    private AxisStep axisStep;

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return null;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {

    }
}
