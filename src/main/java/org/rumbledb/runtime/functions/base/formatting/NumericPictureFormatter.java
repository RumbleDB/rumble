package org.rumbledb.runtime.functions.base.formatting;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.functions.numerics.IntegerFormatModifier;
import org.rumbledb.runtime.functions.numerics.IntegerPicture;
import org.rumbledb.runtime.functions.numerics.FullIntegerPictureParser;
import org.rumbledb.runtime.functions.numerics.PrimaryFormatToken;

import java.math.BigInteger;

public final class NumericPictureFormatter {
    private NumericPictureFormatter() {
    }

    public static String formatInteger(Item valueItem, String pictureString, ExceptionMetadata metadata) {
        // Invariant: value is neither null nor empty

        BigInteger temp = valueItem.getIntegerValue();
        boolean isNegative = temp.signum() < 0;
        BigInteger value = temp.abs();

        IntegerPicture picture = FullIntegerPictureParser.parse(pictureString, metadata);
        PrimaryFormatToken primary = picture.getPrimaryFormatToken();
        IntegerFormatModifier modifier = picture.getFormatModifier();

        String result;

        // Invariant: value => 0

        switch (primary.getType()) {
            case PrimaryFormatToken.DECIMAL:
                result = handleDecimal(value, primary, modifier);
                break;
            case PrimaryFormatToken.ALPHABETIC_UPPER:
                result = handleAlphabeticUpper(value, modifier);
                break;
            case PrimaryFormatToken.ALPHABETIC_LOWER:
                result = handleAlphabeticLower(value, modifier);
                break;
            case PrimaryFormatToken.ROMAN_UPPER:
                result = handleRomanUpper(value, modifier);
                break;
            case PrimaryFormatToken.ROMAN_LOWER:
                result = handleRomanLower(value, modifier);
                break;
            case PrimaryFormatToken.WORDS_LOWER:
                result = handleWordsLower(value, modifier);
                break;
            case PrimaryFormatToken.WORDS_UPPER:
                result = handleWordsUpper(value, modifier);
                break;
            case PrimaryFormatToken.WORDS_TITLE:
                result = handleWordsTitle(value, modifier);
                break;
            case PrimaryFormatToken.OTHER:
                result = handleOther(value, modifier);
                break;
            default:
                throw new OurBadException("Unknown type: " + primary.getType(), metadata);
        }

        return !isNegative ? result : ("-" + result);
    }

    private static String handleDecimal(
            BigInteger value,
            PrimaryFormatToken primary,
            IntegerFormatModifier modifier
    ) {
        NumericPicture picture = primary.getNumericPicture();

        String digits = value.toString();

        int zeroesRemaining = picture.getMandatoryDigitCount() - digits.length();
        if (zeroesRemaining > 0) {
            digits = "0".repeat(zeroesRemaining) + digits;
        }

        digits = IntegerFormattingSupport.applyGrouping(digits, picture);
        digits = NumericPictureParser.mapAsciiDigits(digits, picture.getZeroDigit());

        if (IntegerFormatModifier.ORDINAL.equals(modifier.getNumberType())) {
            digits = digits + IntegerFormattingSupport.ordinalSuffix(value);
        }

        return digits;
    }

    private static String handleRomanUpper(
            BigInteger value,
            IntegerFormatModifier modifier
    ) {
        if (value.signum() == 0 || value.compareTo(BigInteger.valueOf(3999)) > 0) {
            return handleOther(value, modifier);
        }

        String result = IntegerFormattingSupport.integerToRoman(value.intValueExact());

        // For Roman, unsupported ordinal handling is ignored and cardinal numbering is used.
        return result;
    }

    private static String handleRomanLower(
            BigInteger value,
            IntegerFormatModifier modifier
    ) {
        if (value.signum() == 0 || value.compareTo(BigInteger.valueOf(3999)) > 0) {
            return handleOther(value, modifier);
        }

        String result = IntegerFormattingSupport.integerToRoman(value.intValueExact())
            .toLowerCase(java.util.Locale.ROOT);

        // For Roman, unsupported ordinal handling is ignored and cardinal numbering is used.
        return result;
    }

    private static String handleAlphabeticUpper(
            BigInteger value,
            IntegerFormatModifier modifier
    ) {
        if (value.signum() == 0 || value.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            return handleOther(value, modifier);
        }

        String result = IntegerFormattingSupport.integerToAlphabetic(value.intValueExact(), false);

        // For alphabetic numbering, unsupported ordinal handling may be ignored per spec.
        return result;
    }

    private static String handleAlphabeticLower(
            BigInteger value,
            IntegerFormatModifier modifier
    ) {
        if (value.signum() == 0 || value.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            return handleOther(value, modifier);
        }

        // For alphabetic numbering, unsupported ordinal handling may be ignored per spec.
        return IntegerFormattingSupport.integerToAlphabetic(value.intValueExact(), true);
    }

    private static String handleWordsLower(
            BigInteger value,
            IntegerFormatModifier modifier
    ) {
        if (value.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            return handleOther(value, modifier);
        }

        if (IntegerFormatModifier.ORDINAL.equals(modifier.getNumberType())) {
            return IntegerFormattingSupport.toEnglishOrdinalWords(value.intValueExact());
        }

        return IntegerFormattingSupport.toEnglishCardinalWords(value.intValueExact());
    }

    private static String handleWordsUpper(
            BigInteger value,
            IntegerFormatModifier modifier
    ) {
        String result = handleWordsLower(value, modifier);
        return result.toUpperCase(java.util.Locale.ROOT);
    }

    private static String handleWordsTitle(
            BigInteger value,
            IntegerFormatModifier modifier
    ) {
        String result = handleWordsLower(value, modifier);
        return toTitleCaseWords(result);
    }

    private static String handleOther(
            BigInteger value,
            IntegerFormatModifier modifier
    ) {
        String result = value.toString();

        if (IntegerFormatModifier.ORDINAL.equals(modifier.getNumberType())) {
            result = result + IntegerFormattingSupport.ordinalSuffix(value);
        }

        return result;
    }

    private static String toTitleCaseWords(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        boolean capitalizeNext = true;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (Character.isLetter(ch)) {
                if (capitalizeNext) {
                    sb.append(Character.toUpperCase(ch));
                    capitalizeNext = false;
                } else {
                    sb.append(Character.toLowerCase(ch));
                }
            } else {
                sb.append(ch);
                capitalizeNext = (ch == ' ' || ch == '-');
            }
        }

        return sb.toString();
    }
}
