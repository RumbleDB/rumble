package org.rumbledb.runtime.functions.datetime;

import java.util.Locale;

final class FormattingOptions {
    final Locale locale;
    final CalendarMode calendarMode;
    final boolean useFiveArgumentSemantics;

    private FormattingOptions(Locale locale, CalendarMode calendarMode, boolean useFiveArgumentSemantics) {
        this.locale = locale;
        this.calendarMode = calendarMode;
        this.useFiveArgumentSemantics = useFiveArgumentSemantics;
    }

    static FormattingOptions legacy() {
        return new FormattingOptions(Locale.getDefault(), CalendarMode.DEFAULT, false);
    }

    static FormattingOptions extended(Locale locale, CalendarMode calendarMode) {
        return new FormattingOptions(locale, calendarMode, true);
    }
}
