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

public class ReverseStepExpr extends StepExpr {
    @Getter
    private final ReverseAxis reverseAxis;

    private final NodeTest nodeTest;

    public ReverseStepExpr(ReverseAxis reverseAxis, NodeTest nodeTest, ExceptionMetadata exceptionMetadata) {
        super(exceptionMetadata);
        this.reverseAxis = reverseAxis;
        this.nodeTest = nodeTest;
    }

    @Override
    public List<Node> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append(this.reverseAxis.getAxisValue());
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
