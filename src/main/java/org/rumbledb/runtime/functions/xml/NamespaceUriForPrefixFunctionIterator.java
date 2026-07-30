package org.rumbledb.runtime.functions.xml;

import org.rumbledb.runtime.plan.EvaluationArguments;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.util.List;

public class NamespaceUriForPrefixFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public NamespaceUriForPrefixFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return evaluate(
            EvaluationArguments.lazy(
                this.getChildren().size(),
                index -> this.getChild(index).materializeFirstItemOrNull(context)
            )
        );
    }

    private static Item evaluate(EvaluationArguments<Item> arguments) {
        Item prefixItem = arguments.get(0);
        String prefix = prefixItem == null ? "" : prefixItem.getStringValue();
        Item element = arguments.get(1);
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
