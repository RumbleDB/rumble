package org.rumbledb.runtime.functions.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.util.List;

public class NamespaceUriFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public NamespaceUriFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item node = getContextNode(context);
        if (node == null) {
            return ItemFactory.getInstance().createAnyURIItem("");
        }
        if (!node.isNode()) {
            throw new UnexpectedTypeException("The argument must be a reference to an XML node", getMetadata());
        }

        Name nodeName = node.nodeName();
        String namespaceUri = nodeName == null ? "" : nodeName.getNamespace();
        return ItemFactory.getInstance().createAnyURIItem(namespaceUri == null ? "" : namespaceUri);
    }

    private Item getContextNode(DynamicContext context) {
        if (this.getNumberOfChildren() > 0) {
            return this.getChild(0).materializeFirstItemOrNull(context);
        }
        return context.getVariableValues()
            .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
            .get(0);
    }
}
