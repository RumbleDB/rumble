package org.rumbledb.runtime.functions.util.formatting;

import com.ibm.icu.util.ULocale;
import org.rumbledb.config.FormattingLanguageSupport;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.util.formatting.calendar.CalendarModes;
import org.rumbledb.runtime.functions.util.formatting.calendar.CalendarSupport;
import org.rumbledb.runtime.functions.util.formatting.language.LanguageSupport;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

public final class FormattingContext {
    public final ULocale uLocale; // ICU locale with calendar keyword
    public final Locale locale; // effective language, for case conversion
    public final Locale javaLocale; // derived from uLocale, for java.time
    public final String requestedLanguage;
    public final String effectiveLanguage;
    public final boolean languageFallback;
    public final String requestedCalendar;
    public final String calendarDesignator;
    public final String icuCalendarType;
    public final boolean calendarFallback;
    public final String place;
    public final ZoneId placeZoneId;

    private FormattingContext(
            String requestedLanguage,
            String effectiveLanguage,
            String requestedCalendar,
            String calendarDesignator,
            String icuCalendarType,
            boolean calendarFallback,
            String place,
            ZoneId placeZoneId
    ) {
        this.requestedLanguage = requestedLanguage;
        this.effectiveLanguage = effectiveLanguage;
        this.languageFallback = requestedLanguage != null && !requestedLanguage.equalsIgnoreCase(effectiveLanguage);
        this.requestedCalendar = requestedCalendar;
        this.calendarDesignator = calendarDesignator;
        this.icuCalendarType = icuCalendarType;
        this.calendarFallback = calendarFallback;
        this.place = place;
        this.placeZoneId = placeZoneId;
        this.locale = LanguageSupport.resolveLocale(effectiveLanguage);

        ULocale base = ULocale.forLanguageTag(
            effectiveLanguage == null ? FormattingLanguageSupport.DEFAULT_FORMATTING_LANGUAGE : effectiveLanguage
        );
        this.uLocale = base.setKeywordValue("calendar", icuCalendarType);
        this.javaLocale = this.uLocale.toLocale();
    }

    /**
     * Creates a formatting context from the raw formatting arguments.
     *
     * This normalizes the place argument, resolves explicit IANA time zones, determines the effective
     * calendar and language, applies fallbacks where necessary, and derives the ICU and Java locale data
     * used for formatting.
     */
    public static FormattingContext fromArguments(
            String language,
            String calendar,
            String rawPlace,
            Map<String, String> staticallyKnownNamespaces,
            ExceptionMetadata metadata
    ) {
        String place = normalizePlace(rawPlace);
        ZoneId placeZoneId = resolveExplicitIanaZoneOrNull(place);
        String resolvedCalendar = CalendarSupport.resolveCalendarMode(calendar, staticallyKnownNamespaces, metadata);
        String effectiveCalendar = CalendarModes.effectiveDesignatorOrDefault(resolvedCalendar);
        boolean calendarFallback = !effectiveCalendar.equalsIgnoreCase(resolvedCalendar);
        String requestedLanguage = LanguageSupport.normalizeLanguage(language);
        String effectiveLanguage = LanguageSupport.effectiveLanguageOf(requestedLanguage);

        return new FormattingContext(
                requestedLanguage,
                effectiveLanguage,
                calendar,
                effectiveCalendar,
                CalendarModes.toCalendarTypeOrDefault(effectiveCalendar),
                calendarFallback,
                place,
                placeZoneId
        );
    }

    public boolean shouldAdjustToPlaceTimezone() {
        return this.placeZoneId != null;
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
