package org.rumbledb.runtime.functions.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

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
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ComputedLocalCursor.fromArguments(
            this.getChildren(),
            context,
            NamespaceUriForPrefixFunctionIterator::evaluate,
            getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(
            ComputedLocalCursor.arguments(
                this.getChildren().size(),
                index -> this.getChild(index).materializeFirstItemOrNull(context)
            )
        );
    }

    private static Item evaluate(ComputedLocalCursor.Arguments<Item> arguments) {
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
