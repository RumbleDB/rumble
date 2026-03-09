package org.rumbledb.runtime.functions.base.formatting;

import org.rumbledb.exceptions.OurBadException;

import java.math.BigInteger;
import java.util.*;

public final class IntegerFormattingSupport {

    private IntegerFormattingSupport() {
    }

    public static String applyGrouping(String digits, NumericPicture picture) {
        if (picture.getGroupingPositions().isEmpty()) {
            return digits;
        }

        String reversedDigits = new StringBuilder(digits).reverse().toString();

        if (picture.isRepeatingGrouping()) {
            int interval = picture.getRepeatingGroupingInterval();
            String separator = picture.getGroupingPositions().get(0).separator;

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < reversedDigits.length(); i++) {
                if (i > 0 && i % interval == 0) {
                    sb.append(separator);
                }
                sb.append(reversedDigits.charAt(i));
            }
            return sb.reverse().toString();
        }

        List<GroupingPos> gps = new ArrayList<>(picture.getGroupingPositions());
        gps.sort(Comparator.comparingInt(GroupingPos::getDistanceFromRight));

        StringBuilder sb = new StringBuilder(reversedDigits);

        for (int i = gps.size() - 1; i >= 0; i--) {
            GroupingPos gp = gps.get(i);
            int insertPos = gp.distanceFromRight;
            if (insertPos > 0 && insertPos < sb.length()) {
                sb.insert(insertPos, gp.separator);
            }
        }

        return sb.reverse().toString();
    }

    public static boolean isEnglish(Locale locale) {
        return locale != null && "en".equalsIgnoreCase(locale.getLanguage());
    }

    public static String ordinalSuffix(int value, Locale locale) {
        if (!isEnglish(locale)) {
            return "";
        }

        long abs = Math.abs((long) value);
        long mod100 = abs % 100;
        if (mod100 >= 11 && mod100 <= 13) {
            return "th";
        }

        switch ((int) (abs % 10)) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static String ordinalSuffix(BigInteger value, Locale locale) { // only supports english for now
        if (!isEnglish(locale)) {
            throw new OurBadException("Language is not supported", null);
        }
        BigInteger abs = value.abs();
        BigInteger mod100 = abs.mod(BigInteger.valueOf(100));

        if (
            mod100.equals(BigInteger.valueOf(11))
                || mod100.equals(BigInteger.valueOf(12))
                || mod100.equals(BigInteger.valueOf(13))
        ) {
            return "th";
        }

        BigInteger mod10 = abs.mod(BigInteger.TEN);
        if (mod10.equals(BigInteger.ONE)) {
            return "st";
        }
        if (mod10.equals(BigInteger.valueOf(2))) {
            return "nd";
        }
        if (mod10.equals(BigInteger.valueOf(3))) {
            return "rd";
        }
        return "th";
    }

    public static String ordinalSuffix(BigInteger value) { // only supports english for now
        return ordinalSuffix(value, Locale.ENGLISH);
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

        if (value >= 1_000_000_000) {
            int billions = value / 1_000_000_000;
            int rest = value % 1_000_000_000;
            if (rest == 0) {
                return cardinalUnderOneBillion(billions) + " billionth";
            }
            return cardinalUnderOneBillion(billions) + " billion " + toEnglishOrdinalWords(rest);
        }

        if (value >= 1_000_000) {
            int millions = value / 1_000_000;
            int rest = value % 1_000_000;
            if (rest == 0) {
                return cardinalUnderOneMillion(millions) + " millionth";
            }
            return cardinalUnderOneMillion(millions) + " million " + toEnglishOrdinalWords(rest);
        }

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

    public static String integerToRoman(int number) {
        if (number <= 0) {
            return "0";
        }
        final int[] values = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
        final String[] romanLiterals = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                number -= values[i];
                s.append(romanLiterals[i]);
            }
        }
        return s.toString();
    }

    public static String integerToAlphabetic(int number, boolean lowerCase) {
        if (number <= 0) {
            return Integer.toString(number);
        }

        StringBuilder sb = new StringBuilder();
        int value = number;
        while (value > 0) {
            value--;
            char ch = (char) ((lowerCase ? 'a' : 'A') + (value % 26));
            sb.insert(0, ch);
            value /= 26;
        }
        return sb.toString();
    }
}
