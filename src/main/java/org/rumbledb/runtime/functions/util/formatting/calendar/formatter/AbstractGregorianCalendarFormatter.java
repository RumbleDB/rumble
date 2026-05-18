package org.rumbledb.runtime.functions.util.formatting.calendar.formatter;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.OffsetDateTime;

abstract class AbstractGregorianCalendarFormatter implements CalendarFormatter {

    @Override
    public int year(OffsetDateTime value) {
        return value.getYear();
    }

    @Override
    public int monthInYear(OffsetDateTime value) {
        return value.getMonthValue();
    }

    @Override
    public int dayInMonth(OffsetDateTime value) {
        return value.getDayOfMonth();
    }

    @Override
    public int dayInYear(OffsetDateTime value) {
        return value.getDayOfYear();
    }

    @Override
    public Month monthForName(OffsetDateTime value) {
        return value.getMonth();
    }

    @Override
    public DayOfWeek dayForName(OffsetDateTime value) {
        return value.getDayOfWeek();
    }
}
