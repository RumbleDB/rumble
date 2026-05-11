package org.rumbledb.runtime.functions.datetime;

import java.time.ZoneId;
import java.util.Locale;


import java.time.ZoneId;
import java.util.Locale;

final class FormattingOptions {

    final String language;
    final String calendarMode;
    final Locale locale;
    final boolean useFiveArgumentSemantics;

    final String place;
    final ZoneId placeZoneId;

    private FormattingOptions(
            String language,
            String calendarMode,
            Locale locale,
            boolean useFiveArgumentSemantics,
            String place,
            ZoneId placeZoneId
    ) {
        this.language = language;
        this.calendarMode = calendarMode;
        this.locale = locale;
        this.useFiveArgumentSemantics = useFiveArgumentSemantics;
        this.place = place;
        this.placeZoneId = placeZoneId;
    }

    static FormattingOptions legacy() {
        return new FormattingOptions(
                null,
                CalendarMode.DEFAULT,
                Locale.US,
                false,
                null,
                null
        );
    }

    static FormattingOptions extended(String language, String calendarMode) {
        return extended(language, calendarMode, null, null);
    }

    static FormattingOptions extended(
            String language,
            String calendarMode,
            String place,
            ZoneId placeZoneId
    ) {
        Locale locale = resolveLocale(language);

        return new FormattingOptions(
                language,
                calendarMode,
                locale,
                true,
                place,
                placeZoneId
        );
    }

    boolean hasPlaceZoneId() {
        return this.placeZoneId != null;
    }

    private static Locale resolveLocale(String language) {
        if (language == null || language.trim().isEmpty()) {
            return Locale.US;
        }

        return Locale.forLanguageTag(language);
    }
}
