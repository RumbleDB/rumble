package org.rumbledb.runtime.functions.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.items.xml.AttributeItem;
import org.rumbledb.items.xml.DocumentItem;
import org.rumbledb.items.xml.ElementItem;
import org.rumbledb.items.xml.TextItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.List;

public class GetRootFunctionIterator extends LocalFunctionCallIterator {
    public GetRootFunctionIterator(List<RuntimeIterator> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.hasNext = true;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            Item node = getContextNode();
            // TODO: type check that node is XML node type.
            if (
                node instanceof DocumentItem
                    || node instanceof ElementItem
                    || node instanceof AttributeItem
                    || node instanceof TextItem
            ) {
                if (node.parent() == null) {
                    // Node is already the root.
                    return node;
                }
                return node.parent();
            }
            throw new UnsupportedFeatureException(
                    "The argument must be a reference to a supported XML node type",
                    getMetadata()
            );
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " root function", getMetadata());
    }

    private Item getContextNode() {
        if (this.children.isEmpty()) {
            return this.currentDynamicContextForLocalExecution.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
                .get(0);
        }
        return this.children.get(0).materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
    }
}
