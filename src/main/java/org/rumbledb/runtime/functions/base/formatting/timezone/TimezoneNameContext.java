package org.rumbledb.runtime.functions.base.formatting.timezone;

import java.time.ZoneId;
import java.util.Locale;

public final class TimezoneNameContext {

    public final String place;
    public final ZoneId placeZoneId;
    public final Locale locale;

    public TimezoneNameContext(String place, ZoneId placeZoneId, Locale locale) {
        this.place = normalizePlaceValue(place);
        this.placeZoneId = placeZoneId;
        this.locale = locale == null ? Locale.ROOT : locale;
    }

    public String normalizedPlace() {
        if (this.place == null) {
            return "";
        }
        return this.place.trim().toLowerCase(Locale.ROOT);
    }

    private static String normalizePlaceValue(String place) {
        if (place == null) {
            return null;
        }

        String trimmed = place.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
