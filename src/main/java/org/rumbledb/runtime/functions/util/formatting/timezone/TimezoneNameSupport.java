package org.rumbledb.runtime.functions.util.formatting.timezone;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class TimezoneNameSupport {

    private TimezoneNameSupport() {
    }

    public static String formatNamedTimezone(
            OffsetDateTime value,
            TimezoneNameContext context,
            String presentation
    ) {
        Locale locale = context == null ? Locale.ROOT : context.getLocale();
        ZoneId zoneId = context == null ? null : context.getPlaceZoneId();

        String name = null;

        if (zoneId != null) {
            name = formatUsingZoneId(value, zoneId, locale, presentation);
        }

        if (name == null || name.isEmpty()) {
            return null;
        }

        return applyPresentation(name, presentation, locale);
    }

    private static String formatUsingZoneId(
            OffsetDateTime value,
            ZoneId zoneId,
            Locale locale,
            String presentation
    ) {
        try {
            String pattern = usesLongTimezoneName(presentation) ? "zzzz" : "z";

            return DateTimeFormatter
                .ofPattern(pattern, locale)
                .format(value.toInstant().atZone(zoneId));
        } catch (RuntimeException e) {
            return null;
        }
    }

    private static boolean usesLongTimezoneName(String presentation) {
        /*
         * Keep this conservative for now.
         * [ZN], [Zn], [ZNn] can use short names.
         * If we later distinguish long/short timezone name modifiers explicitly,
         * this is the place to do it.
         */
        return false;
    }

    private static String applyPresentation(String value, String presentation, Locale locale) {
        if ("n".equals(presentation)) {
            return value.toLowerCase(locale);
        }

        if ("Nn".equals(presentation)) {
            return toTitleCase(value, locale);
        }

        return value;
    }

    private static String toTitleCase(String value, Locale locale) {
        if (value.isEmpty()) {
            return value;
        }

        StringBuilder result = new StringBuilder(value.length());
        boolean startOfWord = true;

        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);

            if (Character.isLetter(ch)) {
                if (startOfWord) {
                    result.append(String.valueOf(ch).toUpperCase(locale));
                    startOfWord = false;
                } else {
                    result.append(String.valueOf(ch).toLowerCase(locale));
                }
            } else {
                result.append(ch);
                startOfWord = Character.isWhitespace(ch) || ch == '-' || ch == '_';
            }
        }

        return result.toString();
    }
}
