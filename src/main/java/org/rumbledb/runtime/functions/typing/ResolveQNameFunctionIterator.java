package org.rumbledb.runtime.functions.typing;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.xml.NamespaceBindingUtils;

public class ResolveQNameFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResolveQNameFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item qnameItem = this.getChild(0).materializeFirstOrNull(context);
        if (qnameItem == null) {
            return null;
        }

        Item element = this.getChild(1).materializeFirstOrNull(context);
        if (element == null || !element.isElementNode()) {
            throw new UnexpectedTypeException(
                    "The second argument to fn:resolve-QName must be an element node", getMetadata());
        }

        NamespaceBindingUtils.NamespaceResolver resolver = prefix -> resolvePrefix(element, prefix);
        Name resolved = NamespaceBindingUtils.parseLexicalQNameForResolveQName(
                qnameItem.getStringValue(), resolver, getMetadata());
        return ItemFactory.getInstance().createQNameItem(resolved);
    }

    private static String resolvePrefix(Item element, String prefix) {
        for (Item nsNode : element.namespaceNodes()) {
            Name nsName = nsNode.nodeName();
            String nodePrefix = nsName == null ? "" : nsName.getLocalName();
            if (nodePrefix.equals(prefix)) {
                String uri = nsNode.getStringValue();
                return uri.isEmpty() && prefix.isEmpty() ? null : uri;
            }
        }
        return null;
    }
}
