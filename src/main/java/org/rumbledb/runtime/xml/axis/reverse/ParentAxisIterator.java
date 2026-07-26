package org.rumbledb.runtime.xml.axis.reverse;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ParentAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public ParentAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext);
    }

    @Override
    protected List<Item> selectAxis(List<Item> contextItems) {
        List<Item> selectedItems = new ArrayList<>();
        for (Item node : contextItems) {
            Item parent = node.parent();
            if (parent != null) {
                selectedItems.add(parent);
            }
        }
        return selectedItems;
    }
}
