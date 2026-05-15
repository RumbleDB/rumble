package org.rumbledb.runtime.functions.util.formatting.timezone;

import java.time.ZoneId;
import java.util.Locale;

public final class TimezoneNameContext {

    private final ZoneId placeZoneId;
    private final Locale locale;

    public TimezoneNameContext(ZoneId placeZoneId, Locale locale) {
        this.placeZoneId = placeZoneId;
        this.locale = locale == null ? Locale.ROOT : locale;
    }

    public ZoneId getPlaceZoneId() {
        return this.placeZoneId;
    }

    public Locale getLocale() {
        return this.locale;
    }
}
