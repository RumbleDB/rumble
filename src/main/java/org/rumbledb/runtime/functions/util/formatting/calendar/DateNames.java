package org.rumbledb.runtime.functions.util.formatting.calendar;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.EqualsAndHashCode;

import com.ibm.icu.text.DateFormatSymbols;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

import org.rumbledb.runtime.functions.util.formatting.FormattingContext;

public final class DateNames {

    private static final Map<SymbolsKey, DateFormatSymbols> SYMBOLS_CACHE = new ConcurrentHashMap<>();

    private DateNames() {}

    public static String monthName(OffsetDateTime value, FormattingContext context, int min, int max) {
        Calendar cal = CalendarFields.calendar(value, context);
        DateFormatSymbols symbols = symbolsFor(cal, context.uLocale);
        int month = CalendarFields.usesJavaTimeFields(context) ? value.getMonthValue() - 1 : cal.get(Calendar.MONTH);

        return monthName(symbols, month, min, max);
    }

    public static String dayName(OffsetDateTime value, FormattingContext context, int min, int max) {
        Calendar cal = CalendarFields.calendar(value, context);
        DateFormatSymbols symbols = symbolsFor(cal, context.uLocale);
        int day =
                CalendarFields.usesJavaTimeFields(context) ? javaDayOfWeekToIcu(value) : cal.get(Calendar.DAY_OF_WEEK);

        return dayName(symbols, day, min, max);
    }

    public static String amPmName(Calendar cal, FormattingContext context, int min, int max) {
        DateFormatSymbols symbols = symbolsFor(cal, context.uLocale);
        String[] values = symbols.getAmPmStrings();
        return fitName(values[cal.get(Calendar.AM_PM)], min, max);
    }

    private static DateFormatSymbols symbolsFor(Calendar cal, ULocale locale) {
        SymbolsKey key = new SymbolsKey(cal.getType(), locale);
        DateFormatSymbols symbols = SYMBOLS_CACHE.get(key);
        if (symbols == null) {
            symbols = new DateFormatSymbols(cal, locale);
            SYMBOLS_CACHE.putIfAbsent(key, symbols);
        }
        return symbols;
    }

    @EqualsAndHashCode
    private static final class SymbolsKey {
        private final String calendarType;
        private final ULocale locale;

        private SymbolsKey(String calendarType, ULocale locale) {
            this.calendarType = calendarType;
            this.locale = locale;
        }
    }

    private static String monthName(DateFormatSymbols symbols, int month, int min, int max) {
        String wide = symbols.getMonths(DateFormatSymbols.FORMAT, DateFormatSymbols.WIDE)[month];
        String abbr = symbols.getMonths(DateFormatSymbols.FORMAT, DateFormatSymbols.ABBREVIATED)[month];
        String narrow = symbols.getMonths(DateFormatSymbols.FORMAT, DateFormatSymbols.NARROW)[month];

        return fitName(wide, abbr, narrow, min, max);
    }

    private static String dayName(DateFormatSymbols symbols, int day, int min, int max) {
        String wide = symbols.getWeekdays(DateFormatSymbols.FORMAT, DateFormatSymbols.WIDE)[day];
        String abbr = symbols.getWeekdays(DateFormatSymbols.FORMAT, DateFormatSymbols.ABBREVIATED)[day];
        String narrow = symbols.getWeekdays(DateFormatSymbols.FORMAT, DateFormatSymbols.NARROW)[day];

        return fitName(wide, abbr, narrow, min, max);
    }

    private static int javaDayOfWeekToIcu(OffsetDateTime value) {
        int day = value.getDayOfWeek().getValue();
        return day == 7 ? Calendar.SUNDAY : day + 1;
    }

    private static String fitName(String wide, String abbr, String narrow, int min, int max) {
        String name = wide;

        if (max >= 0 && name.length() > max) {
            name = conventionalAbbreviation(min, max, abbr, narrow);
            if (name == null) {
                name = truncateToCodePoints(wide, max);
            }
        }

        return padToMinimumWidth(name, min);
    }

    private static String fitName(String full, int min, int max) {
        return fitName(full, null, null, min, max);
    }

    // The minimum matters, not just the maximum: German abbreviations carry a period ("Jan.", "Mo."), so for
    // [MN,3-3] no conventional form fits and the full name is truncated instead, giving "Jan" rather than "J".
    private static String conventionalAbbreviation(int min, int max, String... candidates) {
        for (String candidate : candidates) {
            if (candidate != null && !candidate.isEmpty() && candidate.length() <= max && candidate.length() >= min) {
                return candidate;
            }
        }
        return null;
    }

    private static String truncateToCodePoints(String name, int max) {
        if (name.length() <= max) {
            return name;
        }
        return name.substring(0, name.offsetByCodePoints(0, Math.min(max, name.codePointCount(0, name.length()))));
    }

    private static String padToMinimumWidth(String name, int min) {
        return name.length() >= min ? name : name + " ".repeat(min - name.length());
    }
}
