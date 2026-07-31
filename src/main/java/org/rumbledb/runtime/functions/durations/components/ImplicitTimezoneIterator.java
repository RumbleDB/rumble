package org.rumbledb.runtime.functions.durations.components;

import java.io.Serial;
import java.time.Duration;
import java.time.OffsetDateTime;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.util.List;

public class ImplicitTimezoneIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public ImplicitTimezoneIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        OffsetDateTime dt = OffsetDateTime.now();
        return ItemFactory.getInstance()
            .createDayTimeDurationItem(Duration.ofMillis(dt.getOffset().getTotalSeconds() * 1000L));
    }

}
