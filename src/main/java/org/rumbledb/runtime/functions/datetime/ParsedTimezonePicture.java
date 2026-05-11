package org.rumbledb.runtime.functions.datetime;

final class ParsedTimezonePicture {
    final boolean gmtPrefix;
    final boolean alwaysShowMinutes;
    final String separator;
    final int hourWidth;
    final int minuteWidth;
    final int zeroDigit;
    final boolean useZForZero;
    final boolean minutesOptionalIfZero;
    final boolean compactNoSeparator;
    final boolean military;


    final boolean named;
    final String namePresentation;

    private ParsedTimezonePicture(
            boolean gmtPrefix,
            boolean alwaysShowMinutes,
            String separator,
            int hourWidth,
            int minuteWidth,
            int zeroDigit,
            boolean useZForZero,
            boolean minutesOptionalIfZero,
            boolean compactNoSeparator,
            boolean military,
            boolean named,
            String namePresentation
    ) {
        this.gmtPrefix = gmtPrefix;
        this.alwaysShowMinutes = alwaysShowMinutes;
        this.separator = separator;
        this.hourWidth = hourWidth;
        this.minuteWidth = minuteWidth;
        this.zeroDigit = zeroDigit;
        this.useZForZero = useZForZero;
        this.minutesOptionalIfZero = minutesOptionalIfZero;
        this.compactNoSeparator = compactNoSeparator;
        this.military = military;
        this.named = named;
        this.namePresentation = namePresentation;
    }

    static ParsedTimezonePicture military() {
        return new ParsedTimezonePicture(false, false, ":", 2, 2, '0', false, false, false, true, false, null);
    }

    static ParsedTimezonePicture defaultNumeric() {
        return new ParsedTimezonePicture(false, true, ":", 2, 2, '0', false, false, false, false, false, null);
    }

    static ParsedTimezonePicture defaultGmt() {
        return new ParsedTimezonePicture(true, true, ":", 2, 2, '0', false, false, false, false, false, null);
    }

    static ParsedTimezonePicture named(String namePresentation) {
        return new ParsedTimezonePicture(
                false,
                true,
                ":",
                2,
                2,
                '0',
                false,
                false,
                false,
                false,
                true,
                namePresentation
        );
    }

    static ParsedTimezonePicture custom(
            boolean gmtPrefix,
            boolean alwaysShowMinutes,
            String sep,
            int hourWidth,
            int minuteWidth,
            int zeroDigit,
            boolean useZForZero,
            boolean minutesOptionalIfZero,
            boolean compactNoSeparator
    ) {
        return new ParsedTimezonePicture(
                gmtPrefix,
                alwaysShowMinutes,
                sep,
                hourWidth,
                minuteWidth,
                zeroDigit,
                useZForZero,
                minutesOptionalIfZero,
                compactNoSeparator,
                false,
                false,
                null
        );
    }
}
