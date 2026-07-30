package org.rumbledb.runtime.functions.nullable;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class IsNullIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;

    public IsNullIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        List<Item> materializedItems = this.children.get(0).materialize(context);
        if (materializedItems == null || materializedItems.isEmpty()) {
            return ItemFactory.getInstance().createBooleanItem(true);
        }
        if (materializedItems.size() > 1) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        return ItemFactory.getInstance().createBooleanItem(materializedItems.get(0).isNull());
    }
}
