package org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber;

import org.rumbledb.context.DecimalFormatDefinition;
import org.rumbledb.runtime.functions.base.formatting.GroupingPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntPredicate;

public class FormatNumberPictureSupport {
    private FormatNumberPictureSupport() {
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

    static boolean isExponentSeparator(int cp, DecimalFormatDefinition decimalFormat) {
        return cp == decimalFormat.getExponentSeparator();
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
            || isMandatoryDigit(cp, decimalFormat)
            || isExponentSeparator(cp, decimalFormat);
    }

    static boolean isPassiveCharacter(int cp, DecimalFormatDefinition decimalFormat) {
        return !isActiveCharacter(cp, decimalFormat);
    }

    /**
     * Context-sensitive activity test:
     * a character matching exponent-separator is only treated as active
     * if it is actually treated as an exponent-separator-sign in this subpicture,
     * i.e. if it is preceded and followed by an active character.
     */
    static boolean isActiveCharacterAt(String s, int index, DecimalFormatDefinition decimalFormat) {
        int cp = s.codePointAt(index);

        if (!isExponentSeparator(cp, decimalFormat)) {
            return isActiveCharacter(cp, decimalFormat);
        }

        if (index == 0) {
            return false;
        }

        int nextIndex = index + Character.charCount(cp);
        if (nextIndex >= s.length()) {
            return false;
        }

        int prevCp = s.codePointBefore(index);
        int nextCp = s.codePointAt(nextIndex);

        return isActiveCharacter(prevCp, decimalFormat)
            && isActiveCharacter(nextCp, decimalFormat);
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
            if (isActiveCharacterAt(s, i, decimalFormat)) {
                return i;
            }
            int cp = s.codePointAt(i);
            i += Character.charCount(cp);
        }
        return -1;
    }

    static int findRightmostActiveCharIndex(String s, DecimalFormatDefinition decimalFormat) {
        int last = -1;
        for (int i = 0; i < s.length();) {
            if (isActiveCharacterAt(s, i, decimalFormat)) {
                last = i;
            }
            int cp = s.codePointAt(i);
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

    static int findSeparatorIndex(String s, IntPredicate isSeparator) {
        int found = -1;
        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);
            if (isSeparator.test(cp)) {
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

    static boolean hasAdjacentGroupingSeparators(String s, DecimalFormatDefinition decimalFormat) {
        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);
            int nextIndex = i + Character.charCount(cp);

            if (nextIndex < s.length()) {
                int nextCp = s.codePointAt(nextIndex);
                if (isGroupingSeparator(cp, decimalFormat) && isGroupingSeparator(nextCp, decimalFormat)) {
                    return true;
                }
            }

            i = nextIndex;
        }
        return false;
    }

    static boolean hasInvalidIntegerGroupingPosition(List<GroupingPos> groupingPositions) {
        if (groupingPositions == null || groupingPositions.isEmpty()) {
            return false;
        }

        for (GroupingPos gp : groupingPositions) {
            if (gp.getDistanceFromRight() <= 0) {
                return true;
            }
        }

        return false;
    }

    static boolean hasGroupingSeparatorAtEndOfFractionalPart(
            String fractionalPart,
            List<GroupingPos> groupingPositions,
            DecimalFormatDefinition decimalFormat
    ) {
        if (groupingPositions == null || groupingPositions.isEmpty()) {
            return false;
        }

        int totalDigitSigns = countActiveDigitSigns(fractionalPart, decimalFormat);

        for (GroupingPos gp : groupingPositions) {
            if (gp.getDistanceFromRight() >= totalDigitSigns) {
                return true;
            }
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
            if (!isActiveCharacterAt(activeRegion, i, decimalFormat)) {
                return true;
            }
            int cp = activeRegion.codePointAt(i);
            i += Character.charCount(cp);
        }
        return false;
    }

    static Integer findRepeatingIntegerGroupingInterval(
            String integerPart,
            List<GroupingPos> groupingPositions,
            DecimalFormatDefinition decimalFormat
    ) {
        if (groupingPositions == null || groupingPositions.isEmpty()) {
            return null;
        }

        List<Integer> positions = new ArrayList<>();
        for (GroupingPos groupingPos : groupingPositions) {
            positions.add(groupingPos.getDistanceFromRight());
        }

        positions.sort(Integer::compareTo);

        int groupingSize = positions.get(0);
        if (groupingSize <= 0) {
            return null;
        }

        int totalDigitSigns = countActiveDigitSigns(integerPart, decimalFormat);

        boolean hasLeftEdgeSeparator = positions.get(positions.size() - 1) == totalDigitSigns;

        int expectedCount = (totalDigitSigns - 1) / groupingSize;
        if (hasLeftEdgeSeparator) {
            expectedCount += 1;
        }

        if (positions.size() != expectedCount) {
            return null;
        }

        int expected = groupingSize;
        int limit = hasLeftEdgeSeparator ? totalDigitSigns - groupingSize : totalDigitSigns - 1;

        for (int i = 0; i < positions.size(); i++) {
            int actual = positions.get(i);

            if (hasLeftEdgeSeparator && i == positions.size() - 1) {
                if (actual != totalDigitSigns) {
                    return null;
                }
            } else {
                if (actual != expected || actual > limit) {
                    return null;
                }
                expected += groupingSize;
            }
        }

        return groupingSize;
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

    static boolean containsOptionalDigit(String s, DecimalFormatDefinition decimalFormat) {
        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);
            if (isOptionalDigit(cp, decimalFormat)) {
                return true;
            }
            i += Character.charCount(cp);
        }
        return false;
    }

    static boolean containsOnlyMandatoryDigits(String s, DecimalFormatDefinition decimalFormat) {
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);
            if (!isMandatoryDigit(cp, decimalFormat)) {
                return false;
            }
            i += Character.charCount(cp);
        }
        return true;
    }

    static String applyIntegerPartGrouping(
            String digits,
            FormatNumberSubPicture picture
    ) {

        List<GroupingPos> groupingPositions = picture.getIntegerPartGroupingPositions();

        if (groupingPositions == null || groupingPositions.isEmpty()) {
            return digits;
        }

        String reversedDigits = new StringBuilder(digits).reverse().toString();

        Integer repeatingInterval = picture.getRepeatingIntegerGroupingInterval();

        String separator = new String(Character.toChars(groupingPositions.get(0).separatorCP));

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

        return sb.reverse().toString();
    }

    static String applyFractionalPartGrouping(
            String digits,
            FormatNumberSubPicture picture
    ) {
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

    static int findDecimalSeparatorIndex(String s, DecimalFormatDefinition decimalFormat) {
        return findSeparatorIndex(s, cp -> isDecimalSeparator(cp, decimalFormat));
    }

    static int findExponentSeparatorIndex(String s, DecimalFormatDefinition decimalFormat) {
        int found = -1;

        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);

            if (isExponentSeparator(cp, decimalFormat) && isActiveCharacterAt(s, i, decimalFormat)) {
                if (found != -1) {
                    return -2;
                }
                found = i;
            }

            i += Character.charCount(cp);
        }

        return found;
    }
}
