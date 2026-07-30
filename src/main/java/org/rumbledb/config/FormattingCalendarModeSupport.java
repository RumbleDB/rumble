package org.rumbledb.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FormattingCalendarModeSupport {


    public static final String DEFAULT = "ISO";

    // validates only the ASCII NCName subset needed by the W3C calendar designators.
    private static final Pattern NCNAME_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9._-]*");

    public static boolean isValidFormattingCalendar(String calendar) {
        return calendar != null && NCNAME_PATTERN.matcher(calendar).matches();
    }
}
