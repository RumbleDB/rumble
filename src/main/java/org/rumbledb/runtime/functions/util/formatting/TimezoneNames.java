package org.rumbledb.runtime.functions.util.formatting;

import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.util.TimeZone;

import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public final class TimezoneNames {

    private TimezoneNames() {
    }

    public static String name(
            OffsetDateTime value,
            FormattingContext context,
            boolean longName
    ) {
        String zoneId = resolveZoneId(value, context);
        if (zoneId == null) {
            return null;
        }

        String javaName = javaDisplayName(value, context, zoneId, longName);
        if (javaName != null) {
            return javaName;
        }

        long millis = value.toInstant().toEpochMilli();
        TimeZone zone = TimeZone.getTimeZone(zoneId);
        boolean daylight = zone.inDaylightTime(new Date(millis));
        TimeZoneNames.NameType type = nameType(daylight, longName);

        return TimeZoneNames
            .getInstance(context.uLocale)
            .getDisplayName(zoneId, type, millis);
    }

    private static String javaDisplayName(
            OffsetDateTime value,
            FormattingContext context,
            String zoneId,
            boolean longName
    ) {
        try {
            ZoneId zone = ZoneId.of(zoneId);
            String pattern = longName ? "zzzz" : "z";
            String name = DateTimeFormatter.ofPattern(pattern, context.javaLocale)
                .format(value.toInstant().atZone(zone));

            return name == null || name.isEmpty() ? null : name;
        } catch (DateTimeException | IllegalArgumentException e) {
            return null;
        }
    }

    private static TimeZoneNames.NameType nameType(boolean daylight, boolean longName) {
        if (longName) {
            return daylight ? TimeZoneNames.NameType.LONG_DAYLIGHT : TimeZoneNames.NameType.LONG_STANDARD;
        }
        return daylight ? TimeZoneNames.NameType.SHORT_DAYLIGHT : TimeZoneNames.NameType.SHORT_STANDARD;
    }

    private static String resolveZoneId(OffsetDateTime value, FormattingContext context) {
        if (context == null) {
            return null;
        }

        if (context.placeZoneId != null) {
            return context.placeZoneId.getId();
        }

        String country = normalizeCountry(context.place);
        if (country == null) {
            return null;
        }

        String[] zoneIds = TimeZone.getAvailableIDs(country);
        if (zoneIds == null || zoneIds.length == 0) {
            return null;
        }

        long millis = value.toInstant().toEpochMilli();
        int expectedOffsetMillis = value.getOffset().getTotalSeconds() * 1000;
        for (String zoneId : zoneIds) {
            TimeZone zone = TimeZone.getTimeZone(zoneId);
            if (zone.getOffset(millis) == expectedOffsetMillis) {
                return zoneId;
            }
        }

        return null;
    }

    private static String normalizeCountry(String place) {
        if (place == null) {
            return null;
        }

        String value = place.trim();
        if (value.length() != 2) {
            return null;
        }

        return value.toUpperCase(Locale.ROOT);
    }
}
