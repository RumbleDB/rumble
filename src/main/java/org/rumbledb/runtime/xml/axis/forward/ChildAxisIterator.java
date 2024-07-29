package org.rumbledb.runtime.xml.axis.forward;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedNodeException;
import org.rumbledb.runtime.xml.axis.AxisIterator;

import java.util.List;

public class ChildAxisIterator extends AxisIterator {

    public ChildAxisIterator(RuntimeStaticContext staticContext) {
        super(staticContext);
    }

    protected void setNextResult() {
        if (this.results == null) {
            List<Item> currentContext = this.currentDynamicContextForLocalExecution.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata());
            if (currentContext.isEmpty()) {
                throw new UnexpectedNodeException("Expected at least a node type as context item", getMetadata());
            }
            Item node = currentContext.get(0);
            this.results = node.children();
        }
        if (this.resultCounter < this.results.size()) {
            this.nextResult = this.results.get(this.resultCounter++);
        } else {
            this.hasNext = false;
        }
    }
}
