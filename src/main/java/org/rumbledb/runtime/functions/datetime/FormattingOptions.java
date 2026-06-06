package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.util.formatting.calendar.CalendarSupport;
import org.rumbledb.runtime.functions.util.formatting.language.LanguageSupport;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

public final class FormattingOptions {

    final String calendarMode;
    final Locale locale;

    // Explicit user-supplied place, normalized. Null if omitted or ().
    final String place;

    // Non-null only if place itself is an explicit recognized IANA timezone.
    final ZoneId placeZoneId;

    private FormattingOptions(
            String calendarMode,
            Locale locale,
            String place,
            ZoneId placeZoneId
    ) {
        this.calendarMode = calendarMode;
        this.locale = locale;
        this.place = place;
        this.placeZoneId = placeZoneId;
    }

    boolean shouldAdjustToPlaceTimezone() {
        return this.placeZoneId != null;
    }

    static FormattingOptions fromArguments(
            String language,
            String calendar,
            String rawPlace,
            Map<String, String> staticallyKnownNamespaces,
            ExceptionMetadata metadata
    ) {
        String place = normalizePlace(rawPlace);
        ZoneId placeZoneId = resolveExplicitIanaZoneOrNull(place);

        return new FormattingOptions(
                CalendarSupport.resolveCalendarMode(calendar, staticallyKnownNamespaces, metadata),
                LanguageSupport.resolveLocale(language),
                place,
                placeZoneId
        );
    }

    private static String normalizePlace(String place) {
        if (place == null) {
            return null;
        }

        String trimmed = place.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static ZoneId resolveExplicitIanaZoneOrNull(String place) {
        if (place == null) {
            return null;
        }

        try {
            return ZoneId.of(place);
        } catch (DateTimeException e) {
            return null;
        }
    }
}
