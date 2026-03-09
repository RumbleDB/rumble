package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatNumberException;
import org.rumbledb.runtime.functions.base.formatting.NumericPicture;
import org.rumbledb.runtime.functions.base.formatting.NumericPictureParser;

public final class FullIntegerPictureParser {

    public static IntegerPicture parse(String pictureString, ExceptionMetadata metadata) {
        if (pictureString == null || pictureString.isEmpty()) {
            throw invalidPicture(pictureString, metadata);
        }

        int semiIndex = pictureString.lastIndexOf(';');

        String primaryFormatTokenString;
        String formatModifierString;

        if (semiIndex == -1) {
            primaryFormatTokenString = pictureString;
            formatModifierString = "";
        } else {
            primaryFormatTokenString = pictureString.substring(0, semiIndex);
            formatModifierString = pictureString.substring(semiIndex + 1);
        }

        if (primaryFormatTokenString.isEmpty()) {
            throw invalidPicture(pictureString, metadata);
        }

        IntegerFormatModifier formatModifier = parseFormatModifier(
            formatModifierString,
            pictureString,
            metadata
        );

        PrimaryFormatToken primaryFormatToken = parsePrimaryFormatToken(
            primaryFormatTokenString,
            pictureString,
            metadata
        );

        return new IntegerPicture(
                pictureString,
                primaryFormatTokenString,
                formatModifierString,
                primaryFormatToken,
                formatModifier
        );
    }

    private static PrimaryFormatToken parsePrimaryFormatToken(
            String primaryFormatTokenString,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        if (containsDecimalDigit(primaryFormatTokenString)) {
            NumericPicture numericPicture = NumericPictureParser.parseForInteger(
                primaryFormatTokenString,
                pictureStringForErrors,
                metadata
            );
            return PrimaryFormatToken.decimal(numericPicture);
        }

        switch (primaryFormatTokenString) {
            case "Ww":
                return PrimaryFormatToken.wordsTitle();
            case "W":
                return PrimaryFormatToken.wordsUpper();
            case "w":
                return PrimaryFormatToken.wordsLower();
            case "A":
                return PrimaryFormatToken.alphabeticUpper();
            case "a":
                return PrimaryFormatToken.alphabeticLower();
            case "I":
                return PrimaryFormatToken.romanUpper();
            case "i":
                return PrimaryFormatToken.romanLower();
            default:
                return PrimaryFormatToken.other(primaryFormatTokenString);
        }
    }

    private static IntegerFormatModifier parseFormatModifier(
            String formatModifierString,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        int i = 0;
        String numberType = IntegerFormatModifier.CARDINAL;
        String formatSpecifier = null;
        String numberingVariant = null;
        boolean sawCardinalityMarker = false;

        if (i < formatModifierString.length()) {
            char c = formatModifierString.charAt(i);
            if (c == 'c') {
                numberType = IntegerFormatModifier.CARDINAL;
                sawCardinalityMarker = true;
                i++;
            } else if (c == 'o') {
                numberType = IntegerFormatModifier.ORDINAL;
                sawCardinalityMarker = true;
                i++;
            }
        }

        if (i < formatModifierString.length() && formatModifierString.charAt(i) == '(') {
            if (!sawCardinalityMarker) {
                throw invalidPicture(pictureStringForErrors, metadata);
            }

            int closing = formatModifierString.indexOf(')', i + 1);
            if (closing < 0 || closing == i + 1) {
                throw invalidPicture(pictureStringForErrors, metadata);
            }

            formatSpecifier = formatModifierString.substring(i + 1, closing);
            i = closing + 1;
        }

        if (i < formatModifierString.length()) {
            char c = formatModifierString.charAt(i);
            if (c == 'a') {
                numberingVariant = IntegerFormatModifier.ALPHABETIC;
                i++;
            } else if (c == 't') {
                numberingVariant = IntegerFormatModifier.TRADITIONAL;
                i++;
            } else {
                throw invalidPicture(pictureStringForErrors, metadata);
            }
        }

        if (i != formatModifierString.length()) {
            throw invalidPicture(pictureStringForErrors, metadata);
        }

        return new IntegerFormatModifier(numberType, formatSpecifier, numberingVariant);
    }

    private static boolean containsDecimalDigit(String s) {
        for (int i = 0; i < s.length();) {
            int cp = s.codePointAt(i);
            if (Character.getType(cp) == Character.DECIMAL_DIGIT_NUMBER) {
                return true;
            }
            i += Character.charCount(cp);
        }
        return false;
    }

    private static IncorrectSyntaxFormatNumberException invalidPicture(
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        return new IncorrectSyntaxFormatNumberException(
                "\"" + pictureStringForErrors + "\": invalid picture string",
                metadata
        );
    }
}
