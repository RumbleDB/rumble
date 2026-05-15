package org.rumbledb.runtime.functions.util.formatting.calendar.formatter;

import org.rumbledb.config.FormattingCalendarModeSupport;

import java.time.OffsetDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;

// Java returns 0 for days at the start of a month that belong to the
// last ISO week of the previous month. XQuery's ISO [w] rendering expects
// the week number within the month owning the ISO week, so we use the
// previous month's last day in that case.
public final class IsoCalendarFormatter extends AbstractGregorianCalendarFormatter {

    @Override
    public String getCalendar() {
        return FormattingCalendarModeSupport.ISO;
    }

    @Override
    public int dayOfWeek(OffsetDateTime value) {
        return value.getDayOfWeek().getValue();
    }

    @Override
    public int weekInYear(OffsetDateTime value) {
        return value.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    @Override
    public int weekInMonth(OffsetDateTime value) {
        int week = value.get(WeekFields.ISO.weekOfMonth());

        if (week == 0) {
            OffsetDateTime previousMonth = value.minusMonths(1);

            return previousMonth
                .withDayOfMonth(previousMonth.toLocalDate().lengthOfMonth())
                .get(WeekFields.ISO.weekOfMonth());
        }

        return week;
    }
}
