package org.rumbledb.runtime.functions.datetime;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.runtime.RuntimeIterator;

public class FormatTimeFunctionIterator extends AbstractFormatFunctionIterator {

    private static final long serialVersionUID = 1L;

    public FormatTimeFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    protected OffsetDateTime getTemporalValue(Item valueItem) {
        try {
            OffsetTime timeValue = valueItem.getTimeValue();
            return timeValue.atDate(LocalDate.of(1972, 12, 31));
        } catch (UnsupportedOperationException e) {
            String message = String.format("\"%s\": not castable to type %s", valueItem.serialize(), "time");
            throw new CastException(message, getMetadata());
        }
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
