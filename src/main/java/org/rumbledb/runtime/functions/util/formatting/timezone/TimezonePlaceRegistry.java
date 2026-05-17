package org.rumbledb.runtime.functions.util.formatting.timezone;

import org.rumbledb.runtime.functions.util.formatting.timezone.formatter.TimezonePlaceFormatter;
import org.rumbledb.runtime.functions.util.formatting.timezone.formatter.UnitedStatesTimezoneFormatter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

// Minimal country-code based timezone-name support.
// The F&O spec allows the $place argument to be a country code or an IANA
// timezone name. Java ZoneId can resolve IANA names, but it does not provide
// a complete country-code-to-timezone-name resolver. This registry is therefore
// intentionally small and only contains explicitly supported place formatters.
// Unknown or unsupported cases return null so that callers can use the spec
// fallback: numeric timezone offsets.
public final class TimezonePlaceRegistry {

    private static final Map<String, TimezonePlaceFormatter> FORMATTERS = new HashMap<>();

    static {
        register(new UnitedStatesTimezoneFormatter());
    }

    private TimezonePlaceRegistry() {
    }

    public static void register(TimezonePlaceFormatter formatter) {
        FORMATTERS.put(normalizePlace(formatter.getPlace()), formatter);
    }

    public static TimezonePlaceFormatter forPlace(String place) {
        if (place == null) {
            return null;
        }

        return FORMATTERS.get(normalizePlace(place));
    }

    public static String normalizePlace(String place) {
        if (place == null || place.trim().isEmpty()) {
            return null;
        }

        return place.trim().replace('_', '-').toLowerCase(Locale.ROOT);
    }
}
