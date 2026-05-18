package org.rumbledb.runtime.functions.util.formatting.namedtimezones.formatter;

import org.rumbledb.runtime.functions.util.formatting.namedtimezones.TimezonePlaceRegistry;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Locale;

public final class UnitedStatesTimezoneFormatter implements TimezonePlaceFormatter {

    @Override
    public String getPlace() {
        return "us";
    }

    @Override
    public boolean supportsPlace(String place) {
        return "us".equals(TimezonePlaceRegistry.normalizePlace(place));
    }

    @Override
    public ZoneId resolveZoneIdOrNull(String place) {
        // Country code "us" does not identify a single timezone.
        return null;
    }

    @Override
    public String timezoneName(
            OffsetDateTime value,
            String presentation,
            Locale locale
    ) {
        int totalMinutes = value.getOffset().getTotalSeconds() / 60;

        switch (totalMinutes) {
            case -10 * 60:
                return "HST";
            case -5 * 60:
                return "ET";
            case 0:
                return "GMT";
            case 5 * 60 + 30:
                return "IST";
            default:
                return null;
        }
    }
}
