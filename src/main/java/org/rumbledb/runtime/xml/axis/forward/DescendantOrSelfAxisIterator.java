package org.rumbledb.runtime.xml.axis.forward;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.io.Serial;

public class DescendantOrSelfAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public DescendantOrSelfAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext, Axis.DESCENDANT_OR_SELF);
    }
}
