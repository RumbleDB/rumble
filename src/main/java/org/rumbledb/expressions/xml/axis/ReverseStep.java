package org.rumbledb.expressions.xml.axis;

import org.rumbledb.expressions.xml.node_test.NodeTest;

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
}
