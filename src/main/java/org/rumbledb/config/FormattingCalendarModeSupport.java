package org.rumbledb.config;

import java.util.ArrayList;
import java.util.List;

public final class FormattingCalendarModeSupport {

    private FormattingCalendarModeSupport() {
    }

    public static final String ISO = "ISO";
    public static final String AD = "AD";
    public static final String DEFAULT = ISO;

    public static final List<String> calendarModes = new ArrayList<>();

    public static boolean isValidFormattingCalendar(String calendar) {
        return calendar != null && calendarModes.contains(calendar);
    }

    static {
        calendarModes.add(AD);
        calendarModes.add(ISO);
    }
}
