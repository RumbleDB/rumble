package org.rumbledb.runtime.functions.xml;


import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;

public class NamespaceUriForPrefixFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public NamespaceUriForPrefixFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return evaluate(context);
    }

    private Item evaluate(DynamicContext context) {
        Item prefixItem = this.getChild(0).materializeFirstOrNull(context);
        String prefix = prefixItem == null ? "" : prefixItem.getStringValue();
        Item element = this.getChild(1).materializeFirstOrNull(context);
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
