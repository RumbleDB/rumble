package org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber;

import org.rumbledb.api.Item;
import org.rumbledb.context.DecimalFormatDefinition;
import org.rumbledb.context.DecimalFormatRuntimeConfig;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidDecimalFormatName;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class NumberPictureFormatter {
    private NumberPictureFormatter() {
    }

    public static String format(
            Item valueItem,
            Item pictureItem,
            Item decimalFormatNameItem,
            DecimalFormatRuntimeConfig decimalFormatRuntimeConfig,
            ExceptionMetadata metadata
    ) {
        DecimalFormatDefinition decimalFormat = decimalFormatRuntimeConfig.getDefaultDecimalFormat();

        if (decimalFormatNameItem != null) {
            decimalFormat = resolveDecimalFormat(decimalFormatNameItem, decimalFormatRuntimeConfig, metadata);
        }

        String pictureString = pictureItem.getStringValue();

        FormatNumberPicture picture = FormatNumberPictureParser.parse(pictureString, decimalFormat, metadata);

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

    private static DecimalFormatDefinition resolveDecimalFormat(
            Item decimalFormatNameItem,
            DecimalFormatRuntimeConfig decimalFormatRuntimeConfig,
            ExceptionMetadata metadata
    ) {
        String lexicalName = decimalFormatNameItem.getStringValue();
        String trimmedName = lexicalName == null ? "" : lexicalName.trim();

        if (trimmedName.isEmpty()) {
            return decimalFormatRuntimeConfig.getDefaultDecimalFormat();
        }

        Name resolvedName = resolveDecimalFormatName(
            trimmedName,
            decimalFormatRuntimeConfig.getStaticallyKnownNamespaces(),
            metadata
        );

        if (!decimalFormatRuntimeConfig.hasDecimalFormat(resolvedName)) {
            throw new InvalidDecimalFormatName(
                    "Decimal format not found: " + trimmedName,
                    metadata
            );
        }

        return decimalFormatRuntimeConfig.getDecimalFormat(resolvedName);
    }

    private static Name resolveDecimalFormatName(
            String text,
            Map<String, String> staticallyKnownNamespaces,
            ExceptionMetadata metadata
    ) {
        if (text.startsWith("Q{")) {
            int closingBrace = text.indexOf('}');
            if (closingBrace < 0 || closingBrace == text.length() - 1) {
                throw new InvalidDecimalFormatName(
                        "Invalid URIQualifiedName: " + text,
                        metadata
                );
            }

            String namespace = text.substring(2, closingBrace);
            String localName = text.substring(closingBrace + 1);

            if (localName.isEmpty()) {
                throw new InvalidDecimalFormatName(
                        "Invalid URIQualifiedName, missing local name: " + text,
                        metadata
                );
            }

            return new Name(namespace, null, localName);
        }

        int colon = text.indexOf(':');

        if (colon < 0) {
            return Name.createVariableInNoNamespace(text);
        }

        String prefix = text.substring(0, colon);
        String localName = text.substring(colon + 1);

        if (prefix.isEmpty() || localName.isEmpty()) {
            throw new InvalidDecimalFormatName(
                    "Invalid QName: " + text,
                    metadata
            );
        }

        String namespace = staticallyKnownNamespaces.get(prefix);
        if (namespace == null) {
            throw new InvalidDecimalFormatName(
                    "Prefix " + prefix + " could not be resolved against a namespace in scope.",
                    metadata
            );
        }

        return new Name(namespace, prefix, localName);
    }

    /**
     * Formats a number according to the second phase of fn:format-number.
     *
     * <p>
     * The following should have already happened and will therefore be ignored:
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
        AdjustedNumber adjusted = computeAdjustedNumber(resolvedNumber, picture);

        // If the operation results in numeric overflow, adjusted number is infinity.
        if (adjusted.infinite) {
            return infinityRepresentation(picture, decimalFormat);
        }

        BigDecimal mantissa = adjusted.value;
        Integer exponent = null;

        // 3. If the minimum exponent size is non-zero, then the adjusted number is scaled to establish a mantissa and
        // an integer exponent. The mantissa and exponent are chosen such that:
        // - the mantissa multiplied by ten to the power of the exponent is equal to the adjusted number, and
        // - the mantissa is less than 10^N and at least 10^(N-1), where N is the scaling factor.
        if (picture.getMinimumExponentPartSize() != 0) {
            MantissaExponentPair mantissaExponentPair = computeMantissaExponent(
                adjusted.value,
                picture.getScalingFactor()
            );
            mantissa = mantissaExponentPair.mantissa;
            exponent = mantissaExponentPair.exponent;
        }

        // 4. Convert the mantissa to xs:decimal and round with round-half-to-even to
        // maximum-fractional-part-size digits.
        int maxFrac = picture.getMaximumFractionalPartSize();
        mantissa = mantissa.setScale(maxFrac, RoundingMode.HALF_EVEN);

        // If rounding causes the mantissa to overflow its allowed range, normalize it and adjust the exponent.
        if (exponent != null) {
            MantissaExponentPair normalized = normalizeMantissaAfterRounding(
                mantissa,
                exponent,
                picture.getScalingFactor()
            );
            mantissa = normalized.mantissa;
            exponent = normalized.exponent;
        }

        String formattedNumber = formatMantissa(mantissa, picture, decimalFormat);

        // If an exponent exists, append exponent-separator, optional minus-sign, and the exponent digits padded
        // to the minimum exponent size.
        if (exponent != null) {
            formattedNumber += formatExponent(
                exponent,
                picture.getMinimumExponentPartSize(),
                decimalFormat
            );
        }

        return picture.getPrefix() + formattedNumber + picture.getSuffix();
    }

    private static String formatMantissa(
            BigDecimal number,
            FormatNumberSubPicture picture,
            DecimalFormatDefinition decimalFormat
    ) {
        // 5. Convert the absolute rounded number to decimal notation using the decimal digit family and decimal
        // separator, with no insignificant leading or trailing zeroes. At this stage the string must always contain
        // a decimal separator; zero is represented by the decimal separator on its own.
        number = number.abs();
        String numberString = toCanonicalDecimalString(number);

        // If the integer part has fewer than minimum-integer-part-size digits, pad it with leading zeroes.
        // If the fractional part has fewer than minimum-fractional-part-size digits, pad it with trailing zeroes.
        String integerPart;
        String fractionalPart;

        if (".".equals(numberString)) {
            integerPart = "";
            fractionalPart = "";
        } else {
            int dot = numberString.indexOf('.');
            integerPart = numberString.substring(0, dot);
            fractionalPart = numberString.substring(dot + 1);
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

        if (!paddedFractionalPart.isEmpty()) {
            formattedNumber += new String(Character.toChars(decimalFormat.getDecimalSeparator()))
                + paddedFractionalPart;
        }

        formattedNumber = FormatNumberPictureSupport.applyDecimalDigitFamily(formattedNumber, decimalFormat);

        return formattedNumber;
    }

    private static String toCanonicalDecimalString(BigDecimal number) {
        BigDecimal stripped = number.stripTrailingZeros();

        if (stripped.compareTo(BigDecimal.ZERO) == 0) {
            return ".";
        }

        String numberString = stripped.toPlainString();

        if (!numberString.contains(".")) {
            numberString += ".";
        }

        if (numberString.startsWith("0.")) {
            numberString = numberString.substring(1);
        }

        return numberString;
    }

    private static String formatExponent(
            int exponent,
            int minimumExponentSize,
            DecimalFormatDefinition decimalFormat
    ) {
        StringBuilder result = new StringBuilder();

        result.appendCodePoint(decimalFormat.getExponentSeparator());

        if (exponent < 0) {
            result.appendCodePoint(decimalFormat.getMinusSign());
        }

        String digits = Integer.toString(Math.abs(exponent));
        if (digits.length() < minimumExponentSize) {
            digits = "0".repeat(minimumExponentSize - digits.length()) + digits;
        }

        digits = FormatNumberPictureSupport.applyDecimalDigitFamily(digits, decimalFormat);
        result.append(digits);

        return result.toString();
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

    private static AdjustedNumber computeAdjustedNumber(
            ResolvedNumber resolvedNumber,
            FormatNumberSubPicture picture
    ) {
        if (ResolvedNumber.DOUBLE.equals(resolvedNumber.type)) {
            double value = resolvedNumber.doubleValue;
            if (picture.getHasPercent()) {
                value *= 100;
            } else if (picture.getHasPerMille()) {
                value *= 1000;
            }

            if (Double.isInfinite(value)) {
                return new AdjustedNumber(resolvedNumber.type, null, true);
            }

            return new AdjustedNumber(resolvedNumber.type, BigDecimal.valueOf(value), false);
        }

        if (ResolvedNumber.FLOAT.equals(resolvedNumber.type)) {
            float value = resolvedNumber.floatValue;
            if (picture.getHasPercent()) {
                value *= 100;
            } else if (picture.getHasPerMille()) {
                value *= 1000;
            }

            if (Float.isInfinite(value)) {
                return new AdjustedNumber(resolvedNumber.type, null, true);
            }

            return new AdjustedNumber(resolvedNumber.type, BigDecimal.valueOf(floatToBigDecimal(value)), false);
        }

        BigDecimal value = resolvedNumber.decimalValue;
        if (picture.getHasPercent()) {
            value = value.multiply(BigDecimal.valueOf(100));
        } else if (picture.getHasPerMille()) {
            value = value.multiply(BigDecimal.valueOf(1000));
        }

        return new AdjustedNumber(resolvedNumber.type, value, false);
    }

    private static MantissaExponentPair computeMantissaExponent(
            BigDecimal adjustedValue,
            int scalingFactor
    ) {
        if (adjustedValue.compareTo(BigDecimal.ZERO) == 0) {
            return new MantissaExponentPair(BigDecimal.ZERO, 0);
        }

        BigDecimal abs = adjustedValue.abs();
        int exponent = abs.precision() - abs.scale() - scalingFactor;
        BigDecimal mantissa = adjustedValue.movePointLeft(exponent);

        return new MantissaExponentPair(mantissa, exponent);
    }

    private static double floatToBigDecimal(float value) {
        return Double.parseDouble(Float.toString(value));
    }

    private static MantissaExponentPair normalizeMantissaAfterRounding(
            BigDecimal mantissa,
            int exponent,
            int scalingFactor
    ) {
        if (mantissa.compareTo(BigDecimal.ZERO) == 0) {
            return new MantissaExponentPair(BigDecimal.ZERO, 0);
        }

        BigDecimal abs = mantissa.abs();
        BigDecimal upperBound = BigDecimal.ONE.scaleByPowerOfTen(scalingFactor);
        BigDecimal lowerBound = BigDecimal.ONE.scaleByPowerOfTen(scalingFactor - 1);

        while (abs.compareTo(upperBound) > 0) {
            mantissa = mantissa.movePointLeft(1);
            exponent += 1;
            abs = mantissa.abs();
        }

        while (abs.compareTo(lowerBound) < 0) {
            mantissa = mantissa.movePointRight(1);
            exponent -= 1;
            abs = mantissa.abs();
        }

        return new MantissaExponentPair(mantissa, exponent);
    }

    private static final class AdjustedNumber {
        final String type;
        final BigDecimal value;
        final boolean infinite;

        AdjustedNumber(String type, BigDecimal value, boolean infinite) {
            this.type = type;
            this.value = value;
            this.infinite = infinite;
        }
    }

    private static final class MantissaExponentPair {
        final BigDecimal mantissa;
        final int exponent;

        MantissaExponentPair(BigDecimal mantissa, int exponent) {
            this.mantissa = mantissa;
            this.exponent = exponent;
        }
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
