package org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber;

import org.rumbledb.api.Item;
import org.rumbledb.context.DecimalFormatDefinition;
import org.rumbledb.exceptions.ExceptionMetadata;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberPictureFormatter {
    private NumberPictureFormatter() {
    }

    public static String format(
            Item valueItem,
            Item pictureItem,
            Item decimalFormatNameItem,
            DecimalFormatDefinition defaultDecimalFormat,
            ExceptionMetadata metadata
    ) {
        DecimalFormatDefinition decimalFormat = defaultDecimalFormat;;

        // TODO
        if (decimalFormatNameItem == null) {
            decimalFormat = defaultDecimalFormat;
        } else {
            // TODO resolve decimalFormatName and handle accordingly
        }
        String pictureString = pictureItem.getStringValue();

        FormatNumberPicture picture = FormatNumberPictureParser.parse(pictureString, decimalFormat, metadata);


        System.err.println("\n\n" + debugValueItem(valueItem));
        System.err.println(debugFormatNumberPicture(pictureString, picture) + "\n\n");

        // Handling Special Cases for Double and Float

        if (valueItem.isDouble()) {
            double value = valueItem.getDoubleValue();

            if (Double.isNaN(value)) {
                return decimalFormat.getNanSymbol();
            } else if (value == Double.POSITIVE_INFINITY) {
                return infinityRepresentation(picture.getPositiveSubPicture(), decimalFormat);
            } else if (value == Double.NEGATIVE_INFINITY) {
                return infinityRepresentation(picture.getNegativeSubPicture(), decimalFormat);
            }

        } else if (valueItem.isFloat()) {
            float value = valueItem.getFloatValue();

            if (Float.isNaN(value)) {
                return decimalFormat.getNanSymbol();
            } else if (value == Float.POSITIVE_INFINITY) {
                return infinityRepresentation(picture.getPositiveSubPicture(), decimalFormat);
            } else if (value == Float.NEGATIVE_INFINITY) {
                return infinityRepresentation(picture.getNegativeSubPicture(), decimalFormat);
            }
        }

        ResolvedNumber resolvedNumber = FormatNumberTypeResolver.getValue(valueItem);
        if (resolvedNumber.isNegative) {
            return applySubPictureFormatting(resolvedNumber, picture.getNegativeSubPicture(), decimalFormat);
        } else {
            return applySubPictureFormatting(resolvedNumber, picture.getPositiveSubPicture(), decimalFormat);
        }
    }

    /**
     * Formats a number according to the second phase of fn:format-number.
     *
     * <p>
     * The following will have already happened and can be safely ignored:
     * </p>
     *
     * <ol>
     * <li>If the input number is NaN, return the NaN symbol without prefix or suffix.</li>
     * <li>Choose the positive sub-picture for positive numbers and positive zero,
     * otherwise choose the negative sub-picture. Negative zero is treated as negative.</li>
     * <li>If the number is positive or negative infinity, return
     * prefix + infinity symbol + suffix.</li>
     * </ol>
     */
    private static String applySubPictureFormatting(
            ResolvedNumber resolvedNumber,
            FormatNumberSubPicture picture,
            DecimalFormatDefinition decimalFormat
    ) {
        // 1. If the sub-picture contains a percent sign, multiply the number by 100. If it contains a per-mille sign,
        // multiply the number by 1000.
        // 2. Convert the adjusted number to an arbitrary-precision decimal representation.
        BigDecimal number = resolvedNumber.decimalValue;
        if (ResolvedNumber.DOUBLE.equals(resolvedNumber.type)) {
            if (picture.getHasPercent()) {
                double doubleNumber = resolvedNumber.doubleValue;
                doubleNumber *= 100;
                number = BigDecimal.valueOf(doubleNumber);
            } else if (picture.getHasPerMille()) {
                double doubleNumber = resolvedNumber.doubleValue;
                doubleNumber *= 1000;
                number = BigDecimal.valueOf(doubleNumber);
            }
        } else if (ResolvedNumber.FLOAT.equals(resolvedNumber.type)) {
            if (picture.getHasPercent()) {
                float doubleNumber = resolvedNumber.floatValue;
                doubleNumber *= 100;
                number = BigDecimal.valueOf(doubleNumber);
            } else if (picture.getHasPerMille()) {
                float floatNumber = resolvedNumber.floatValue;
                floatNumber *= 1000;
                number = BigDecimal.valueOf(floatNumber);
            }
        } else {
            if (picture.getHasPercent())
                number = resolvedNumber.decimalValue.multiply(new BigDecimal(100));
            else if (picture.getHasPerMille())
                number = resolvedNumber.decimalValue.multiply(new BigDecimal(1000));
        }

        // 3. Round the adjusted number with round-half-to-even to maximum-fractional-part-size digits.
        int maxFrac = picture.getMaximumFractionalPartSize();
        number = number.setScale(maxFrac, RoundingMode.HALF_EVEN);

        // 4. Convert the absolute rounded number to decimal notation using the decimal digit family and decimal
        // separator, with no insignificant leading or trailing zeroes.
        number = number.abs();
        BigDecimal stripped = number.stripTrailingZeros();
        String numberString = stripped.toPlainString();

        if (stripped.compareTo(BigDecimal.ZERO) == 0) {
            numberString = ".";
        } else if (numberString.startsWith("0.")) {
            numberString = numberString.substring(1);
        }

        // If the integer part has fewer than minimum-integer-part-size digits, pad it with leading zeroes.
        // If the fractional part has fewer than minimum-fractional-part-size digits, pad it with trailing zeroes.
        String integerPart;
        String fractionalPart;

        if (".".equals(numberString)) {
            integerPart = "";
            fractionalPart = "";
        } else {
            int dot = numberString.indexOf('.');
            if (dot >= 0) {
                integerPart = numberString.substring(0, dot);
                fractionalPart = numberString.substring(dot + 1);
            } else {
                integerPart = numberString;
                fractionalPart = "";
            }
        }

        String leading = getZeroPadding(integerPart, picture, false);
        String trailing = getZeroPadding(fractionalPart, picture, true);

        String paddedIntegerPart = leading + integerPart;

        String paddedFractionalPart = fractionalPart + trailing;

        // Insert grouping separators in the integer part according to the integer-part-grouping-positions.
        paddedIntegerPart = FormatNumberPictureSupport.applyIntegerPartGrouping(paddedIntegerPart, picture);

        // Insert grouping separators in the fractional part according to the fractional-part-grouping-positions.
        paddedFractionalPart = FormatNumberPictureSupport.applyFractionalPartGrouping(paddedFractionalPart, picture);


        // Remove the decimal separator if the sub-picture does not contain one, or if no fractional digits remain.
        String formattedNumber = paddedIntegerPart;
        if (
            !paddedFractionalPart.isEmpty()
                && FormatNumberPictureSupport.findDecimalSeparatorIndex(
                    picture.getRawPictureString(),
                    decimalFormat
                ) > 0
        ) {
            formattedNumber += new String(Character.toChars(decimalFormat.getDecimalSeparator()))
                + paddedFractionalPart;
        }

        formattedNumber = FormatNumberPictureSupport.applyDecimalDigitFamily(formattedNumber, decimalFormat);

        return picture.getPrefix() + formattedNumber + picture.getSuffix();
    }

    private static String infinityRepresentation(
            FormatNumberSubPicture picture,
            DecimalFormatDefinition decimalFormat
    ) {
        return picture.getPrefix() + decimalFormat.getInfinity() + picture.getSuffix();
    }

    // Returns ASCII Zero Padding
    private static String getZeroPadding(String numberPart, FormatNumberSubPicture picture, boolean isFractional) {
        int zeroesRequired = 0;
        if (!isFractional)
            zeroesRequired = picture.getMinimumIntegerPartSize() - numberPart.length(); // IntegerPart
        else
            zeroesRequired = picture.getMinimumFractionalPartSize() - numberPart.length(); // FractionalPart
        if (zeroesRequired > 0)
            return "0".repeat(zeroesRequired);
        return "";
    }


    // DEBUGGING METHODS

    private static String debugValueItem(Item item) {
        return "Item value="
            + FormatNumberTypeResolver.getValue(item)
            + ", type="
            + debugNumberType(item);
    }

    private static String debugNumberType(Item item) {
        if (item.isInteger()) {
            return "INTEGER";
        }

        if (item.isDecimal()) {
            return "DECIMAL";
        }

        if (item.isDouble()) {
            return "DOUBLE";
        }

        if (item.isFloat()) {
            return "FLOAT";
        }
        return "ERROR";
    }

    private static String debugFormatNumberPicture(String rawPictureString, FormatNumberPicture picture) {
        return "rawPictureString: "
            + rawPictureString
            + " - positive["
            + debugSubpicture(picture.getPositiveSubPicture())
            + "]"
            + " negative["
            + debugSubpicture(picture.getNegativeSubPicture())
            + "]";
    }

    private static String debugSubpicture(FormatNumberSubPicture subpicture) {
        if (subpicture == null) {
            return "null";
        }

        return "prefix="
            + subpicture.getPrefix()
            + " ,integer="
            + subpicture.getIntegerPart()
            + " ,fractional="
            + subpicture.getFractionalPart()
            + " ,suffix="
            + subpicture.getSuffix()
            + " , hasExponent="
            + subpicture.hasExponent()
            + " , exponentPart="
            + subpicture.getExponentPart()
            + " , scalingFactor="
            + subpicture.getScalingFactor()
            + " ,percent="
            + subpicture.getHasPercent()
            + " ,permille="
            + subpicture.getHasPerMille()
            + " ,intGroups="
            + subpicture.getIntegerPartGroupingPositions()
            + " ,repeat="
            + subpicture.getRepeatingIntegerGroupingInterval()
            + " ,fracGroups="
            + subpicture.getFractionalPartGroupingPositions()
            + " ,minInt="
            + subpicture.getMinimumIntegerPartSize()
            + " ,minFrac="
            + subpicture.getMinimumFractionalPartSize()
            + " ,maxFrac="
            + subpicture.getMaximumFractionalPartSize();
    }
}
