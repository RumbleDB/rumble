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
import java.util.concurrent.ConcurrentHashMap;

import lombok.EqualsAndHashCode;

public final class FormattingContext {

    private static final Map<LocaleKey, LocaleTriple> LOCALE_CACHE = new ConcurrentHashMap<>();

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

        LocaleTriple triple = LOCALE_CACHE.computeIfAbsent(
            new LocaleKey(effectiveLanguage, icuCalendarType),
            key -> {
                Locale resolvedLocale = LanguageSupport.resolveLocale(key.effectiveLanguage);
                ULocale base = ULocale.forLanguageTag(
                    key.effectiveLanguage == null
                        ? FormattingLanguageSupport.DEFAULT_FORMATTING_LANGUAGE
                        : key.effectiveLanguage
                );
                ULocale resolvedULocale = base.setKeywordValue("calendar", key.icuCalendarType);
                return new LocaleTriple(resolvedLocale, resolvedULocale, resolvedULocale.toLocale());
            }
        );
        this.locale = triple.locale;
        this.uLocale = triple.uLocale;
        this.javaLocale = triple.javaLocale;
    }

    @EqualsAndHashCode
    private static final class LocaleKey {
        private final String effectiveLanguage;
        private final String icuCalendarType;

        private LocaleKey(String effectiveLanguage, String icuCalendarType) {
            this.effectiveLanguage = effectiveLanguage;
            this.icuCalendarType = icuCalendarType;
        }

    }

    private static final class LocaleTriple {
        private final Locale locale;
        private final ULocale uLocale;
        private final Locale javaLocale;

        private LocaleTriple(Locale locale, ULocale uLocale, Locale javaLocale) {
            this.locale = locale;
            this.uLocale = uLocale;
            this.javaLocale = javaLocale;
        }
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
