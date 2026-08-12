package org.rumbledb.runtime.xml.axis.forward;

import java.io.Serial;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

public class ChildAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public ChildAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext, Axis.CHILD, ResultOrder.PRESERVE_SELECTION_ORDER);
    }
}
