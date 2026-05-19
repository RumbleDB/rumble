package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.config.FormattingCalendarModeSupport;
import org.rumbledb.config.FormattingLanguageSupport;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.util.formatting.calendar.CalendarSupport;
import org.rumbledb.runtime.functions.util.formatting.language.LanguageSupport;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

public final class FormattingOptions {

    final String calendarMode;
    final Locale locale;
    final boolean useFiveArgumentSemantics;

    final String place;
    final ZoneId placeZoneId;

    private FormattingOptions(
            String calendarMode,
            Locale locale,
            boolean useFiveArgumentSemantics,
            String place,
            ZoneId placeZoneId
    ) {
        this.calendarMode = calendarMode;
        this.locale = locale;
        this.useFiveArgumentSemantics = useFiveArgumentSemantics;
        this.place = place;
        this.placeZoneId = placeZoneId;
    }

    private static FormattingOptions twoArgumentDefaults() {
        return new FormattingOptions(
                FormattingCalendarModeSupport.DEFAULT,
                Locale.forLanguageTag(FormattingLanguageSupport.DEFAULT_FORMATTING_LANGUAGE),
                false,
                null,
                null
        );
    }

    private static FormattingOptions extended(
            String language,
            String calendarMode,
            String place,
            ZoneId placeZoneId
    ) {
        Locale locale = LanguageSupport.resolveLocale(language);

        return new FormattingOptions(
                calendarMode,
                locale,
                true,
                place,
                placeZoneId
        );
    }

    static FormattingOptions fromArguments(
            int arity,
            String language,
            String calendar,
            String place,
            ZoneId placeZoneId,
            Map<String, String> staticallyKnownNamespaces,
            ExceptionMetadata metadata
    ) {
        if (arity <= 2) {
            return twoArgumentDefaults();
        }

        return extended(
            language,
            CalendarSupport.resolveCalendarMode(calendar, staticallyKnownNamespaces, metadata),
            place,
            placeZoneId
        );
    }
}
