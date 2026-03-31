package org.rumbledb.runtime.functions.base.formatting.language;

import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.Month;

public class EnglishFormatter implements LanguageFormatter {

    @Override
    public String getLanguage() {
        return "en";
    }

    @Override
    public boolean supportsOrdinalSuffix() {
        return true;
    }

    @Override
    public String ordinalSuffix(BigInteger value) {
        BigInteger mod100 = value.mod(BigInteger.valueOf(100));

        if (
            mod100.equals(BigInteger.valueOf(11))
                ||
                mod100.equals(BigInteger.valueOf(12))
                ||
                mod100.equals(BigInteger.valueOf(13))
        ) {
            return "th";
        }

        BigInteger mod10 = value.mod(BigInteger.TEN);
        if (mod10.equals(BigInteger.ONE))
            return "st";
        if (mod10.equals(BigInteger.valueOf(2)))
            return "nd";
        if (mod10.equals(BigInteger.valueOf(3)))
            return "rd";
        return "th";
    }

    @Override
    public String toCardinal(int value) {
        return toEnglishCardinalWords(value);
    }

    @Override
    public String toOrdinal(int value) {
        return toEnglishOrdinalWords(value);
    }

    @Override
    public String dayAbbreviation(DayOfWeek day, int maxWidth) {
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

    @Override
    public String monthAbbreviation(Month month) {
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

    @Override
    public String dayName(DayOfWeek day) {
        switch (day) {
            case MONDAY:
                return "Monday";
            case TUESDAY:
                return "Tuesday";
            case WEDNESDAY:
                return "Wednesday";
            case THURSDAY:
                return "Thursday";
            case FRIDAY:
                return "Friday";
            case SATURDAY:
                return "Saturday";
            case SUNDAY:
                return "Sunday";
            default:
                return null;
        }
    }

    @Override
    public String dayName(DayOfWeek day, int minWidth, int maxWidth) {
        String full = dayName(day);

        // No explicit width => full name
        if (maxWidth < 0) {
            return full;
        }

        String abbr = dayAbbreviationInternal(day, maxWidth);
        if (abbr != null && abbr.length() >= minWidth && abbr.length() <= maxWidth) {
            return abbr;
        }

        if (full.length() <= maxWidth && full.length() >= minWidth) {
            return full;
        }

        if (full.length() > maxWidth) {
            return full.substring(0, maxWidth);
        }

        return full;
    }

    @Override
    public String monthName(Month month) {
        switch (month) {
            case JANUARY:
                return "January";
            case FEBRUARY:
                return "February";
            case MARCH:
                return "March";
            case APRIL:
                return "April";
            case MAY:
                return "May";
            case JUNE:
                return "June";
            case JULY:
                return "July";
            case AUGUST:
                return "August";
            case SEPTEMBER:
                return "September";
            case OCTOBER:
                return "October";
            case NOVEMBER:
                return "November";
            case DECEMBER:
                return "December";
            default:
                return null;
        }
    }

    @Override
    public String monthName(Month month, int minWidth, int maxWidth) {
        String full = monthName(month);

        // No explicit width => full name
        if (maxWidth < 0) {
            return full;
        }

        String abbr = monthAbbreviation(month);
        if (abbr != null && abbr.length() >= minWidth && abbr.length() <= maxWidth) {
            return abbr;
        }

        if (full.length() <= maxWidth && full.length() >= minWidth) {
            return full;
        }

        if (full.length() > maxWidth) {
            return full.substring(0, maxWidth);
        }

        return full;
    }

    private String dayAbbreviationInternal(DayOfWeek day, int maxWidth) {
        switch (day) {
            case MONDAY:
                return "Mon";
            case TUESDAY:
                return maxWidth >= 4 ? "Tues" : "Tue";
            case WEDNESDAY:
                return maxWidth >= 4 ? "Weds" : "Wed";
            case THURSDAY:
                if (maxWidth >= 5)
                    return "Thurs";
                if (maxWidth >= 4)
                    return "Thur";
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

    public static String toEnglishCardinalWords(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Negative values are not supported here.");
        }
        if (value == 0) {
            return "zero";
        }
        return cardinalUnderOneTrillion(value);
    }

    private static String cardinalUnderOneTrillion(int value) {
        if (value >= 1_000_000_000) {
            int billions = value / 1_000_000_000;
            int rest = value % 1_000_000_000;
            if (rest == 0) {
                return cardinalUnderOneBillion(billions) + " billion";
            }
            return cardinalUnderOneBillion(billions) + " billion " + cardinalUnderOneBillion(rest);
        }
        return cardinalUnderOneBillion(value);
    }

    private static String cardinalUnderOneBillion(int value) {
        if (value >= 1_000_000) {
            int millions = value / 1_000_000;
            int rest = value % 1_000_000;
            if (rest == 0) {
                return cardinalUnderOneMillion(millions) + " million";
            }
            return cardinalUnderOneMillion(millions) + " million " + cardinalUnderOneMillion(rest);
        }
        return cardinalUnderOneMillion(value);
    }

    private static String cardinalUnderOneMillion(int value) {
        if (value >= 1_000) {
            int thousands = value / 1_000;
            int rest = value % 1_000;
            if (rest == 0) {
                return cardinalUnderOneThousand(thousands) + " thousand";
            }
            return cardinalUnderOneThousand(thousands) + " thousand " + cardinalUnderOneThousand(rest);
        }
        return cardinalUnderOneThousand(value);
    }

    private static String cardinalUnderOneThousand(int value) {
        if (value >= 100) {
            int hundreds = value / 100;
            int rest = value % 100;
            if (rest == 0) {
                return unitWord(hundreds) + " hundred";
            }
            return unitWord(hundreds) + " hundred " + cardinalUnderOneHundred(rest);
        }
        return cardinalUnderOneHundred(value);
    }

    private static String cardinalUnderOneHundred(int value) {
        if (value < 20) {
            return unitOrTeenWord(value);
        }
        int tens = value / 10;
        int ones = value % 10;
        String tensWord = tensWord(tens);
        if (ones == 0) {
            return tensWord;
        }
        return tensWord + "-" + unitWord(ones);
    }

    public static String toEnglishOrdinalWords(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Negative values are not supported here.");
        }
        if (value == 0) {
            return "zeroth";
        }
        return ordinalUnderOneTrillion(value);
    }

    private static String ordinalUnderOneTrillion(int value) {
        if (value >= 1_000_000_000) {
            int billions = value / 1_000_000_000;
            int rest = value % 1_000_000_000;
            if (rest == 0) {
                return cardinalUnderOneBillion(billions) + " billionth";
            }
            return cardinalUnderOneBillion(billions) + " billion " + ordinalUnderOneBillion(rest);
        }
        return ordinalUnderOneBillion(value);
    }

    private static String ordinalUnderOneBillion(int value) {
        if (value >= 1_000_000) {
            int millions = value / 1_000_000;
            int rest = value % 1_000_000;
            if (rest == 0) {
                return cardinalUnderOneMillion(millions) + " millionth";
            }
            return cardinalUnderOneMillion(millions) + " million " + ordinalUnderOneMillion(rest);
        }
        return ordinalUnderOneMillion(value);
    }

    private static String ordinalUnderOneMillion(int value) {
        if (value >= 1_000) {
            int thousands = value / 1_000;
            int rest = value % 1_000;
            if (rest == 0) {
                return cardinalUnderOneThousand(thousands) + " thousandth";
            }
            return cardinalUnderOneThousand(thousands) + " thousand " + ordinalUnderOneThousand(rest);
        }
        return ordinalUnderOneThousand(value);
    }

    private static String ordinalUnderOneThousand(int value) {
        if (value >= 100) {
            int hundreds = value / 100;
            int rest = value % 100;
            if (rest == 0) {
                return unitWord(hundreds) + " hundredth";
            }
            return unitWord(hundreds) + " hundred " + ordinalUnderOneHundred(rest);
        }
        return ordinalUnderOneHundred(value);
    }

    private static String ordinalUnderOneHundred(int value) {
        switch (value) {
            case 1:
                return "first";
            case 2:
                return "second";
            case 3:
                return "third";
            case 4:
                return "fourth";
            case 5:
                return "fifth";
            case 6:
                return "sixth";
            case 7:
                return "seventh";
            case 8:
                return "eighth";
            case 9:
                return "ninth";
            case 10:
                return "tenth";
            case 11:
                return "eleventh";
            case 12:
                return "twelfth";
            case 13:
                return "thirteenth";
            case 14:
                return "fourteenth";
            case 15:
                return "fifteenth";
            case 16:
                return "sixteenth";
            case 17:
                return "seventeenth";
            case 18:
                return "eighteenth";
            case 19:
                return "nineteenth";
            case 20:
                return "twentieth";
            case 30:
                return "thirtieth";
            case 40:
                return "fortieth";
            case 50:
                return "fiftieth";
            case 60:
                return "sixtieth";
            case 70:
                return "seventieth";
            case 80:
                return "eightieth";
            case 90:
                return "ninetieth";
            default:
                int tens = value / 10;
                int ones = value % 10;
                return tensWord(tens) + "-" + ordinalUnderOneHundred(ones);
        }
    }

    private static String unitWord(int value) {
        switch (value) {
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            case 8:
                return "eight";
            case 9:
                return "nine";
            default:
                throw new IllegalArgumentException("Unsupported unit: " + value);
        }
    }

    private static String unitOrTeenWord(int value) {
        switch (value) {
            case 0:
                return "zero";
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            case 8:
                return "eight";
            case 9:
                return "nine";
            case 10:
                return "ten";
            case 11:
                return "eleven";
            case 12:
                return "twelve";
            case 13:
                return "thirteen";
            case 14:
                return "fourteen";
            case 15:
                return "fifteen";
            case 16:
                return "sixteen";
            case 17:
                return "seventeen";
            case 18:
                return "eighteen";
            case 19:
                return "nineteen";
            default:
                throw new IllegalArgumentException("Unsupported <20 value: " + value);
        }
    }

    private static String tensWord(int tens) {
        switch (tens) {
            case 2:
                return "twenty";
            case 3:
                return "thirty";
            case 4:
                return "forty";
            case 5:
                return "fifty";
            case 6:
                return "sixty";
            case 7:
                return "seventy";
            case 8:
                return "eighty";
            case 9:
                return "ninety";
            default:
                throw new IllegalArgumentException("Unsupported tens: " + tens);
        }
    }
}
