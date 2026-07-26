package org.rumbledb.runtime.xml.axis.forward;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.io.Serial;
import java.util.List;

public class SelfAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public SelfAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext);
    }

    @Override
    protected List<Item> selectAxis(List<Item> contextItems) {
        return contextItems;
    }
}
