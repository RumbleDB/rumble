package org.rumbledb.runtime.functions.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.QNameItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.MappingLocalCursor;

import java.io.Serial;
import java.util.List;

public class NamespaceURIFromQNameFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public NamespaceURIFromQNameFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new MappingLocalCursor<>(
                this.getChild(0),
                context,
                NamespaceURIFromQNameFunctionIterator::evaluate,
                getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(this.getChild(0).materializeFirstItemOrNull(context));
    }

    private static Item evaluate(Item item) {
        QNameItem qnameItem = (QNameItem) item;
        if (qnameItem == null) {
            return null;
        }
        Name qname = qnameItem.getQNameValue();
        String namespace = qname.getNamespace();
        return ItemFactory.getInstance().createAnyURIItem(namespace == null ? "" : namespace);
    }
}
