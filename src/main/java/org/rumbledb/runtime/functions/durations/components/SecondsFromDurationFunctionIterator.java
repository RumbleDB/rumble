package org.rumbledb.runtime.functions.durations.components;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.math.BigDecimal;
import java.util.List;

public class SecondsFromDurationFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item durationItem = null;

    public SecondsFromDurationFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.durationItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        if (this.durationItem == null) {
            return null;
        }
        return ItemFactory.getInstance()
            .createDecimalItem(
                BigDecimal.valueOf(
                    this.durationItem.getDurationValue().getSeconds()
                        + this.durationItem.getDurationValue().getMillis() * 1.0 / 1000
                )
            );
    }

}
