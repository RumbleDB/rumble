package org.rumbledb.runtime.xml.axis.reverse;

import java.io.Serial;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

public class PrecedingAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public PrecedingAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext, Axis.PRECEDING);
    }
}
