/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.
 */

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
            boolean military
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
    }

    static ParsedTimezonePicture military() {
        return new ParsedTimezonePicture(false, false, ":", 2, 2, '0', false, false, false, true);
    }

    static ParsedTimezonePicture defaultNumeric() {
        return new ParsedTimezonePicture(false, true, ":", 2, 2, '0', false, false, false, false);
    }

    static ParsedTimezonePicture defaultGmt() {
        return new ParsedTimezonePicture(true, true, ":", 2, 2, '0', false, false, false, false);
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
                false
        );
    }
}
