package org.rumbledb.expressions.xml.axis;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.runtime.xml.axis.AxisIterator;
import org.rumbledb.runtime.xml.axis.AxisIteratorVisitor;

public class ReverseStep implements Step {
    private ReverseAxis reverseAxis;
    private NodeTest nodeTest;

    public ReverseStep(ReverseAxis reverseAxis, NodeTest nodeTest) {
        this.reverseAxis = reverseAxis;
        this.nodeTest = nodeTest;
    }

    @Override
    public String toString() {
        return reverseAxis.getAxisValue() + nodeTest.toString();
    }

    public ReverseAxis getReverseAxis() {
        return this.reverseAxis;
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
