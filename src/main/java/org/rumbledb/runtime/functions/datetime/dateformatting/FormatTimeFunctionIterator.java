package org.rumbledb.runtime.functions.datetime.dateformatting;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.RuntimeIterator;

public class FormatTimeFunctionIterator extends DateFormattingFunctionIterator {

    private static final long serialVersionUID = 1L;

    public FormatTimeFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    protected OffsetDateTime extractTemporalValue(Item valueItem) {
        OffsetTime timeValue = valueItem.getTimeValue();
        return timeValue.atDate(LocalDate.of(1972, 12, 31));
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
