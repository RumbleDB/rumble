package org.rumbledb.runtime.functions.util.formatting.calendar;

import org.rumbledb.config.FormattingCalendarModeSupport;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.functions.util.formatting.calendar.formatter.AdCalendarFormatter;
import org.rumbledb.runtime.functions.util.formatting.calendar.formatter.CalendarFormatter;
import org.rumbledb.runtime.functions.util.formatting.calendar.formatter.IsoCalendarFormatter;

import java.util.HashMap;
import java.util.Map;

public final class CalendarRegistry {

    private static final Map<String, CalendarFormatter> FORMATTERS = new HashMap<>();

    static {
        register(new IsoCalendarFormatter());
        register(new AdCalendarFormatter());
    }

    private CalendarRegistry() {
    }

    public static void register(CalendarFormatter formatter) {
        String calendar = formatter.getCalendar();

        if (!FormattingCalendarModeSupport.supportedCalendarModes.contains(calendar)) {
            throw new OurBadException(
                    "Registered formatter is not supported by the RumbleRuntimeConfiguration: " + calendar
            );
        }

        FORMATTERS.put(calendar, formatter);
    }

    public static CalendarFormatter forCalendar(String resolvedCalendarMode) {
        return FORMATTERS.getOrDefault(
            resolvedCalendarMode,
            FORMATTERS.get(FormattingCalendarModeSupport.DEFAULT)
        );
    }
}
