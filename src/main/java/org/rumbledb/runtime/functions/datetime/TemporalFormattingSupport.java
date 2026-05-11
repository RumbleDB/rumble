package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.runtime.functions.base.formatting.NumericPictureParser;

import java.time.ZoneOffset;
import java.util.Locale;

final class TemporalFormattingSupport {

    private TemporalFormattingSupport() {
    }

    static String applyNameCase(String value, ParsedVariableMarker parsed, Locale locale) {
        if (parsed.nameForm == null) {
            return value;
        }
        switch (parsed.nameForm) {
            case ParsedVariableMarker.NameForm.UPPER:
                return value.toUpperCase(locale);
            case ParsedVariableMarker.NameForm.LOWER:
                return value.toLowerCase(locale);
            case ParsedVariableMarker.NameForm.TITLE:
            default:
                return value;
        }
    }

    static String applyWordCase(String value, String wordCase, Locale locale) {
        if (wordCase == null) {
            return value;
        }

        switch (wordCase) {
            case ParsedVariableMarker.WordCase.UPPER:
                return value.toUpperCase(locale);
            case ParsedVariableMarker.WordCase.LOWER:
                return value.toLowerCase(locale);
            case ParsedVariableMarker.WordCase.TITLE:
                return toTitleCaseWords(value, locale);
            default:
                return value;
        }
    }

    static String toTitleCaseWords(String value, Locale locale) {
        StringBuilder sb = new StringBuilder(value.length());
        boolean start = true;
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (Character.isLetter(ch) && start) {
                sb.append(String.valueOf(ch).toUpperCase(locale));
                start = false;
            } else {
                sb.append(ch);
                if (ch == ' ' || ch == '-') {
                    start = true;
                }
            }
        }
        return sb.toString();
    }

    static String formatTimezone(ZoneOffset offset, ParsedTimezonePicture tz, boolean hasExplicitTimezone) {
        if (tz.military) {
            return formatMilitaryTimezone(offset, hasExplicitTimezone);
        }

        int totalMinutes = offset.getTotalSeconds() / 60;
        if (tz.useZForZero && totalMinutes == 0 && !tz.gmtPrefix) {
            return "Z";
        }

        char sign = totalMinutes >= 0 ? '+' : '-';
        int absMinutes = Math.abs(totalMinutes);
        int hours = absMinutes / 60;
        int minutes = absMinutes % 60;

        String hour = Integer.toString(hours);
        if (hour.length() < tz.hourWidth) {
            hour = "0".repeat(tz.hourWidth - hour.length()) + hour;
        }

        String minute = Integer.toString(minutes);
        if (minute.length() < tz.minuteWidth) {
            minute = "0".repeat(tz.minuteWidth - minute.length()) + minute;
        }

        String body;
        if (tz.compactNoSeparator) {
            if (tz.alwaysShowMinutes) {
                body = "" + sign + hour + minute;
            } else if (tz.minutesOptionalIfZero && minutes == 0) {
                body = "" + sign + hour;
            } else {
                body = "" + sign + hour + minute;
            }
        } else {
            if (!tz.alwaysShowMinutes && tz.minutesOptionalIfZero && minutes == 0) {
                body = "" + sign + hour;
            } else {
                body = "" + sign + hour + tz.separator + minute;
            }
        }

        body = NumericPictureParser.mapAsciiDigits(body, tz.zeroDigit);
        return tz.gmtPrefix ? "GMT" + body : body;
    }

    static String formatMilitaryTimezone(ZoneOffset offset, boolean hasExplicitTimezone) {
        if (!hasExplicitTimezone) {
            return "J";
        }

        int totalSeconds = offset.getTotalSeconds();
        if (totalSeconds % 3600 != 0) {
            int absMinutes = Math.abs(totalSeconds / 60);
            int h = absMinutes / 60;
            int m = absMinutes % 60;
            char sign = totalSeconds >= 0 ? '+' : '-';
            return String.format("%c%02d:%02d", sign, h, m);
        }

        int h = totalSeconds / 3600;
        if (h == 0) {
            return "Z";
        }
        if (h >= 1 && h <= 9) {
            return String.valueOf((char) ('A' + h - 1));
        }
        if (h >= 10 && h <= 12) {
            return String.valueOf((char) ('K' + h - 10));
        }
        if (h <= -1 && h >= -12) {
            return String.valueOf((char) ('N' + (-h) - 1));
        }
        return offset.getId();
    }
}
