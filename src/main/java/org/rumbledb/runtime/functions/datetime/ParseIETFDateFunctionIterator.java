package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseIETFDateFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Map<String, Integer> MONTHS = createMonthMap();
    private static final Map<String, Integer> NAMED_ZONE_OFFSETS = createNamedZoneOffsets();
    private static final String S = "[\\t\\n\\r ]+";
    private static final String OPT_S = "[\\t\\n\\r ]*";
    private static final String DAYNAME =
        "(?:Mon|Tue|Wed|Thu|Fri|Sat|Sun|Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)";
    private static final String MONTHNAME = "(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)";
    private static final String DAYNUM = "(\\d{1,2})";
    private static final String YEAR = "(\\d{2}|\\d{4})";
    private static final String TZNAME = "(UT|UTC|GMT|EST|EDT|CST|CDT|MST|MDT|PST|PDT)";
    private static final String TZOFFSET = "([+-])(\\d{1,2})(?::?(\\d{2}))?";
    private static final String TIMEZONE = "(?:"
        + TZNAME
        + "|"
        + TZOFFSET
        + "(?:"
        + OPT_S
        + "\\("
        + OPT_S
        + TZNAME
        + OPT_S
        + "\\))?"
        + ")";
    private static final String TIME = "(\\d{1,2}):(\\d{2})(?::(\\d{2})(?:\\.(\\d+))?)?(?:"
        + OPT_S
        + "("
        + TIMEZONE
        + "))?";
    private static final Pattern DATE_SPEC_PATTERN = Pattern.compile(
        "^"
            + OPT_S
            + "(?:"
            + "("
            + DAYNAME
            + ")"
            + ",?"
            + S
            + ")?"
            + DAYNUM
            + "(?:"
            + S
            + "|"
            + OPT_S
            + "-"
            + OPT_S
            + ")"
            + "("
            + MONTHNAME
            + ")"
            + "(?:"
            + S
            + "|"
            + OPT_S
            + "-"
            + OPT_S
            + ")"
            + YEAR
            + S
            + TIME
            + OPT_S
            + "$",
        Pattern.CASE_INSENSITIVE
    );
    private static final Pattern ASCTIME_PATTERN = Pattern.compile(
        "^"
            + OPT_S
            + "(?:"
            + "("
            + DAYNAME
            + ")"
            + ",?"
            + S
            + ")?"
            + "("
            + MONTHNAME
            + ")"
            + "(?:"
            + S
            + "|"
            + OPT_S
            + "-"
            + OPT_S
            + ")"
            + DAYNUM
            + S
            + TIME
            + S
            + YEAR
            + OPT_S
            + "$",
        Pattern.CASE_INSENSITIVE
    );

    public ParseIETFDateFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item inputItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (inputItem == null) {
            return null;
        }
        String lexicalValue = inputItem.getStringValue();
        try {
            ParsedDateTime parsedValue = parseIetfDate(lexicalValue);
            return ItemFactory.getInstance().createDateTimeItem(parsedValue.value, parsedValue.hasTimeZone);
        } catch (IllegalArgumentException | DateTimeException e) {
            throw new RumbleException(
                    "Invalid date/time value passed to fn:parse-ietf-date(): " + lexicalValue,
                    ErrorCode.ParseIetfDateErrorCode,
                    getMetadata()
            );
        }
    }

    private static ParsedDateTime parseIetfDate(String input) {
        Matcher dateSpecMatcher = DATE_SPEC_PATTERN.matcher(input);
        if (dateSpecMatcher.matches()) {
            return buildParsedDateTime(
                dateSpecMatcher.group(2),
                dateSpecMatcher.group(3),
                dateSpecMatcher.group(4),
                dateSpecMatcher.group(5),
                dateSpecMatcher.group(6),
                dateSpecMatcher.group(7),
                dateSpecMatcher.group(8),
                dateSpecMatcher.group(9),
                dateSpecMatcher.group(10),
                dateSpecMatcher.group(11),
                dateSpecMatcher.group(12),
                dateSpecMatcher.group(13)
            );
        }

        Matcher asctimeMatcher = ASCTIME_PATTERN.matcher(input);
        if (asctimeMatcher.matches()) {
            return buildParsedDateTime(
                asctimeMatcher.group(3),
                asctimeMatcher.group(2),
                asctimeMatcher.group(14),
                asctimeMatcher.group(4),
                asctimeMatcher.group(5),
                asctimeMatcher.group(6),
                asctimeMatcher.group(7),
                asctimeMatcher.group(8),
                asctimeMatcher.group(9),
                asctimeMatcher.group(10),
                asctimeMatcher.group(11),
                asctimeMatcher.group(12)
            );
        }

        throw new IllegalArgumentException("Input does not match the fn:parse-ietf-date grammar");
    }

    private static ParsedDateTime buildParsedDateTime(
            String dayString,
            String monthString,
            String yearString,
            String hourString,
            String minuteString,
            String secondString,
            String fractionalSecondString,
            String timezoneString,
            String timezoneName,
            String timezoneSign,
            String timezoneHourString,
            String timezoneMinuteString
    ) {
        int day = parseDay(dayString);
        int month = parseMonth(monthString);
        int year = parseYear(yearString);
        LocalDate date = LocalDate.of(year, month, day);
        LocalTime time = parseTime(hourString, minuteString, secondString, fractionalSecondString);
        ZoneOffset offset = parseZoneOffset(
            timezoneString,
            timezoneName,
            timezoneSign,
            timezoneHourString,
            timezoneMinuteString
        );
        return new ParsedDateTime(OffsetDateTime.of(date, time, offset), true);
    }

    private static int parseDay(String dayString) {
        return Integer.parseInt(dayString);
    }

    private static int parseMonth(String monthString) {
        Integer month = MONTHS.get(monthString.toLowerCase(Locale.ENGLISH));
        if (month == null) {
            throw new IllegalArgumentException("Invalid month");
        }
        return month;
    }

    private static int parseYear(String yearString) {
        int year = Integer.parseInt(yearString);
        if (yearString.length() == 2) {
            return 1900 + year;
        }
        return year;
    }

    private static LocalTime parseTime(
            String hourString,
            String minuteString,
            String secondString,
            String fractionalSecondString
    ) {
        int hour = Integer.parseInt(hourString);
        int minute = Integer.parseInt(minuteString);
        int second = secondString == null ? 0 : Integer.parseInt(secondString);
        int nanoseconds = 0;
        if (fractionalSecondString != null) {
            if (fractionalSecondString.length() > 9) {
                nanoseconds = Integer.parseInt(fractionalSecondString.substring(0, 9));
            } else {
                nanoseconds = Integer.parseInt(String.format("%-9s", fractionalSecondString).replace(' ', '0'));
            }
        }
        return LocalTime.of(hour, minute, second, nanoseconds);
    }

    private static ZoneOffset parseZoneOffset(
            String timezoneString,
            String timezoneName,
            String timezoneSign,
            String timezoneHourString,
            String timezoneMinuteString
    ) {
        if (timezoneString == null) {
            return ZoneOffset.UTC;
        }
        if (timezoneName != null) {
            Integer namedOffsetHours = NAMED_ZONE_OFFSETS.get(timezoneName.toUpperCase(Locale.ENGLISH));
            if (namedOffsetHours == null) {
                throw new IllegalArgumentException("Invalid zone");
            }
            return ZoneOffset.ofHours(namedOffsetHours);
        }
        int sign = "-".equals(timezoneSign) ? -1 : 1;
        int hours = Integer.parseInt(timezoneHourString);
        int minutes = timezoneMinuteString == null ? 0 : Integer.parseInt(timezoneMinuteString);
        return ZoneOffset.ofHoursMinutes(sign * hours, sign * minutes);
    }

    private static Map<String, Integer> createMonthMap() {
        Map<String, Integer> months = new HashMap<>();
        months.put("jan", 1);
        months.put("feb", 2);
        months.put("mar", 3);
        months.put("apr", 4);
        months.put("may", 5);
        months.put("jun", 6);
        months.put("jul", 7);
        months.put("aug", 8);
        months.put("sep", 9);
        months.put("oct", 10);
        months.put("nov", 11);
        months.put("dec", 12);
        return months;
    }

    private static Map<String, Integer> createNamedZoneOffsets() {
        Map<String, Integer> offsets = new HashMap<>();
        offsets.put("UT", 0);
        offsets.put("UTC", 0);
        offsets.put("GMT", 0);
        offsets.put("EST", -5);
        offsets.put("EDT", -4);
        offsets.put("CST", -6);
        offsets.put("CDT", -5);
        offsets.put("MST", -7);
        offsets.put("MDT", -6);
        offsets.put("PST", -8);
        offsets.put("PDT", -7);
        return offsets;
    }

    private static class ParsedDateTime {
        private final OffsetDateTime value;
        private final boolean hasTimeZone;

        private ParsedDateTime(OffsetDateTime value, boolean hasTimeZone) {
            this.value = value;
            this.hasTimeZone = hasTimeZone;
        }
    }
}
