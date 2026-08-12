package org.rumbledb.expressions.xml.axis;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.runtime.xml.axis.AxisIterator;
import org.rumbledb.runtime.xml.axis.AxisIteratorVisitor;

public class ForwardStepExpr extends StepExpr {
    @Getter
    private final ForwardAxis forwardAxis;

    private final NodeTest nodeTest;

    public ForwardStepExpr(ForwardAxis forwardAxis, NodeTest nodeTest, ExceptionMetadata exceptionMetadata) {
        super(exceptionMetadata);
        this.forwardAxis = forwardAxis;
        this.nodeTest = nodeTest;
    }

    @Override
    public List<Node> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append(this.forwardAxis.getAxisValue());
        sb.append(this.nodeTest.toString());
    }

    @Override
    public AxisIterator accept(AxisIteratorVisitor visitor, RuntimeStaticContext staticContext) {
        return visitor.visit(this, staticContext);
    }

    @Override
    public NodeTest getNodeTest() {
        return this.nodeTest;
    }
}
