package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.SingletonLocalCursor;

import java.io.Serial;
import java.util.List;

public class StaticBaseURIFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public StaticBaseURIFunctionIterator(
            List<RuntimeIterator> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new SingletonLocalCursor<>(materializeFirstItemOrNull(context));
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return ItemFactory.getInstance().createAnyURIItem(this.staticContext.getStaticURI().toString());
    }
}
