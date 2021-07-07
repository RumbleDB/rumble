package org.rumbledb.runtime.functions.datetime.components;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class MinutesFromDateTimeFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item dateTimeItem = null;

    public MinutesFromDateTimeFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.dateTimeItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        if (this.dateTimeItem == null) {
            return null;
        }
        return ItemFactory.getInstance().createIntItem(this.dateTimeItem.getDateTimeValue().getMinuteOfHour());
    }

}
