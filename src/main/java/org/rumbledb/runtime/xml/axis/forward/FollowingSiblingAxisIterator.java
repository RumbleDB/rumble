package org.rumbledb.runtime.xml.axis.forward;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FollowingSiblingAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public FollowingSiblingAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext);
    }

    @Override
    protected List<Item> selectAxis(List<Item> contextItems) {
        List<Item> selectedItems = new ArrayList<>();
        for (Item node : contextItems) {
            selectedItems.addAll(getFollowingSiblings(node));
        }
        return selectedItems;
    }

    private List<Item> getFollowingSiblings(Item node) {
        Item parent = node.parent();
        if (parent == null || parent.isNull()) {
            return Collections.emptyList();
        }
        List<Item> result = new ArrayList<>();
        List<Item> parentChildren = parent.children();
        int siblingsStartIndex = 0;
        for (int i = 0; i < parentChildren.size(); ++i) {
            if (parentChildren.get(i).equals(node)) {
                siblingsStartIndex = i + 1;
                break;
            }
        }
        for (int i = siblingsStartIndex; i > 0 && i < parentChildren.size(); ++i) {
            result.add(parentChildren.get(i));
        }
        return result;
    }
}
