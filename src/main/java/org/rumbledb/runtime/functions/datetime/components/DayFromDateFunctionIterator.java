package org.rumbledb.runtime.functions.datetime.components;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.TemporalComponentFunctionIterator;

import java.io.Serial;
import java.util.List;

public class DayFromDateFunctionIterator extends TemporalComponentFunctionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public DayFromDateFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext, Component.DAY);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item dateItem = this.getChild(0).materializeFirstItemOrNull(context);
        if (dateItem == null) {
            return null;
        }
        return ItemFactory.getInstance().createIntItem(dateItem.getDay());
    }
}
