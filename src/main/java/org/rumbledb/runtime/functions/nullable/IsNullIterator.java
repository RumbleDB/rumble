package org.rumbledb.runtime.functions.nullable;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

import java.io.Serial;
import java.util.List;

public class IsNullIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public IsNullIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> evaluate(this.getChild(0).materialize(context)),
                getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(this.getChild(0).materialize(context));
    }

    private Item evaluate(List<Item> items) {
        if (items.isEmpty()) {
            return ItemFactory.getInstance().createBooleanItem(true);
        }
        if (items.size() > 1) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        return ItemFactory.getInstance().createBooleanItem(items.get(0).isNull());
    }
}
