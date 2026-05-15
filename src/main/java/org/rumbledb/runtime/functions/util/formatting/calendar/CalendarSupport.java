package org.rumbledb.runtime.functions.util.formatting.calendar;

import org.rumbledb.config.FormattingCalendarModeSupport;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CalendarSupport {

    public static final String DEFAULT_CALENDAR = FormattingCalendarModeSupport.DEFAULT;

    private static final Pattern EQNAME_PATTERN = Pattern.compile("^Q\\{([^}]*)}(.+)$");
    private static final Pattern NCNAME_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9._-]*");

    private CalendarSupport() {
    }

    /**
     * Normalizes a calendar argument if it matches a known calendar mode.
     *
     * <p>
     * EQName literals without a namespace are resolved against the known calendar
     * names. Unknown values are returned unchanged.
     * </p>
     *
     */
    public static String normalizeKnownCalendarMode(String calendar) {
        String value = normalizeCalendarArgument(calendar);

        if ("DEFAULT".equalsIgnoreCase(value)) {
            return DEFAULT_CALENDAR;
        }

        CalendarName name = parseEQNameLiteral(value);
        if (name == null || !name.hasEmptyNamespace()) {
            return value;
        }

        String knownCalendar = knownCalendarOrNull(name.localName);
        return knownCalendar != null ? knownCalendar : value;
    }

    /**
     * Resolves the given calendar mode to the calendar identifier used internally.
     * <p>
     * Unqualified calendar names must refer to a known supported calendar.
     * Namespace-qualified calendar names are considered valid if their prefix can be
     * resolved using the statically known namespaces, but since external calendar
     * systems are not supported, they fall back to the default calendar.
     */
    public static String resolveCalendarMode(
            String calendar,
            Map<String, String> staticallyKnownNamespaces,
            ExceptionMetadata metadata
    ) {
        String value = normalizeCalendarArgument(calendar);

        if ("DEFAULT".equalsIgnoreCase(value)) {
            return DEFAULT_CALENDAR;
        }

        CalendarName name = parseCalendarName(value, staticallyKnownNamespaces, metadata);

        if (!isValidCalendarLocalName(name.localName)) {
            throw invalidCalendar(calendar, metadata);
        }

        if (name.hasEmptyNamespace()) {
            String knownCalendar = knownCalendarOrNull(name.localName);
            if (knownCalendar != null) {
                return knownCalendar;
            }

            throw invalidCalendar(calendar, metadata);
        }

        return DEFAULT_CALENDAR;
    }

    private static String normalizeCalendarArgument(String calendar) {
        if (calendar == null || calendar.trim().isEmpty()) {
            return DEFAULT_CALENDAR;
        }

        return calendar.trim();
    }

    /**
     * Parses a calendar name from the given string value.
     * <p>
     * The value may be an EQName literal, an unprefixed local name, or a prefixed
     * name whose prefix is resolved using the statically known namespaces.
     * </p>
     */
    private static CalendarName parseCalendarName(
            String value,
            Map<String, String> staticallyKnownNamespaces,
            ExceptionMetadata metadata
    ) {
        CalendarName eqName = parseEQNameLiteral(value);
        if (eqName != null) {
            return eqName;
        }

        int colon = value.indexOf(':');

        if (colon < 0) {
            return new CalendarName("", value);
        }

        if (colon == 0 || colon == value.length() - 1 || value.indexOf(':', colon + 1) >= 0) {
            throw invalidCalendar(value, metadata);
        }

        String prefix = value.substring(0, colon);
        String localName = value.substring(colon + 1);

        String namespaceUri = staticallyKnownNamespaces == null
            ? null
            : staticallyKnownNamespaces.get(prefix);

        if (namespaceUri == null) {
            throw invalidCalendar(value, metadata);
        }

        return new CalendarName(namespaceUri, localName);
    }

    /**
     * Returns a new CalendarName object if {@code value} is an EQName, {@code null} otherwise.
     */
    private static CalendarName parseEQNameLiteral(String value) {
        Matcher matcher = EQNAME_PATTERN.matcher(value);
        if (!matcher.matches()) {
            return null;
        }

        return new CalendarName(matcher.group(1), matcher.group(2));
    }

    /**
     * Looks for exact names in {@link FormattingCalendarModeSupport#supportedCalendarModes} and returns true if a match
     * is found. This does not support aliasing.
     */
    private static String knownCalendarOrNull(String localName) {
        if (localName == null) {
            return null;
        }

        for (String calendar : FormattingCalendarModeSupport.supportedCalendarModes) {
            if (calendar.equalsIgnoreCase(localName)) {
                return calendar;
            }
        }

        return null;
    }

    private static boolean isValidCalendarLocalName(String localName) {
        return localName != null && NCNAME_PATTERN.matcher(localName).matches();
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

    private static final class CalendarName {
        private final String namespaceUri;
        private final String localName;

        private CalendarName(String namespaceUri, String localName) {
            this.namespaceUri = namespaceUri;
            this.localName = localName;
        }

        private boolean hasEmptyNamespace() {
            return this.namespaceUri == null || this.namespaceUri.isEmpty();
        }
    }
}
