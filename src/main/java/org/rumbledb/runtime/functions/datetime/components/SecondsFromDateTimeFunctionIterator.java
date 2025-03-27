package org.rumbledb.runtime.functions.datetime.components;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SecondsFromDateTimeFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item dateTimeItem = null;

    public SecondsFromDateTimeFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.dateTimeItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        if (this.dateTimeItem == null) {
            return null;
        }
        LocalDateTime dateTime = LocalDateTime.now();
        int second = dateTime.getSecond();
        int nanoOfSecond = dateTime.getNano();

        return ItemFactory.getInstance().createDecimalItem(BigDecimal.valueOf(second + nanoOfSecond / 1_000_000.0));
    }

}
