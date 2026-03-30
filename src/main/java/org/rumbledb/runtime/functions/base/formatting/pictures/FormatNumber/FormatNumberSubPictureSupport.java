package org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber;

import org.rumbledb.context.DecimalFormatDefinition;
import org.rumbledb.runtime.functions.base.formatting.GroupingPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FormatNumberSubPictureSupport {
    private FormatNumberSubPictureSupport() {
    }

    static boolean isMandatoryDigit(int cp, DecimalFormatDefinition decimalFormat) {
        int zeroDigit = decimalFormat.getZeroDigit();
        return cp >= zeroDigit && cp <= zeroDigit + 9;
    }

    static boolean isOptionalDigit(int cp, DecimalFormatDefinition decimalFormat) {
        return cp == decimalFormat.getOptionalDigit();
    }

    static boolean isDecimalSeparator(int cp, DecimalFormatDefinition decimalFormat) {
        return cp == decimalFormat.getDecimalSeparator();
    }

    static boolean isGroupingSeparator(int cp, DecimalFormatDefinition decimalFormat) {
        return cp == decimalFormat.getGroupingSeparator();
    }

    static boolean isPatternSeparator(int cp, DecimalFormatDefinition decimalFormat) {
        return cp == decimalFormat.getPatternSeparator();
    }

    static boolean isPercent(int cp, DecimalFormatDefinition decimalFormat) {
        return cp == decimalFormat.getPercent();
    }

    static boolean isPerMille(int cp, DecimalFormatDefinition decimalFormat) {
        return cp == decimalFormat.getPerMille();
    }

    static boolean isActiveCharacter(int cp, DecimalFormatDefinition decimalFormat) {
        return isDecimalSeparator(cp, decimalFormat)
            || isGroupingSeparator(cp, decimalFormat)
            || isOptionalDigit(cp, decimalFormat)
            || isMandatoryDigit(cp, decimalFormat);
    }

    static boolean isPassiveCharacter(int cp, DecimalFormatDefinition decimalFormat) {
        return !isActiveCharacter(cp, decimalFormat);
    }

    static int countActiveDigitSigns(String s, DecimalFormatDefinition decimalFormat) {
        int count = 0;
        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);
            if (isMandatoryDigit(cp, decimalFormat) || isOptionalDigit(cp, decimalFormat)) {
                count++;
            }
            i += Character.charCount(cp);
        }
        return count;
    }

    static int countMandatoryDigits(String s, DecimalFormatDefinition decimalFormat) {
        int count = 0;
        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);
            if (isMandatoryDigit(cp, decimalFormat)) {
                count++;
            }
            i += Character.charCount(cp);
        }
        return count;
    }

    static int findLeftmostActiveCharIndex(String s, DecimalFormatDefinition decimalFormat) {
        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);
            if (isActiveCharacter(cp, decimalFormat)) {
                return i;
            }
            i += Character.charCount(cp);
        }
        return -1;
    }

    static int findRightmostActiveCharIndex(String s, DecimalFormatDefinition decimalFormat) {
        int last = -1;
        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);
            if (isActiveCharacter(cp, decimalFormat)) {
                last = i;
            }
            i += Character.charCount(cp);
        }
        return last;
    }

    static int countOccurrences(
            String s,
            int targetCodePoint
    ) {
        int count = 0;
        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);
            if (cp == targetCodePoint) {
                count++;
            }
            i += Character.charCount(cp);
        }
        return count;
    }

    static int findDecimalSeparatorIndex(String s, DecimalFormatDefinition decimalFormat) {
        int found = -1;
        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);
            if (isDecimalSeparator(cp, decimalFormat)) {
                if (found != -1) {
                    return -2;
                }
                found = i;
            }
            i += Character.charCount(cp);
        }
        return found;
    }

    static List<GroupingPos> findIntegerGroupingPositions(String s, DecimalFormatDefinition decimalFormat) {
        List<GroupingPos> groupingPositions = new ArrayList<>();
        int digitSignsSeenToRight = 0;

        for (int i = s.length(); i > 0;) {
            int cp = s.codePointBefore(i);
            i -= Character.charCount(cp);

            if (isMandatoryDigit(cp, decimalFormat) || isOptionalDigit(cp, decimalFormat)) {
                digitSignsSeenToRight++;
            } else if (isGroupingSeparator(cp, decimalFormat)) {
                groupingPositions.add(new GroupingPos(digitSignsSeenToRight, decimalFormat.getGroupingSeparator()));
            }
        }

        return groupingPositions;
    }

    static List<GroupingPos> findFractionalGroupingPositions(String s, DecimalFormatDefinition decimalFormat) {
        List<GroupingPos> groupingPositions = new ArrayList<>();
        int digitSignsSeenToLeft = 0;

        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);

            if (isMandatoryDigit(cp, decimalFormat) || isOptionalDigit(cp, decimalFormat)) {
                digitSignsSeenToLeft++;
            } else if (isGroupingSeparator(cp, decimalFormat)) {
                groupingPositions.add(new GroupingPos(digitSignsSeenToLeft, decimalFormat.getGroupingSeparator()));
            }

            i += Character.charCount(cp);
        }

        return groupingPositions;
    }

    static boolean hasAdjacentGroupingAndDecimalSeparator(String s, DecimalFormatDefinition decimalFormat) {
        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);
            int nextIndex = i + Character.charCount(cp);

            if (nextIndex < s.length()) {
                int nextCp = s.codePointAt(nextIndex);
                boolean currentIsGrouping = isGroupingSeparator(cp, decimalFormat);
                boolean currentIsDecimal = isDecimalSeparator(cp, decimalFormat);
                boolean nextIsGrouping = isGroupingSeparator(nextCp, decimalFormat);
                boolean nextIsDecimal = isDecimalSeparator(nextCp, decimalFormat);

                if ((currentIsGrouping && nextIsDecimal) || (currentIsDecimal && nextIsGrouping)) {
                    return true;
                }
            }

            i = nextIndex;
        }
        return false;
    }

    static boolean hasMandatoryDigitFollowedByOptionalDigit(String s, DecimalFormatDefinition decimalFormat) {
        boolean seenMandatoryDigit = false;

        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);

            if (isMandatoryDigit(cp, decimalFormat)) {
                seenMandatoryDigit = true;
            } else if (isOptionalDigit(cp, decimalFormat) && seenMandatoryDigit) {
                return true;
            }

            i += Character.charCount(cp);
        }

        return false;
    }

    static boolean hasOptionalDigitFollowedByMandatoryDigit(String s, DecimalFormatDefinition decimalFormat) {
        boolean seenOptionalDigit = false;

        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);

            if (isOptionalDigit(cp, decimalFormat)) {
                seenOptionalDigit = true;
            } else if (isMandatoryDigit(cp, decimalFormat) && seenOptionalDigit) {
                return true;
            }

            i += Character.charCount(cp);
        }

        return false;
    }

    static boolean hasPassiveCharacterWithinActiveRegion(String activeRegion, DecimalFormatDefinition decimalFormat) {
        for (int i = 0; i < activeRegion.length();) {
            int cp = activeRegion.codePointAt(i);
            if (isPassiveCharacter(cp, decimalFormat)) {
                return true;
            }
            i += Character.charCount(cp);
        }
        return false;
    }

    static Integer findRepeatingIntegerGroupingInterval(List<GroupingPos> groupingPositions) {
        if (groupingPositions == null || groupingPositions.isEmpty()) {
            return null;
        }

        List<Integer> positions = new ArrayList<>();
        for (GroupingPos groupingPos : groupingPositions) {
            positions.add(groupingPos.distanceFromRight);
        }

        positions.sort(Integer::compareTo);

        int n = positions.get(0);

        if (n <= 0) {
            return null;
        }

        for (int i = 0; i < positions.size(); i++) {
            int expected = n * (i + 1);
            if (positions.get(i) != expected) {
                return null;
            }
        }

        return n;
    }

    static String applyDecimalDigitFamily(String s, DecimalFormatDefinition decimalFormat) {
        int zeroDigit = decimalFormat.getZeroDigit();
        if (zeroDigit == '0') {
            return s;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch >= '0' && ch <= '9') {
                int mapped = zeroDigit + (ch - '0');
                sb.appendCodePoint(mapped);
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    static boolean containsDecimalSeparator(FormatNumberSubPicture picture, DecimalFormatDefinition decimalFormat) {
        return picture.getRawPictureString()
            .contains(new String(Character.toChars(decimalFormat.getDecimalSeparator())));
    }


    static String applyIntegerPartGrouping(
            String digits,
            FormatNumberSubPicture picture
    ) {

        // TODO liegt identisch in NumericFormattingSupport, wenn sie sich ohne changes bewährt => zusammenziehen
        List<GroupingPos> groupingPositions = picture.getIntegerPartGroupingPositions();

        if (groupingPositions == null || groupingPositions.isEmpty()) {
            return digits;
        }

        String reversedDigits = new StringBuilder(digits).reverse().toString();

        Integer repeatingInterval = picture.getRepeatingIntegerGroupingInterval();

        String separator = new String(Character.toChars(groupingPositions.get(0).separatorCP));

        // TODO von hier
        if (repeatingInterval != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < reversedDigits.length(); i++) {
                if (i > 0 && i % repeatingInterval == 0) {
                    sb.append(separator);
                }
                sb.append(reversedDigits.charAt(i));
            }
            return sb.reverse().toString();
        }

        List<GroupingPos> gps = new ArrayList<>(groupingPositions);
        gps.sort(Comparator.comparingInt(GroupingPos::getDistanceFromRight));

        StringBuilder sb = new StringBuilder(reversedDigits);

        for (int i = gps.size() - 1; i >= 0; i--) {
            GroupingPos gp = gps.get(i);
            int insertPos = gp.distanceFromRight;
            if (insertPos > 0 && insertPos < sb.length()) {
                sb.insert(insertPos, separator);
            }
        }

        // TODO NACH HIER kann man sehr wahrscheinlich später zusammenführen

        return sb.reverse().toString();
    }

    static String applyFractionalPartGrouping(
            String digits,
            FormatNumberSubPicture picture
    ) { // TODO refactor pull together if necessary
        List<GroupingPos> groupingPositions = picture.getFractionalPartGroupingPositions();

        if (groupingPositions == null || groupingPositions.isEmpty()) {
            return digits;
        }

        String separator = new String(Character.toChars(groupingPositions.get(0).separatorCP));

        List<GroupingPos> gps = new ArrayList<>(groupingPositions);
        gps.sort(Comparator.comparingInt(GroupingPos::getDistanceFromRight));

        StringBuilder sb = new StringBuilder(digits);

        for (int i = gps.size() - 1; i >= 0; i--) {
            GroupingPos gp = gps.get(i);
            int insertPos = gp.getDistanceFromRight();

            if (insertPos > 0 && insertPos < sb.length()) {
                sb.insert(insertPos, separator);
            }
        }

        return sb.toString();
    }
}
