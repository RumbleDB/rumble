package org.rumbledb.expressions.xml.axis;

import org.rumbledb.expressions.xml.node_test.NodeTest;

public class ForwardStep implements Step {
    private ForwardAxis forwardAxis;
    private NodeTest nodeTest;
    private boolean hasAbbreviatedForwardStep;

    public ForwardStep(NodeTest nodeTest) {
        this.forwardAxis = null;
        this.nodeTest = nodeTest;
        this.hasAbbreviatedForwardStep = true;
    }

    public ForwardStep(ForwardAxis forwardAxis, NodeTest nodeTest) {
        this.forwardAxis = forwardAxis;
        this.nodeTest = nodeTest;
    }
}
