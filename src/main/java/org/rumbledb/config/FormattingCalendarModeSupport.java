package org.rumbledb.config;

import java.util.ArrayList;
import java.util.List;

public final class FormattingCalendarModeSupport {

    private FormattingCalendarModeSupport() {
    }

    public static final String ISO = "ISO";
    public static final String AD = "AD";
    public static final String DEFAULT = ISO;

    public static final List<String> supportedCalendarModes = new ArrayList<>();

    public static boolean isValidFormattingCalendar(String calendar) {
        return calendar != null && supportedCalendarModes.contains(calendar);
    }

    static {
        supportedCalendarModes.add(AD);
        supportedCalendarModes.add(ISO);
    }
}
