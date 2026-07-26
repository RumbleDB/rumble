package org.rumbledb.runtime.functions.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.QNameItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.MappingLocalCursor;

import java.io.Serial;
import java.util.List;

public class PrefixFromQNameFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public PrefixFromQNameFunctionIterator(
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
                PrefixFromQNameFunctionIterator::evaluate,
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
        String prefix = qnameItem.getQNameValue().getPrefix();
        if (prefix == null)
            return null;

        return ItemFactory.getInstance().createNCNameItem(prefix);
    }
}
