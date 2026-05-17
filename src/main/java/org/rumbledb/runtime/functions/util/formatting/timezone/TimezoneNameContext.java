package org.rumbledb.runtime.functions.util.formatting.timezone;

import java.time.ZoneId;
import java.util.Locale;

public final class TimezoneNameContext {

    private final String place;
    private final ZoneId placeZoneId;
    private final Locale locale;

    public TimezoneNameContext(String place, ZoneId placeZoneId, Locale locale) {
        this.place = place;
        this.placeZoneId = placeZoneId;
        this.locale = locale == null ? Locale.ROOT : locale;
    }

    public String getPlace() {
        return this.place;
    }

    public ZoneId getPlaceZoneId() {
        return this.placeZoneId;
    }

    public Locale getLocale() {
        return this.locale;
    }
}
