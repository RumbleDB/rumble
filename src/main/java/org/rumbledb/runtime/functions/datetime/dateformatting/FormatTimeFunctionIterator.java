package org.rumbledb.runtime.functions.datetime.dateformatting;

import java.io.Serial;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;

public class FormatTimeFunctionIterator extends DateFormattingFunctionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public FormatTimeFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    protected OffsetDateTime extractTemporalValue(Item valueItem) {
        OffsetTime timeValue = valueItem.getTimeValue();
        return timeValue.atDate(LocalDate.of(1972, 1, 1));
    }

    @Override
    protected String temporalTypeName() {
        return "time";
    }

    @Override
    protected boolean supportsComponent(char component) {
        return component == 'H'
            || component == 'h'
            || component == 'P'
            || component == 'm'
            || component == 's'
            || component == 'f'
            || component == 'Z'
            || component == 'z';
    }
}
