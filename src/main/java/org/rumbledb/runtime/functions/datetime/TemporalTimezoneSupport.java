package org.rumbledb.runtime.functions.datetime;

import java.time.OffsetDateTime;

final class TemporalTimezoneSupport {

    private TemporalTimezoneSupport() {
    }

    static OffsetDateTime valueForRendering(
            OffsetDateTime value,
            boolean hasExplicitTimezone,
            FormattingOptions options
    ) {
        if (!hasExplicitTimezone) {
            return value;
        }

        if (options == null || options.placeZoneId == null) {
            return value;
        }

        return value.toInstant()
            .atZone(options.placeZoneId)
            .toOffsetDateTime();
    }
}
