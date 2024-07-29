package org.rumbledb.expressions.xml.axis;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.runtime.xml.axis.AxisIterator;
import org.rumbledb.runtime.xml.axis.AxisIteratorVisitor;

public class NoStep implements Step {

    @Override
    public String toString() {
        return "";
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
