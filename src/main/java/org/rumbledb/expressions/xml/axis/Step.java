package org.rumbledb.expressions.xml.axis;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.runtime.xml.axis.AxisIterator;
import org.rumbledb.runtime.xml.axis.AxisIteratorVisitor;

public interface Step {
    public AxisIterator accept(AxisIteratorVisitor visitor, RuntimeStaticContext staticContext);

    public NodeTest getNodeTest();
}
