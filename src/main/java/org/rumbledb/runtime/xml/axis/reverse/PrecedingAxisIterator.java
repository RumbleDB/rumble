package org.rumbledb.runtime.xml.axis.reverse;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedNodeException;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrecedingAxisIterator extends AxisIterator {
    public PrecedingAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext);
    }


    @Override
    protected void setNextResult() {
        if (this.results == null) {
            this.results = new ArrayList<>();
            List<Item> currentContext = this.currentDynamicContextForLocalExecution.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata());
            if (currentContext.isEmpty()) {
                throw new UnexpectedNodeException("Expected at least a node type as context item", getMetadata());
            }
            for (Item node: currentContext) {
                this.results.addAll(getPrecedingNode(node.parent(), node));
            }
        }
        storeNextResult();
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
            }
        }
        for (int i = 0; i < nodeIndex; ++i) {
            precedingNodes.addAll(getDescendants(parentChildren.get(i)));
        }
        precedingNodes.addAll(getPrecedingNode(parent.parent(), parent));
        return precedingNodes;
    }
}
