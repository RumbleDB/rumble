package org.rumbledb.runtime.functions.datetime.components;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.math.BigDecimal;
import java.util.List;

public class SecondsFromTimeFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item timeItem = null;

    public SecondsFromTimeFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.timeItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (this.timeItem == null) {
            return null;
        }
        return ItemFactory.getInstance()
            .createDecimalItem(
                BigDecimal.valueOf(
                    this.timeItem.getDateTimeValue().getSecondOfMinute()
                        + this.timeItem.getDateTimeValue().getMillisOfSecond() * 1.0 / 1000
                )
            );
    }

}
