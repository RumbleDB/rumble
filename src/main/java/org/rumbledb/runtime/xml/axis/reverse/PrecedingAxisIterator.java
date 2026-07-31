package org.rumbledb.runtime.xml.axis.reverse;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.io.Serial;

public class PrecedingAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public PrecedingAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext, Axis.PRECEDING);
    }
}
