package org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber;

import org.rumbledb.context.DecimalFormatDefinition;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatNumberException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.functions.base.formatting.GroupingPos;

import java.util.List;

public class FormatNumberPictureParser {
    private FormatNumberPictureParser() {
    }

    public static FormatNumberPicture parse(
            String pictureString,
            DecimalFormatDefinition decimalFormat,
            ExceptionMetadata metadata
    ) {
        if (pictureString == null || pictureString.isEmpty()) {
            throw invalidPicture(pictureString, metadata, "must not be empty");
        }

        int separatorPos = -1;
        for (int i = 0; i < pictureString.length();) {
            int cp = pictureString.codePointAt(i);
            if (FormatNumberPictureSupport.isPatternSeparator(cp, decimalFormat)) {
                if (separatorPos != -1) {
                    throw invalidPicture(pictureString, metadata, "must not have more than one separator");
                }
                separatorPos = i;
            }
            i += Character.charCount(cp);
        }

        if (separatorPos == -1) {
            FormatNumberSubPicture positiveSubpicture = parseSubpicture(
                pictureString,
                pictureString,
                decimalFormat,
                metadata
            );
            FormatNumberSubPicture negativeSubpicture =
                deriveNegativeSubpictureFromPositive(positiveSubpicture, decimalFormat);

            return new FormatNumberPicture(
                    positiveSubpicture,
                    negativeSubpicture
            );
        }

        int separatorLength = Character.charCount(decimalFormat.getPatternSeparator());

        String positive = pictureString.substring(0, separatorPos);
        String negative = pictureString.substring(separatorPos + separatorLength);

        return new FormatNumberPicture(
                parseSubpicture(positive, pictureString, decimalFormat, metadata),
                parseSubpicture(negative, pictureString, decimalFormat, metadata)
        );
    }

    private static FormatNumberSubPicture parseSubpicture(
            String subPictureString,
            String pictureString,
            DecimalFormatDefinition decimalFormat,
            ExceptionMetadata metadata
    ) {
        // Rule: A sub-picture must exist and must not be empty.
        if (subPictureString == null || subPictureString.isEmpty()) {
            throw invalidPicture(pictureString, metadata, "subpicture must not be empty");
        }

        // Determine the active region:
        // prefix = passive characters before the first active character
        // suffix = passive characters after the last active character
        int leftmostActiveCharIndex = FormatNumberPictureSupport.findLeftmostActiveCharIndex(
            subPictureString,
            decimalFormat
        );
        int rightmostActiveCharIndex = FormatNumberPictureSupport.findRightmostActiveCharIndex(
            subPictureString,
            decimalFormat
        );

        // Rule: A sub-picture must contain at least one active character.
        // This also protects the substring/codePoint operations below.
        if (leftmostActiveCharIndex == -1 || rightmostActiveCharIndex == -1) {
            throw invalidPicture(pictureString, metadata, "subpicture must contain at least one active character");
        }

        String prefix = subPictureString.substring(0, leftmostActiveCharIndex);

        int rightmostActiveCodePoint = subPictureString.codePointAt(rightmostActiveCharIndex);
        int suffixStart = rightmostActiveCharIndex + Character.charCount(rightmostActiveCodePoint);
        String suffix = subPictureString.substring(suffixStart);

        // From here on, work only on the active region.
        // This excludes prefix/suffix passive characters from the structural analysis.
        String activeRegion = subPictureString.substring(leftmostActiveCharIndex, suffixStart);

        int exponentSeparatorIndex = FormatNumberPictureSupport.findExponentSeparatorIndex(activeRegion, decimalFormat);

        // Rule: A sub-picture must not contain more than one exponent-separator-sign.
        if (exponentSeparatorIndex == -2) {
            throw invalidPicture(pictureString, metadata, "exponent separator must not occur more than once");
        }

        // Split the active region into mantissa and exponent part.
        // If no exponent separator exists, the whole active region is the mantissa.
        String mantissaPart;
        String exponentPart = "";

        if (exponentSeparatorIndex == -1) {
            mantissaPart = activeRegion;
        } else {
            int exponentSeparatorLength = Character.charCount(decimalFormat.getExponentSeparator());
            mantissaPart = activeRegion.substring(0, exponentSeparatorIndex);
            exponentPart = activeRegion.substring(exponentSeparatorIndex + exponentSeparatorLength);
        }

        boolean hasExponent = !exponentPart.isEmpty();

        if (
            hasExponent
                && !FormatNumberPictureSupport.containsOnlyMandatoryDigits(exponentPart, decimalFormat)
        ) {
            throw invalidPicture(
                pictureString,
                metadata,
                "exponent part must contain only decimal digit family characters"
            );
        }

        // Find the decimal separator in the mantissa only.
        int decimalSeparatorIndex = FormatNumberPictureSupport.findDecimalSeparatorIndex(
            mantissaPart,
            decimalFormat
        );

        // Rule: A sub-picture must not contain more than one decimal-separator-sign.
        if (decimalSeparatorIndex == -2) {
            throw invalidPicture(pictureString, metadata, "decimal separator must not occur more than once");
        }

        // Split the mantissa into integer part and fractional part.
        // If no decimal separator exists, the entire mantissa is the integer part.
        String integerPart;
        String fractionalPart = "";

        if (decimalSeparatorIndex == -1) {
            integerPart = mantissaPart;
        } else {
            integerPart = mantissaPart.substring(0, decimalSeparatorIndex);

            int decimalSeparatorLength = Character.charCount(decimalFormat.getDecimalSeparator());
            fractionalPart = mantissaPart.substring(decimalSeparatorIndex + decimalSeparatorLength);
        }

        // Rule: A sub-picture must contain at least one optional-digit-sign
        // or at least one member of the decimal-digit-family.
        if (
            FormatNumberPictureSupport.countActiveDigitSigns(integerPart, decimalFormat)
                + FormatNumberPictureSupport.countActiveDigitSigns(fractionalPart, decimalFormat) == 0
        ) {
            throw invalidPicture(pictureString, metadata, "subpicture must contain at least one digit sign");
        }

        // Rule: A sub-picture must not contain a grouping-separator-sign
        // adjacent to a decimal-separator-sign.
        if (FormatNumberPictureSupport.hasAdjacentGroupingAndDecimalSeparator(activeRegion, decimalFormat)) {
            throw invalidPicture(
                pictureString,
                metadata,
                "grouping separator must not be adjacent to decimal separator"
            );
        }

        // Rule: A sub-picture must not contain adjacent grouping-separator-signs
        if (FormatNumberPictureSupport.hasAdjacentGroupingSeparators(activeRegion, decimalFormat)) {
            throw invalidPicture(
                pictureString,
                metadata,
                "subpicture must not contain adjacent grouping separators"
            );
        }

        // Rule: The integer part must not contain a mandatory digit
        // followed by an optional digit.
        if (FormatNumberPictureSupport.hasMandatoryDigitFollowedByOptionalDigit(integerPart, decimalFormat)) {
            throw invalidPicture(
                pictureString,
                metadata,
                "integer part must not contain a mandatory digit followed by an optional digit"
            );
        }

        // Rule: The fractional part must not contain an optional digit
        // followed by a mandatory digit.
        if (FormatNumberPictureSupport.hasOptionalDigitFollowedByMandatoryDigit(fractionalPart, decimalFormat)) {
            throw invalidPicture(
                pictureString,
                metadata,
                "fractional part must not contain an optional digit followed by a mandatory digit"
            );
        }

        // Rule: A sub-picture must not contain a passive character that is
        // preceded by an active character and followed by another active character.
        // Since prefix and suffix were removed, this becomes:
        // the active region itself must contain only active characters.
        if (FormatNumberPictureSupport.hasPassiveCharacterWithinActiveRegion(activeRegion, decimalFormat)) {
            throw invalidPicture(
                pictureString,
                metadata,
                "subpicture must not contain a passive character between active characters"
            );
        }

        // Rule: A sub-picture must not contain more than one percent-sign
        // or more than one per-mille-sign.
        int percentOccurrences = FormatNumberPictureSupport.countOccurrences(
            subPictureString,
            decimalFormat.getPercent()
        );
        int perMilleOccurrences = FormatNumberPictureSupport.countOccurrences(
            subPictureString,
            decimalFormat.getPerMille()
        );

        if (percentOccurrences > 1 || perMilleOccurrences > 1) {
            throw invalidPicture(
                pictureString,
                metadata,
                "must not have more than one percent sign or more than one per-mille sign"
            );
        }

        // Rule: A sub-picture must not contain both a percent-sign and a per-mille-sign.
        boolean hasPercent = percentOccurrences > 0;
        boolean hasPerMille = perMilleOccurrences > 0;

        if (hasPercent && hasPerMille) {
            throw invalidPicture(
                pictureString,
                metadata,
                "must not contain both percent sign and per-mille sign"
            );
        }

        // Rule: A sub-picture that contains a percent-sign or per-mille-sign
        // must not contain a character treated as an exponent-separator-sign.
        if (hasExponent && (hasPercent || hasPerMille)) {
            throw invalidPicture(
                pictureString,
                metadata,
                "subpicture must not contain a percent sign or per-mille sign together with an exponent separator"
            );
        }

        // Analysis phase:
        // integer-part-grouping-positions
        List<GroupingPos> integerPartGroupingPositions = FormatNumberPictureSupport.findIntegerGroupingPositions(
            integerPart,
            decimalFormat
        );

        Integer repeatingIntegerGroupingInterval = FormatNumberPictureSupport.findRepeatingIntegerGroupingInterval(
            integerPart,
            integerPartGroupingPositions,
            decimalFormat
        );

        // Analysis phase:
        // fractional-part-grouping-positions
        List<GroupingPos> fractionalPartGroupingPositions = FormatNumberPictureSupport
            .findFractionalGroupingPositions(
                fractionalPart,
                decimalFormat
            );

        // Rule: A sub-picture must not contain grouping separators adjacent to the decimal separator
        if (FormatNumberPictureSupport.hasInvalidIntegerGroupingPosition(integerPartGroupingPositions)) {
            throw invalidPicture(
                pictureString,
                metadata,
                "grouping separator must not appear at the end of the integer part"
            );
        }

        if (
            FormatNumberPictureSupport.hasGroupingSeparatorAtEndOfFractionalPart(
                fractionalPart,
                fractionalPartGroupingPositions,
                decimalFormat
            )
        ) {
            throw invalidPicture(
                pictureString,
                metadata,
                "grouping separator must not appear at the end of the fractional part"
            );
        }

        int minimumIntegerPartSize =
            FormatNumberPictureSupport.countMandatoryDigits(integerPart, decimalFormat);

        int scalingFactor =
            FormatNumberPictureSupport.countMandatoryDigits(integerPart, decimalFormat);

        int minimumFractionalPartSize =
            FormatNumberPictureSupport.countMandatoryDigits(fractionalPart, decimalFormat);

        int maximumFractionalPartSize =
            FormatNumberPictureSupport.countActiveDigitSigns(fractionalPart, decimalFormat);

        int minimumExponentSize =
            exponentPart.isEmpty()
                ? 0
                : FormatNumberPictureSupport.countMandatoryDigits(exponentPart, decimalFormat);

        // If minimum-integer-part-size and maximum-fractional-part-size are both zero,
        // then adjust depending on whether an exponent separator is present.
        if (minimumIntegerPartSize == 0 && maximumFractionalPartSize == 0) {
            if (hasExponent) {
                minimumFractionalPartSize = 1;
                maximumFractionalPartSize = 1;
            } else {
                minimumIntegerPartSize = 1;
            }
        }

        // If an exponent separator is present, minimum-integer-part-size is zero,
        // and the integer part contains at least one optional digit,
        // then minimum-integer-part-size becomes 1.
        if (
            hasExponent
                && minimumIntegerPartSize == 0
                && FormatNumberPictureSupport.containsOptionalDigit(integerPart, decimalFormat)
        ) {
            minimumIntegerPartSize = 1;
        }


        // After the above adjustments, if both minimum sizes are zero,
        // then minimum-fractional-part-size becomes 1.
        if (minimumIntegerPartSize == 0 && minimumFractionalPartSize == 0) {
            minimumFractionalPartSize = 1;
        }

        return new FormatNumberSubPicture(
                subPictureString,
                prefix,
                suffix,
                integerPart,
                fractionalPart,
                exponentPart,
                hasExponent,
                hasPercent,
                hasPerMille,
                integerPartGroupingPositions,
                repeatingIntegerGroupingInterval,
                fractionalPartGroupingPositions,
                minimumIntegerPartSize,
                minimumFractionalPartSize,
                maximumFractionalPartSize,
                minimumExponentSize,
                scalingFactor
        );
    }

    private static FormatNumberSubPicture deriveNegativeSubpictureFromPositive(
            FormatNumberSubPicture positiveSubpicture,
            DecimalFormatDefinition decimalFormat
    ) {
        String negativePrefix =
            new String(Character.toChars(decimalFormat.getMinusSign())) + positiveSubpicture.getPrefix();

        return new FormatNumberSubPicture(
                positiveSubpicture.getRawPictureString(),
                negativePrefix,
                positiveSubpicture.getSuffix(),
                positiveSubpicture.getIntegerPart(),
                positiveSubpicture.getFractionalPart(),
                positiveSubpicture.getExponentPart(),
                positiveSubpicture.hasExponent(),
                positiveSubpicture.getHasPercent(),
                positiveSubpicture.getHasPerMille(),
                positiveSubpicture.getIntegerPartGroupingPositions(),
                positiveSubpicture.getRepeatingIntegerGroupingInterval(),
                positiveSubpicture.getFractionalPartGroupingPositions(),
                positiveSubpicture.getMinimumIntegerPartSize(),
                positiveSubpicture.getMinimumFractionalPartSize(),
                positiveSubpicture.getMaximumFractionalPartSize(),
                positiveSubpicture.getMinimumExponentPartSize(),
                positiveSubpicture.getScalingFactor()
        );
    }

    private static RumbleException invalidPicture(
            String pictureString,
            ExceptionMetadata metadata,
            String message
    ) {
        return new IncorrectSyntaxFormatNumberException(
                "\"" + pictureString + "\": invalid picture string; " + message,
                metadata
        );
    }
}
