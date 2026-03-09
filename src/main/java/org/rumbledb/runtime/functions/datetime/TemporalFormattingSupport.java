package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.runtime.functions.base.formatting.NumericPictureParser;

import java.time.DayOfWeek;
import java.time.Month;
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
            case UPPER:
                return value.toUpperCase(locale);
            case LOWER:
                return value.toLowerCase(locale);
            case TITLE:
            default:
                return value;
        }
    }

    static String applyWordCase(String value, ParsedVariableMarker.WordCase wordCase, Locale locale) {
        if (wordCase == null) {
            return value;
        }
        switch (wordCase) {
            case UPPER:
                return value.toUpperCase(locale);
            case LOWER:
                return value.toLowerCase(locale);
            case TITLE:
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

    static String getMonthName(Month month, ParsedVariableMarker parsed, FormattingOptions formattingOptions) {
        String full = month.getDisplayName(java.time.format.TextStyle.FULL, formattingOptions.locale);

        if (parsed.maxWidth < 0) {
            return full;
        }

        if (isEnglish(formattingOptions.locale)) {
            String abbr = englishMonthAbbreviation(month);
            return chooseWidthBoundedName(full, abbr, parsed.minWidth, parsed.maxWidth);
        }

        return truncateName(full, parsed.maxWidth);
    }

    static String getDayName(DayOfWeek day, ParsedVariableMarker parsed, FormattingOptions formattingOptions) {
        String full = day.getDisplayName(java.time.format.TextStyle.FULL, formattingOptions.locale);

        if (parsed.maxWidth < 0) {
            return full;
        }

        if (isEnglish(formattingOptions.locale)) {
            String abbr = englishDayAbbreviation(day, parsed.maxWidth);
            return chooseWidthBoundedName(full, abbr, parsed.minWidth, parsed.maxWidth);
        }

        return truncateName(full, parsed.maxWidth);
    }

    static String chooseWidthBoundedName(String full, String preferredAbbreviation, int minWidth, int maxWidth) {
        if (preferredAbbreviation != null) {
            int len = preferredAbbreviation.length();
            if (len >= minWidth && len <= maxWidth) {
                return preferredAbbreviation;
            }
        }

        if (full.length() <= maxWidth && full.length() >= minWidth) {
            return full;
        }

        if (full.length() > maxWidth) {
            return full.substring(0, maxWidth);
        }

        return full;
    }

    static String truncateName(String value, int maxWidth) {
        if (maxWidth > 0 && value.length() > maxWidth) {
            return value.substring(0, maxWidth);
        }
        return value;
    }

    static boolean isEnglish(Locale locale) {
        return locale != null && "en".equalsIgnoreCase(locale.getLanguage());
    }

    static String englishMonthAbbreviation(Month month) {
        switch (month) {
            case JANUARY:
                return "Jan";
            case FEBRUARY:
                return "Feb";
            case MARCH:
                return "Mar";
            case APRIL:
                return "Apr";
            case MAY:
                return "May";
            case JUNE:
                return "Jun";
            case JULY:
                return "Jul";
            case AUGUST:
                return "Aug";
            case SEPTEMBER:
                return "Sep";
            case OCTOBER:
                return "Oct";
            case NOVEMBER:
                return "Nov";
            case DECEMBER:
                return "Dec";
            default:
                return null;
        }
    }

    static String englishDayAbbreviation(DayOfWeek day, int maxWidth) {
        switch (day) {
            case MONDAY:
                return "Mon";
            case TUESDAY:
                return maxWidth >= 4 ? "Tues" : "Tue";
            case WEDNESDAY:
                return maxWidth >= 4 ? "Weds" : "Wed";
            case THURSDAY:
                if (maxWidth >= 5) {
                    return "Thurs";
                }
                if (maxWidth >= 4) {
                    return "Thur";
                }
                return "Thu";
            case FRIDAY:
                return "Fri";
            case SATURDAY:
                return "Sat";
            case SUNDAY:
                return "Sun";
            default:
                return null;
        }
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
