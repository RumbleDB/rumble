package org.rumbledb.runtime.xml.axis.reverse;

import java.io.Serial;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

public class AncestorOrSelfAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public AncestorOrSelfAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext, Axis.ANCESTOR_OR_SELF);
    }
}
