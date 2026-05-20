package org.rumbledb.runtime.functions.util.formatting.calendar.formatter;

import org.rumbledb.config.FormattingCalendarModeSupport;

import java.time.OffsetDateTime;
import java.time.temporal.WeekFields;

public final class AdCalendarFormatter extends AbstractGregorianCalendarFormatter {

    @Override
    public String getCalendar() {
        return FormattingCalendarModeSupport.AD;
    }

    @Override
    public int dayOfWeek(OffsetDateTime value) {
        return value.getDayOfWeek().getValue();
    }

    @Override
    public int weekInYear(OffsetDateTime value) {
        return value.get(WeekFields.SUNDAY_START.weekOfYear());
    }

    @Override
    public int weekInMonth(OffsetDateTime value) {
        return value.get(WeekFields.SUNDAY_START.weekOfMonth());
    }
}
