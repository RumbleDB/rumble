package org.rumbledb.expressions.xml.axis;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.runtime.xml.axis.AxisIterator;
import org.rumbledb.runtime.xml.axis.AxisIteratorVisitor;

import java.util.Collections;
import java.util.List;

public class EmptyStepExpr extends StepExpr {

    public EmptyStepExpr(ExceptionMetadata metadata) {
        super(metadata);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
    }

    @Override
    public AxisIterator accept(AxisIteratorVisitor visitor, RuntimeStaticContext staticContext) {
        return null;
    }

    @Override
    public NodeTest getNodeTest() {
        return null;
    }
}
