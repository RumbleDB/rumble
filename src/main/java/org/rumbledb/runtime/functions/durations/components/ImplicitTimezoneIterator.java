package org.rumbledb.runtime.functions.durations.components;

import java.time.Duration;
import java.time.ZonedDateTime;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class ImplicitTimezoneIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public ImplicitTimezoneIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        ZonedDateTime dt = ZonedDateTime.now();
        return ItemFactory.getInstance()
            .createDayTimeDurationItem(Duration.ofMillis(dt.getOffset().getTotalSeconds() * 1000L));
    }

}
