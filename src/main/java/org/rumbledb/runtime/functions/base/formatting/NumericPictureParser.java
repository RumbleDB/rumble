package org.rumbledb.runtime.functions.base.formatting;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;
import org.rumbledb.exceptions.IncorrectSyntaxFormatNumberException;
import org.rumbledb.exceptions.RumbleException;

public final class NumericPictureParser {

    public static class NumericPictureKind {
        private NumericPictureKind() {
        }

        public static String DATE = "DATE";
        public static String INTEGER = "INTEGER";
    }

    private NumericPictureParser() {
    }

    private static NumericPicture parse(
            String picture,
            String pictureStringForErrors,
            ExceptionMetadata metadata,
            String kind
    ) {
        if (picture == null || picture.isEmpty()) {
            throw invalidPicture(pictureStringForErrors, metadata, kind);
        }

        int[] cps = picture.codePoints().toArray();
        int zeroDigit = -1;
        boolean sawMandatory = false;
        boolean lastWasGrouping = false;

        List<GroupingPos> groupings = new ArrayList<>();

        for (int i = 0; i < cps.length; i++) {
            int cp = cps[i];

            if (cp == '#') {
                if (sawMandatory) {
                    throw invalidPicture(pictureStringForErrors, metadata, kind);
                }
                lastWasGrouping = false;
                continue;
            }

            if (isDecimalDigit(cp)) {
                int z = zeroDigitOf(cp);
                if (z < 0) {
                    throw invalidPicture(pictureStringForErrors, metadata, kind);
                }
                if (zeroDigit < 0) {
                    zeroDigit = z;
                } else if (zeroDigit != z) {
                    throw invalidPicture(pictureStringForErrors, metadata, kind);
                }
                sawMandatory = true;
                lastWasGrouping = false;
                continue;
            }

            if (isGroupingSeparator(cp)) {
                if (i == 0 || i == cps.length - 1 || lastWasGrouping) {
                    throw invalidPicture(pictureStringForErrors, metadata, kind);
                }
                int digitsToRight = countActiveDigitsToRight(cps, i);
                if (digitsToRight <= 0) {
                    throw invalidPicture(pictureStringForErrors, metadata, kind);
                }
                groupings.add(new GroupingPos(digitsToRight, new String(Character.toChars(cp))));
                lastWasGrouping = true;
                continue;
            }

            throw invalidPicture(pictureStringForErrors, metadata, kind);
        }

        int mandatoryCount = countMandatoryDigits(cps);
        int activeCount = countActiveDigits(cps);

        if (mandatoryCount == 0 || zeroDigit < 0) {
            throw invalidPicture(pictureStringForErrors, metadata, kind);
        }

        List<Integer> runLengths = computeActiveRunLengths(cps);
        RepeatingGroupingInfo repeatingInfo = detectRepeatingGrouping(groupings, runLengths, kind);

        return new NumericPicture(
                picture,
                zeroDigit,
                mandatoryCount,
                activeCount,
                groupings,
                repeatingInfo.repeating,
                repeatingInfo.interval
        );
    }

    public static NumericPicture parseForDate(
            String picture,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        return parse(picture, pictureStringForErrors, metadata, NumericPictureKind.DATE);
    }

    public static NumericPicture parseForInteger(
            String picture,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        return parse(picture, pictureStringForErrors, metadata, NumericPictureKind.INTEGER);
    }

    public static String mapAsciiDigits(String s, int zeroDigit) {
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

    public static int zeroDigitOf(int cp) {
        int n = Character.getNumericValue(cp);
        if (n < 0 || n > 9) {
            return -1;
        }
        return cp - n;
    }

    private static boolean isDecimalDigit(int cp) {
        return Character.getType(cp) == Character.DECIMAL_DIGIT_NUMBER;
    }

    private static int countMandatoryDigits(int[] cps) {
        int count = 0;
        for (int cp : cps) {
            if (isDecimalDigit(cp)) {
                count++;
            }
        }
        return count;
    }

    private static int countActiveDigits(int[] cps) {
        int count = 0;
        for (int cp : cps) {
            if (cp == '#' || isDecimalDigit(cp)) {
                count++;
            }
        }
        return count;
    }

    private static int countActiveDigitsToRight(int[] cps, int index) {
        int count = 0;
        for (int i = index + 1; i < cps.length; i++) {
            if (cps[i] == '#' || isDecimalDigit(cps[i])) {
                count++;
            }
        }
        return count;
    }

    private static List<Integer> computeActiveRunLengths(int[] cps) {
        List<Integer> runs = new ArrayList<>();
        int current = 0;

        for (int cp : cps) {
            if (cp == '#' || isDecimalDigit(cp)) {
                current++;
            } else if (isGroupingSeparator(cp)) {
                runs.add(current);
                current = 0;
            }
        }

        runs.add(current);
        return runs;
    }

    private static RepeatingGroupingInfo detectRepeatingGrouping(
            List<GroupingPos> groupings,
            List<Integer> runLengths,
            String kind
    ) {
        if (kind != NumericPictureKind.INTEGER) {
            return new RepeatingGroupingInfo(false, 0);
        }

        if (groupings.isEmpty()) {
            return new RepeatingGroupingInfo(false, 0);
        }

        String separator = groupings.get(0).separator;
        for (GroupingPos gp : groupings) {
            if (!separator.equals(gp.separator)) {
                return new RepeatingGroupingInfo(false, 0);
            }
        }

        if (runLengths.size() < 2) {
            return new RepeatingGroupingInfo(false, 0);
        }

        int interval = runLengths.get(1);
        if (interval <= 0) {
            return new RepeatingGroupingInfo(false, 0);
        }

        for (int i = 1; i < runLengths.size(); i++) {
            if (runLengths.get(i) != interval) {
                return new RepeatingGroupingInfo(false, 0);
            }
        }

        if (runLengths.get(0) > interval) {
            return new RepeatingGroupingInfo(false, 0);
        }

        return new RepeatingGroupingInfo(true, interval);
    }

    private static boolean isGroupingSeparator(int cp) {
        switch (Character.getType(cp)) {
            case Character.DECIMAL_DIGIT_NUMBER:
            case Character.LETTER_NUMBER:
            case Character.OTHER_NUMBER:
            case Character.UPPERCASE_LETTER:
            case Character.LOWERCASE_LETTER:
            case Character.TITLECASE_LETTER:
            case Character.MODIFIER_LETTER:
            case Character.OTHER_LETTER:
                return false;
            default:
                return true;
        }
    }

    private static RumbleException invalidPicture(
            String pictureStringForErrors,
            ExceptionMetadata metadata,
            String kind
    ) {
        String message = "\"" + pictureStringForErrors + "\": invalid picture string";

        if (kind == NumericPictureKind.DATE) {
            return new IncorrectSyntaxFormatDateTimeException(message, metadata);
        }

        return new IncorrectSyntaxFormatNumberException(message, metadata);
    }

    private static final class RepeatingGroupingInfo {
        private final boolean repeating;
        private final int interval;

        private RepeatingGroupingInfo(boolean repeating, int interval) {
            this.repeating = repeating;
            this.interval = interval;
        }
    }
}
