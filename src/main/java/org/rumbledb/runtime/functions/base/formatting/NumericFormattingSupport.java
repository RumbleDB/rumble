package org.rumbledb.runtime.functions.base.formatting;

import org.rumbledb.runtime.functions.base.formatting.language.LanguageFormatter;
import org.rumbledb.runtime.functions.base.formatting.language.LanguageRegistry;

import java.math.BigInteger;
import java.util.*;

public final class NumericFormattingSupport {

    private NumericFormattingSupport() {
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

    public static String toCardinal(int value, String language) {
        LanguageFormatter formatter = LanguageRegistry.resolve(language);
        return formatter.toCardinal(value);
    }

    public static String toOrdinal(int value, String language) {
        LanguageFormatter formatter = LanguageRegistry.resolve(language);
        return formatter.toOrdinal(value);
    }

    public static String ordinalSuffix(int value, String language) {
        return NumericFormattingSupport.ordinalSuffix(BigInteger.valueOf(value), language);
    }

    public static String ordinalSuffix(BigInteger value, String language) {
        LanguageFormatter formatter = LanguageRegistry.resolve(language);
        if (!formatter.supportsOrdinalSuffix()) {
            return "";
        }
        return formatter.ordinalSuffix(value);
    }
}
