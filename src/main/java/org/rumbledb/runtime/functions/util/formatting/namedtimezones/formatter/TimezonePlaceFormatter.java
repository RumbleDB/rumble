package org.rumbledb.runtime.functions.util.formatting.namedtimezones.formatter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Locale;

public interface TimezonePlaceFormatter {
    String getPlace();

    boolean supportsPlace(String place);

    String timezoneName(
            OffsetDateTime value,
            String presentation,
            Locale locale
    );

    ZoneId resolveZoneIdOrNull(String place);
}
