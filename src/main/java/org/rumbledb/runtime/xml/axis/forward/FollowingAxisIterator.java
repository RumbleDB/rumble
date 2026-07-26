package org.rumbledb.runtime.xml.axis.forward;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.io.Serial;

public class FollowingAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public FollowingAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext, Axis.FOLLOWING);
    }
}
