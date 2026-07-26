package org.rumbledb.runtime.xml.axis.forward;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ChildAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public ChildAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext, ResultOrder.PRESERVE_SELECTION_ORDER);
    }

    @Override
    protected List<Item> selectAxis(List<Item> contextItems) {
        List<Item> selectedItems = new ArrayList<>();
        for (Item node : contextItems) {
            selectedItems.addAll(node.children());
        }
        return selectedItems;
    }
}
