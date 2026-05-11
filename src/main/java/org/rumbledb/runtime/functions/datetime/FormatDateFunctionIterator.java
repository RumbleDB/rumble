package org.rumbledb.runtime.functions.datetime;

import java.time.OffsetDateTime;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.runtime.RuntimeIterator;

public class FormatDateFunctionIterator extends AbstractFormatFunctionIterator {

    private static final long serialVersionUID = 1L;

    public FormatDateFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    protected OffsetDateTime getTemporalValue(Item valueItem) {
        try {
            return valueItem.getDateTimeValue();
        } catch (UnsupportedOperationException e) {
            String message = String.format("\"%s\": not castable to type %s", valueItem.serialize(), "date");
            throw new CastException(message, getMetadata());
        }
    }

    @Override
    protected boolean supportsComponent(char component) {
        return component == 'Y'
            || component == 'M'
            || component == 'D'
            || component == 'd'
            || component == 'F'
            || component == 'W'
            || component == 'w'
            || component == 'Z'
            || component == 'z'
            || component == 'X';
    }
}
