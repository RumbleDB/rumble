package org.rumbledb.runtime.functions.util.formatting.timezone;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

final class BuiltinTimezoneNameFormatter implements TimezoneNameFormatter {

    @Override
    public String resolve(OffsetDateTime value, TimezoneNameContext context) {
        if (isAmericaNewYork(context)) {
            return "ET";
        }

        return resolve(value.getOffset(), context);
    }

    @Override
    public String resolve(ZoneOffset offset, TimezoneNameContext context) {
        int totalSeconds = offset.getTotalSeconds();
        String normalizedPlace = context == null ? "" : context.normalizedPlace();

        if (totalSeconds == 0) {
            return "Z";
        }

        if ("america/new_york".equals(normalizedPlace)) {
            return "ET";
        }

        if ("us".equals(normalizedPlace)) {
            if (totalSeconds == -10 * 3600) {
                return "HST";
            }

            if (totalSeconds == -5 * 3600) {
                return "ET";
            }
        }

        if (totalSeconds == 5 * 3600 + 30 * 60) {
            return "IST";
        }

        return null;
    }

    private static boolean isAmericaNewYork(TimezoneNameContext context) {
        if (context == null) {
            return false;
        }

        if ("america/new_york".equals(context.normalizedPlace())) {
            return true;
        }

        return context.placeZoneId != null
            && "America/New_York".equals(context.placeZoneId.getId());
    }
}
