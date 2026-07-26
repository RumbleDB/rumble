package org.rumbledb.runtime.xml.axis.forward;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FollowingAxisIterator extends AxisIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public FollowingAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext);
    }

    @Override
    protected List<Item> selectAxis(List<Item> contextItems) {
        List<Item> selectedItems = new ArrayList<>();
        for (Item node : contextItems) {
            selectedItems.addAll(getFollowingNodes(node.parent(), node));
        }
        return selectedItems;
    }

    /*
     * Method adds the rest of the parent's descendants following the current node. Afterward, it visits the parent's
     * parent and adds its descendants following the parent.
     */
    private List<Item> getFollowingNodes(Item parent, Item node) {
        if (parent == null) {
            return Collections.emptyList();
        }
        List<Item> followingNodes = new ArrayList<>();
        List<Item> parentChildren = parent.children();
        int followingIndex = -1;
        for (int i = 0; i < parentChildren.size(); ++i) {
            if (parentChildren.get(i).equals(node)) {
                followingIndex = i + 1;
                break;
            }
        }
        for (int i = followingIndex; i > 0 && i < parentChildren.size(); ++i) {
            followingNodes.addAll(getDescendantsOrSelf(parentChildren.get(i)));
        }
        followingNodes.addAll(getFollowingNodes(parent.parent(), parent));
        return followingNodes;
    }
}
