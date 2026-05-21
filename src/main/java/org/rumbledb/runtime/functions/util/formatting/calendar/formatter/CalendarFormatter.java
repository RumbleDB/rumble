package org.rumbledb.runtime.functions.util.formatting.calendar.formatter;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.OffsetDateTime;

public interface CalendarFormatter {

    String getCalendar();

    int year(OffsetDateTime value);

    int monthInYear(OffsetDateTime value);

    int dayInMonth(OffsetDateTime value);

    int dayInYear(OffsetDateTime value);

    int dayOfWeek(OffsetDateTime value);

    int weekInYear(OffsetDateTime value);

    int weekInMonth(OffsetDateTime value);

    Month monthForName(OffsetDateTime value);

    DayOfWeek dayForName(OffsetDateTime value);
}
