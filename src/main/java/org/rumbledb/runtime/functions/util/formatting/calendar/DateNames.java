package org.rumbledb.runtime.functions.util.formatting.calendar;

import com.ibm.icu.text.DateFormatSymbols;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import org.rumbledb.runtime.functions.util.formatting.FormattingContext;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class DateNames {

    private static final Map<SymbolsKey, DateFormatSymbols> SYMBOLS_CACHE = new ConcurrentHashMap<>();

    private DateNames() {
    }

    public static String monthName(OffsetDateTime value, FormattingContext context, int min, int max) {
        Calendar cal = CalendarFields.calendar(value, context);
        DateFormatSymbols symbols = symbolsFor(cal, context.uLocale);
        int month = CalendarFields.usesJavaTimeFields(context)
            ? value.getMonthValue() - 1
            : cal.get(Calendar.MONTH);

        return monthName(symbols, month, min, max);
    }

    public static String dayName(OffsetDateTime value, FormattingContext context, int min, int max) {
        Calendar cal = CalendarFields.calendar(value, context);
        DateFormatSymbols symbols = symbolsFor(cal, context.uLocale);
        int day = CalendarFields.usesJavaTimeFields(context)
            ? javaDayOfWeekToIcu(value)
            : cal.get(Calendar.DAY_OF_WEEK);

        return dayName(symbols, day, min, max);
    }

    public static String amPmName(Calendar cal, FormattingContext context) {
        DateFormatSymbols symbols = symbolsFor(cal, context.uLocale);
        String[] values = symbols.getAmPmStrings();
        return values[cal.get(Calendar.AM_PM)];
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

    private static final class SymbolsKey {
        private final String calendarType;
        private final ULocale locale;

        private SymbolsKey(String calendarType, ULocale locale) {
            this.calendarType = calendarType;
            this.locale = locale;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SymbolsKey)) {
                return false;
            }
            SymbolsKey other = (SymbolsKey) o;
            return this.calendarType.equals(other.calendarType) && this.locale.equals(other.locale);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.calendarType, this.locale);
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
        if (max < 0) {
            return wide;
        }
        for (String candidate : List.of(abbr, narrow, wide)) {
            if (candidate != null && candidate.length() >= min && candidate.length() <= max) {
                return candidate;
            }
        }
        return wide.length() > max ? wide.substring(0, max) : wide;
    }
}
