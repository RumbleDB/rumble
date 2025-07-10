package org.rumbledb.expressions.xml;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.runtime.xml.axis.AxisIterator;
import org.rumbledb.runtime.xml.axis.AxisIteratorVisitor;

public abstract class StepExpr extends Expression {
    public StepExpr(ExceptionMetadata metadata) {
        super(metadata);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitStepExpr(this, argument);
    }

    public abstract AxisIterator accept(AxisIteratorVisitor visitor, RuntimeStaticContext staticContext);

    public abstract NodeTest getNodeTest();
}
