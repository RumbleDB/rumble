package org.rumbledb.runtime.functions.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class PrefixFromQNameFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;

    public PrefixFromQNameFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item qnameItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (qnameItem == null) {
            return null;
        }
        String prefix = qnameItem.getQNameValue().getPrefix();
        if (prefix == null || prefix.isEmpty()) {
            return null;
        }

        return ItemFactory.getInstance().createNCNameItem(prefix);
    }
}
