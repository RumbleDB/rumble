package org.rumbledb.runtime.functions.util.formatting.calendar;

import org.rumbledb.config.FormattingCalendarModeSupport;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CalendarSupport {

    public static final String DEFAULT_CALENDAR = FormattingCalendarModeSupport.DEFAULT;

    private static final Pattern EQNAME_PATTERN = Pattern.compile("^Q\\{([^}]*)\\}(.+)$");

    private CalendarSupport() {
    }

    public static String normalizeKnownCalendarMode(String calendar) {
        if (calendar == null || calendar.trim().isEmpty()) {
            return DEFAULT_CALENDAR;
        }

        String normalized = calendar.trim();

        if ("Q{}ISO".equals(normalized) || "ISO".equalsIgnoreCase(normalized)) {
            return FormattingCalendarModeSupport.ISO;
        }

        if ("Q{}AD".equals(normalized) || "AD".equalsIgnoreCase(normalized)) {
            return FormattingCalendarModeSupport.AD;
        }

        if ("DEFAULT".equalsIgnoreCase(normalized)) {
            return FormattingCalendarModeSupport.DEFAULT;
        }

        return normalized;
    }

    public static String resolveCalendarMode(String calendar, ExceptionMetadata metadata) {
        String normalized = normalizeKnownCalendarMode(calendar);

        if (FormattingCalendarModeSupport.calendarModes.contains(normalized)) {
            return normalized;
        }

        Matcher matcher = EQNAME_PATTERN.matcher(normalized);
        if (matcher.matches()) {
            String namespace = matcher.group(1);
            String localName = matcher.group(2);

            if (!isValidCalendarLocalName(localName)) {
                throw invalidCalendar(calendar, metadata);
            }

            if (!namespace.isEmpty()) {
                return DEFAULT_CALENDAR;
            }

            throw invalidCalendar(calendar, metadata);
        }

        throw invalidCalendar(calendar, metadata);
    }

    private static boolean isValidCalendarLocalName(String localName) {
        return localName != null
            && !localName.isEmpty()
            && localName.matches("[A-Za-z_][A-Za-z0-9._-]*");
    }

    private static IncorrectSyntaxFormatDateTimeException invalidCalendar(
            String calendar,
            ExceptionMetadata metadata
    ) {
        return new IncorrectSyntaxFormatDateTimeException(
                "\"" + calendar + "\": invalid calendar",
                metadata
        );
    }
}
