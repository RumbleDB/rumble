package org.rumbledb.runtime.functions.util.formatting.timezone;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public final class TimezoneNameRegistry {

    private static final List<TimezoneNameFormatter> FORMATTERS = new ArrayList<>();

    static {
        register(new BuiltinTimezoneNameFormatter());
    }

    private TimezoneNameRegistry() {
    }

    public static void register(TimezoneNameFormatter formatter) {
        FORMATTERS.add(formatter);
    }

    public static String resolve(OffsetDateTime value, TimezoneNameContext context) {
        for (TimezoneNameFormatter formatter : FORMATTERS) {
            String result = formatter.resolve(value, context);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public static String resolve(ZoneOffset offset, TimezoneNameContext context) {
        for (TimezoneNameFormatter formatter : FORMATTERS) {
            String result = formatter.resolve(offset, context);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
