package org.rumbledb.expressions.xml.axis;

import org.rumbledb.expressions.xml.node_test.NodeTest;

public class ForwardStep implements Step {
    private ForwardAxis forwardAxis;
    private NodeTest nodeTest;

    public ForwardStep(ForwardAxis forwardAxis, NodeTest nodeTest) {
        this.forwardAxis = forwardAxis;
        this.nodeTest = nodeTest;
    }
}
