package org.rumbledb.runtime.functions.util.formatting.calendar;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import org.rumbledb.runtime.functions.util.formatting.FormattingContext;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;

public final class CalendarFields {

    private CalendarFields() {
    }

    public static Calendar calendar(OffsetDateTime value, FormattingContext context) {
        String tzid = context.placeZoneId != null
            ? context.placeZoneId.getId()
            : "GMT" + value.getOffset().getId().replace("Z", "+00:00");

        Calendar cal = Calendar.getInstance(
            TimeZone.getTimeZone(tzid),
            context.uLocale
        );
        cal.setTimeInMillis(value.toInstant().toEpochMilli());

        if ("ISO".equalsIgnoreCase(context.calendarDesignator)) {
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            cal.setMinimalDaysInFirstWeek(4);
        }

        return cal;
    }

    // ISO requires Gregorian fields and ISO week rules; keep these
    // fields on java.time until the ICU calendar path is configured accordingly.
    public static boolean usesJavaTimeFields(FormattingContext context) {
        return context == null || CalendarModes.usesJavaTimeFields(context.calendarDesignator);
    }

    public static int year(OffsetDateTime value, FormattingContext context) {
        if (usesJavaTimeFields(context)) {
            return value.getYear();
        }
        return year(calendar(value, context));
    }

    public static int monthInYear(OffsetDateTime value, FormattingContext context) {
        if (usesJavaTimeFields(context)) {
            return value.getMonthValue();
        }
        return monthInYear(calendar(value, context));
    }

    public static int dayInMonth(OffsetDateTime value, FormattingContext context) {
        if (usesJavaTimeFields(context)) {
            return value.getDayOfMonth();
        }
        return dayInMonth(calendar(value, context));
    }

    public static int dayInYear(OffsetDateTime value, FormattingContext context) {
        if (usesJavaTimeFields(context)) {
            return value.getDayOfYear();
        }
        return dayInYear(calendar(value, context));
    }

    public static int weekInYear(OffsetDateTime value, FormattingContext context) {
        if (usesJavaTimeFields(context)) {
            if ("ISO".equalsIgnoreCase(context.calendarDesignator)) {
                return value.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            }
            return value.get(WeekFields.SUNDAY_START.weekOfYear());
        }
        return weekInYear(calendar(value, context));
    }

    public static int weekInMonth(OffsetDateTime value, FormattingContext context) {
        if (usesJavaTimeFields(context)) {
            WeekFields weekFields = "ISO".equalsIgnoreCase(context.calendarDesignator)
                ? WeekFields.ISO
                : WeekFields.SUNDAY_START;
            return weekInMonth(value.toLocalDate(), weekFields);
        }
        return weekInMonth(calendar(value, context));
    }

    public static int isoDayOfWeek(OffsetDateTime value, FormattingContext context) {
        if (usesJavaTimeFields(context)) {
            return value.getDayOfWeek().getValue();
        }
        return isoDayOfWeek(calendar(value, context));
    }

    private static int year(Calendar cal) {
        return cal.get(Calendar.YEAR);
    }

    private static int monthInYear(Calendar cal) {
        return cal.get(Calendar.MONTH) + 1;
    }

    private static int dayInMonth(Calendar cal) {
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    private static int dayInYear(Calendar cal) {
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    private static int weekInYear(Calendar cal) {
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    private static int weekInMonth(Calendar cal) {
        int week = cal.get(Calendar.WEEK_OF_MONTH);
        if (week != 0) {
            return week;
        }

        Calendar previousMonth = cal.clone();
        previousMonth.add(Calendar.MONTH, -1);
        previousMonth.set(Calendar.DAY_OF_MONTH, previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
        return previousMonth.get(Calendar.WEEK_OF_MONTH);
    }

    private static int weekInMonth(LocalDate date, WeekFields weekFields) {
        int week = date.get(weekFields.weekOfMonth());
        if (week != 0) {
            return week;
        }

        LocalDate previousMonth = date.minusMonths(1);
        LocalDate previousMonthEnd = previousMonth.withDayOfMonth(previousMonth.lengthOfMonth());
        return previousMonthEnd.get(weekFields.weekOfMonth());
    }

    private static int isoDayOfWeek(Calendar cal) {
        int day = cal.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.SUNDAY ? 7 : day - 1;
    }
}
