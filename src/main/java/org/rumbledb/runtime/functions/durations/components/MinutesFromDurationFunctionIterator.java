package org.rumbledb.runtime.functions.durations.components;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class MinutesFromDurationFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public MinutesFromDurationFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item durationItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        if (durationItem == null) {
            return null;
        }
        Duration duration = durationItem.getDurationValue();

        LocalDateTime referenceDate = LocalDateTime.now();
        LocalDateTime futureDate = referenceDate.plus(duration);

        Duration durationDiff = Duration.between(referenceDate, futureDate);
        return ItemFactory.getInstance().createIntItem((int) durationDiff.toMinutes());
    }

}
