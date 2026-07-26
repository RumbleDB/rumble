package org.rumbledb.runtime.xml.axis.reverse;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrecedingAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public PrecedingAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext);
    }


    @Override
    protected List<Item> selectAxis(List<Item> contextItems) {
        List<Item> selectedItems = new ArrayList<>();
        for (Item node : contextItems) {
            selectedItems.addAll(getPrecedingNode(node.parent(), node));
        }
        return selectedItems;
    }

    /*
     * Method adds the rest of the parent's descendants preceding the current node. Afterward, it visits the parent's
     * parent and adds its descendants preceding the parent.
     */
    private List<Item> getPrecedingNode(Item parent, Item node) {
        if (parent == null) {
            return Collections.emptyList();
        }
        List<Item> precedingNodes = new ArrayList<>();
        List<Item> parentChildren = parent.children();
        int nodeIndex = parentChildren.size();
        for (int i = 0; i < parentChildren.size(); ++i) {
            if (parentChildren.get(i).equals(node)) {
                nodeIndex = i;
                break;
            }
        }
        for (int i = 0; i < nodeIndex; ++i) {
            precedingNodes.addAll(getDescendantsOrSelf(parentChildren.get(i)));
        }
        precedingNodes.addAll(getPrecedingNode(parent.parent(), parent));
        return precedingNodes;
    }
}
