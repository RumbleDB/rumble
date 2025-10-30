package org.rumbledb.runtime.xml.axis.forward;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedNodeException;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FollowingSiblingAxisIterator extends AxisIterator {
    private static final long serialVersionUID = 1L;
    public FollowingSiblingAxisIterator(RuntimeStaticContext staticContext) {
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
            for (Item node : currentContext) {
                this.results.addAll(getFollowingSiblings(node));
            }
        }
        storeNextResult();
    }

    private List<Item> getFollowingSiblings(Item node) {
        if (node.parent().isNull()) {
            return Collections.emptyList();
        }
        List<Item> result = new ArrayList<>();
        List<Item> parentChildren = node.parent().children();
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
