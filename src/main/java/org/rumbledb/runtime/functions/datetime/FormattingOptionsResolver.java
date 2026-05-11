package org.rumbledb.runtime.functions.datetime;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;

final class FormattingOptionsResolver {

    private static final Pattern EQNAME_PATTERN = Pattern.compile("^Q\\{([^}]*)\\}(.+)$");

    private FormattingOptionsResolver() {
    }

    static FormattingOptions resolve(
            int arity,
            Item languageItem,
            Item calendarItem,
            Item placeItem,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        if (arity <= 2) {
            return FormattingOptions.legacy();
        }

        String language = getOptionalString(languageItem);
        String calendar = getOptionalString(calendarItem);

        String rawPlace = getOptionalString(placeItem);
        String place = normalizePlace(rawPlace);
        ZoneId placeZoneId = resolvePlaceZoneId(place);

        String calendarMode = resolveCalendarMode(calendar, pictureStringForErrors, metadata);

        return FormattingOptions.extended(language, calendarMode, place, placeZoneId);
    }

    private static String getOptionalString(Item item) {
        return item != null && !item.isNull()
            ? item.getStringValue()
            : null;
    }

    private static String normalizePlace(String place) {
        if (place == null) {
            return null;
        }

        String trimmed = place.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static ZoneId resolvePlaceZoneId(String place) {
        if (place == null) {
            return null;
        }

        try {
            return ZoneId.of(place);
        } catch (DateTimeException e) {
            // Conservative behavior:
            // Unknown places such as "us" are still useful as name hints,
            // but they are not valid Java ZoneIds and should not fail formatting.
            return null;
        }
    }

    static String resolveCalendarMode(
            String calendar,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        if (calendar == null || calendar.trim().isEmpty()) {
            return CalendarMode.DEFAULT;
        }

        String normalized = calendar.trim();

        if ("Q{}ISO".equals(normalized) || "ISO".equalsIgnoreCase(normalized)) {
            return CalendarMode.ISO;
        }
        if ("Q{}AD".equals(normalized) || "AD".equalsIgnoreCase(normalized)) {
            return CalendarMode.DEFAULT;
        }

        Matcher matcher = EQNAME_PATTERN.matcher(normalized);
        if (matcher.matches()) {
            String namespace = matcher.group(1);
            String localName = matcher.group(2);

            if (!isValidCalendarLocalName(localName)) {
                throw invalidCalendar(pictureStringForErrors, metadata);
            }

            if (!namespace.isEmpty()) {
                return CalendarMode.DEFAULT;
            }

            throw invalidCalendar(pictureStringForErrors, metadata);
        }

        throw invalidCalendar(pictureStringForErrors, metadata);
    }

    static boolean isValidCalendarLocalName(String localName) {
        if (localName == null || localName.isEmpty()) {
            return false;
        }
        return localName.matches("[A-Za-z_][A-Za-z0-9._-]*");
    }

    private static IncorrectSyntaxFormatDateTimeException invalidCalendar(
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        return new IncorrectSyntaxFormatDateTimeException(
                "\"" + pictureStringForErrors + "\": invalid picture string",
                metadata
        );
    }
}
