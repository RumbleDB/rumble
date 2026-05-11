package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;
import org.rumbledb.runtime.functions.base.formatting.NumericPicture;
import org.rumbledb.runtime.functions.base.formatting.NumericPictureParser;

final class TimezonePictureParser {

    private TimezonePictureParser() {
    }

    static ParsedTimezonePicture parse(
            char component,
            String presentation,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        if (presentation.isEmpty()) {
            return component == 'z'
                ? ParsedTimezonePicture.defaultGmt()
                : ParsedTimezonePicture.defaultNumeric();
        }

        if (component == 'Z' && "Z".equals(presentation)) {
            return ParsedTimezonePicture.military();
        }

        String core = presentation;
        boolean useZForZero = false;
        if (core.endsWith("t")) {
            useZForZero = true;
            core = core.substring(0, core.length() - 1);
        }

        if (core.isEmpty()) {
            throw invalidPicture(pictureStringForErrors, metadata);
        }

        if (component == 'z') {
            return parseNumericTimezone(core, true, useZForZero, pictureStringForErrors, metadata);
        }
        return parseNumericTimezone(core, false, useZForZero, pictureStringForErrors, metadata);
    }

    private static ParsedTimezonePicture parseNumericTimezone(
            String core,
            boolean gmtPrefix,
            boolean useZForZero,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        int sepIndex = findSeparatorIndex(core);
        if (sepIndex >= 0) {
            String left = core.substring(0, sepIndex);
            String right = core.substring(sepIndex + 1);

            NumericPicture hourPic = NumericPictureParser.parseForDate(left, pictureStringForErrors, metadata);
            NumericPicture minPic = NumericPictureParser.parseForDate(right, pictureStringForErrors, metadata);

            return ParsedTimezonePicture.custom(
                gmtPrefix,
                true,
                String.valueOf(core.charAt(sepIndex)),
                hourPic.getMandatoryDigitCount(),
                minPic.getMandatoryDigitCount(),
                hourPic.getZeroDigit(),
                useZForZero,
                false,
                false
            );
        }

        NumericPicture pic = NumericPictureParser.parseForDate(core, pictureStringForErrors, metadata);
        int digits = pic.getActiveDigitCount();

        if (digits == 1 || digits == 2) {
            return ParsedTimezonePicture.custom(
                gmtPrefix,
                false,
                ":",
                pic.getMandatoryDigitCount(),
                2,
                pic.getZeroDigit(),
                useZForZero,
                true,
                false
            );
        }

        if (digits == 3 || digits == 4) {
            return ParsedTimezonePicture.custom(
                gmtPrefix,
                true,
                "",
                digits == 4 ? 2 : 1,
                2,
                pic.getZeroDigit(),
                useZForZero,
                false,
                true
            );
        }

        throw invalidPicture(pictureStringForErrors, metadata);
    }

    private static int findSeparatorIndex(String s) {
        int[] cps = s.codePoints().toArray();
        int utf16Index = 0;
        for (int cp : cps) {
            int charCount = Character.charCount(cp);
            if (cp == '#' || Character.getType(cp) == Character.DECIMAL_DIGIT_NUMBER) {
                utf16Index += charCount;
                continue;
            }
            return utf16Index;
        }
        return -1;
    }

    private static IncorrectSyntaxFormatDateTimeException invalidPicture(
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        return new IncorrectSyntaxFormatDateTimeException(
                "\"" + pictureStringForErrors + "\": invalid picture string",
                metadata
        );
    }
}
