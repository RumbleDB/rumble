package org.rumbledb.runtime.functions.datetime.dateformatting;

import java.time.OffsetDateTime;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.RuntimeIterator;

public class FormatDateFunctionIterator extends DateFormattingFunctionIterator {

    private static final long serialVersionUID = 1L;

    public FormatDateFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    protected OffsetDateTime extractTemporalValue(Item valueItem) {
        return valueItem.getDateTimeValue();
    }

    @Override
    protected String temporalTypeName() {
        return "date";
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
