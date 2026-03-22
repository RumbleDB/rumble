package org.rumbledb.runtime.functions.datetime;

import java.util.Locale;

import org.rumbledb.runtime.functions.base.formatting.language.LanguageSupport;

final class FormattingOptions {
    final String language;
    final Locale locale;
    final String calendarMode;
    final boolean useFiveArgumentSemantics;

    private FormattingOptions(
            String language,
            Locale locale,
            String calendarMode,
            boolean useFiveArgumentSemantics
    ) {
        this.language = language;
        this.locale = locale;
        this.calendarMode = calendarMode;
        this.useFiveArgumentSemantics = useFiveArgumentSemantics;
    }

    static FormattingOptions legacy() {
        String language = null;
        Locale locale = LanguageSupport.resolveLocale(language);
        return new FormattingOptions(language, locale, CalendarMode.DEFAULT, false);
    }

    static FormattingOptions extended(String language, String calendarMode) {
        Locale locale = LanguageSupport.resolveLocale(language);
        return new FormattingOptions(language, locale, calendarMode, true);
    }
}
