package org.rumbledb.runtime.xml.axis.forward;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedNodeException;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FollowingAxisIterator extends AxisIterator {
    public FollowingAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext);
    }

    @Override
    protected void setNextResult() {
        if (this.results == null) {
            List<Item> currentContext = this.currentDynamicContextForLocalExecution.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata());
            if (currentContext.isEmpty()) {
                throw new UnexpectedNodeException("Expected at least a node type as context item", getMetadata());
            }
            // TODO: add duplicate elimination
            Item node = currentContext.get(0);
            this.results = getFollowingNodes(node.parent(), node);
        }
        storeNextResult();
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
            }
        }
        for (int i = followingIndex; i > 0 && i < parentChildren.size(); ++i) {
            followingNodes.addAll(getDescendants(parentChildren.get(i)));
        }
        followingNodes.addAll(getFollowingNodes(parent.parent(), parent));
        return followingNodes;
    }
}
