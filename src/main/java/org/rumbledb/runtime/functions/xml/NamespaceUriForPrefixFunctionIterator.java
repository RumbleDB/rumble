package org.rumbledb.runtime.functions.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class NamespaceUriForPrefixFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;

    public NamespaceUriForPrefixFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item prefixItem = this.children.get(0).materializeFirstItemOrNull(context);
        String prefix = prefixItem == null ? "" : prefixItem.getStringValue();
        Item element = this.children.get(1).materializeFirstItemOrNull(context);
        for (Item namespaceNode : element.namespaceNodes()) {
            Name name = namespaceNode.nodeName();
            String namespacePrefix = name == null ? "" : name.getLocalName();
            if (namespacePrefix.equals(prefix)) {
                return ItemFactory.getInstance().createAnyURIItem(namespaceNode.getStringValue());
            }
        }
        return null;
    }
}
