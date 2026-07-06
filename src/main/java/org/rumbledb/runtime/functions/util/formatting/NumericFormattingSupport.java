package org.rumbledb.runtime.functions.util.formatting;

import java.util.*;

/**
 * Locale-independent digit formatting: grouping, Roman and alphabetic. Locale-sensitive words live in
 * {@link NumberWords}.
 */
public final class NumericFormattingSupport {

    private NumericFormattingSupport() {
    }

    public static String applyGrouping(String digits, NumericPicture picture) {
        Integer repeatingInterval = picture.isRepeatingGrouping()
            ? picture.getRepeatingGroupingInterval()
            : null;
        return applyGrouping(digits, picture.getGroupingPositions(), repeatingInterval, true);
    }

    /**
     * Inserts grouping separators into a run of digits. With {@code groupFromRight} the positions are counted from the
     * right (integer part); otherwise from the left (fractional part). A non-null {@code repeatingInterval} groups at a
     * fixed periodic interval instead of at the explicit positions.
     */
    public static String applyGrouping(
            String digits,
            List<GroupingPos> groupingPositions,
            Integer repeatingInterval,
            boolean groupFromRight
    ) {
        if (groupingPositions == null || groupingPositions.isEmpty()) {
            return digits;
        }

        String working = groupFromRight ? new StringBuilder(digits).reverse().toString() : digits;

        if (repeatingInterval != null) {
            // Repeating grouping uses a single separator across all positions.
            String separator = new String(Character.toChars(groupingPositions.get(0).separatorCP()));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < working.length(); i++) {
                if (i > 0 && i % repeatingInterval == 0) {
                    sb.append(separator);
                }
                sb.append(working.charAt(i));
            }
            return groupFromRight ? sb.reverse().toString() : sb.toString();
        }

        List<GroupingPos> gps = new ArrayList<>(groupingPositions);
        gps.sort(Comparator.comparingInt(GroupingPos::distanceFromAnchor));

        StringBuilder sb = new StringBuilder(working);
        for (int i = gps.size() - 1; i >= 0; i--) {
            GroupingPos gp = gps.get(i);
            int insertPos = gp.distanceFromAnchor();
            if (insertPos > 0 && insertPos < sb.length()) {
                sb.insert(insertPos, new String(Character.toChars(gp.separatorCP())));
            }
        }

        return groupFromRight ? sb.reverse().toString() : sb.toString();
    }

    /**
     * Maps the ASCII digits of {@code s} into the Unicode digit family whose zero is {@code zeroDigit}, leaving every
     * other character unchanged.
     */
    public static String mapAsciiDigits(String s, int zeroDigit) {
        if (zeroDigit == '0') {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch >= '0' && ch <= '9') {
                sb.appendCodePoint(zeroDigit + (ch - '0'));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
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
