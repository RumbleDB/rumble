package org.rumbledb.runtime.xml.axis.reverse;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.io.Serial;

public class ParentAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public ParentAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext, Axis.PARENT);
    }
}
