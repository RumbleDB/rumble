package org.rumbledb.runtime.functions.datetime;

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
        String place = getOptionalString(placeItem);

        String calendarMode = resolveCalendarMode(calendar, pictureStringForErrors, metadata);

        // place aktuell noch ignoriert, aber bewusst schon eingelesen
        return FormattingOptions.extended(language, calendarMode);
    }

    private static String getOptionalString(Item item) {
        return item != null && !item.isNull()
            ? item.getStringValue()
            : null;
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
