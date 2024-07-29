package org.rumbledb.expressions.xml.axis;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.runtime.xml.axis.AxisIterator;
import org.rumbledb.runtime.xml.axis.AxisIteratorVisitor;

public class ForwardStep implements Step {
    private ForwardAxis forwardAxis;
    private NodeTest nodeTest;

    public ForwardStep(ForwardAxis forwardAxis, NodeTest nodeTest) {
        this.forwardAxis = forwardAxis;
        this.nodeTest = nodeTest;
    }

    @Override
    public String toString() {
        return forwardAxis.getAxisValue() + nodeTest.toString();
    }

    public ForwardAxis getForwardAxis() {
        return forwardAxis;
    }

    @Override
    public AxisIterator accept(AxisIteratorVisitor visitor, RuntimeStaticContext staticContext) {
        return visitor.visit(this, staticContext);
    }

    @Override
    public NodeTest getNodeTest() {
        return nodeTest;
    }
}
