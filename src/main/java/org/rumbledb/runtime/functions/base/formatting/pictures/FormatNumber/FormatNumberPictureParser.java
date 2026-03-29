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
            if (FormatNumberSubPictureSupport.isPatternSeparator(cp, decimalFormat)) {
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
        int leftmostActiveCharIndex = FormatNumberSubPictureSupport.findLeftmostActiveCharIndex(
            subPictureString,
            decimalFormat
        );
        int rightmostActiveCharIndex = FormatNumberSubPictureSupport.findRightmostActiveCharIndex(
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

        // Find the decimal separator inside the active region only.
        int decimalSeparatorIndex = FormatNumberSubPictureSupport.findDecimalSeparatorIndex(
            activeRegion,
            decimalFormat
        );

        // Rule: A sub-picture must not contain more than one decimal-separator-sign.
        if (decimalSeparatorIndex == -2) {
            throw invalidPicture(pictureString, metadata, "decimal separator must not occur more than once");
        }

        // Split the active region into integer part and fractional part.
        // If no decimal separator exists, the entire active region is the integer part.
        String integerPart;
        String fractionalPart = "";

        if (decimalSeparatorIndex == -1) {
            integerPart = activeRegion;
        } else {
            integerPart = activeRegion.substring(0, decimalSeparatorIndex);

            int decimalSeparatorLength = Character.charCount(decimalFormat.getDecimalSeparator());
            fractionalPart = activeRegion.substring(decimalSeparatorIndex + decimalSeparatorLength);
        }

        // Rule: A sub-picture must contain at least one optional-digit-sign
        // or at least one member of the decimal-digit-family.
        if (
            FormatNumberSubPictureSupport.countActiveDigitSigns(integerPart, decimalFormat)
                + FormatNumberSubPictureSupport.countActiveDigitSigns(fractionalPart, decimalFormat) == 0
        ) {
            throw invalidPicture(pictureString, metadata, "subpicture must contain at least one digit sign");
        }

        // Rule: A sub-picture must not contain a grouping-separator-sign
        // adjacent to a decimal-separator-sign.
        if (FormatNumberSubPictureSupport.hasAdjacentGroupingAndDecimalSeparator(activeRegion, decimalFormat)) {
            throw invalidPicture(
                pictureString,
                metadata,
                "grouping separator must not be adjacent to decimal separator"
            );
        }

        // Rule: The integer part must not contain a mandatory digit
        // followed by an optional digit.
        if (FormatNumberSubPictureSupport.hasMandatoryDigitFollowedByOptionalDigit(integerPart, decimalFormat)) {
            throw invalidPicture(
                pictureString,
                metadata,
                "integer part must not contain a mandatory digit followed by an optional digit"
            );
        }

        // Rule: The fractional part must not contain an optional digit
        // followed by a mandatory digit.
        if (FormatNumberSubPictureSupport.hasOptionalDigitFollowedByMandatoryDigit(fractionalPart, decimalFormat)) {
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
        if (FormatNumberSubPictureSupport.hasPassiveCharacterWithinActiveRegion(activeRegion, decimalFormat)) {
            throw invalidPicture(
                pictureString,
                metadata,
                "subpicture must not contain a passive character between active characters"
            );
        }

        // Rule: A sub-picture must not contain more than one percent-sign
        // or more than one per-mille-sign.
        int percentOccurrences = FormatNumberSubPictureSupport.countOccurrences(
            subPictureString,
            decimalFormat.getPercent()
        );
        int perMilleOccurrences = FormatNumberSubPictureSupport.countOccurrences(
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

        // Analysis phase:
        // integer-part-grouping-positions
        List<GroupingPos> integerPartGroupingPositions = FormatNumberSubPictureSupport.findIntegerGroupingPositions(
            integerPart,
            decimalFormat
        );

        Integer repeatingIntegerGroupingInterval =
            FormatNumberSubPictureSupport.findRepeatingIntegerGroupingInterval(integerPartGroupingPositions);

        // Analysis phase:
        // fractional-part-grouping-positions
        List<GroupingPos> fractionalPartGroupingPositions = FormatNumberSubPictureSupport
            .findFractionalGroupingPositions(
                fractionalPart,
                decimalFormat
            );

        // Analysis phase:
        // minimum-integer-part-size = number of mandatory digits in the integer part
        int minimumIntegerPartSize = FormatNumberSubPictureSupport.countMandatoryDigits(integerPart, decimalFormat);

        // Special rule:
        // If there is no mandatory digit in the integer part and no decimal separator,
        // minimum-integer-part-size is 1.
        if (minimumIntegerPartSize == 0 && decimalSeparatorIndex == -1) {
            minimumIntegerPartSize = 1;
        }

        // Analysis phase:
        // minimum-fractional-part-size = number of mandatory digits in the fractional part
        int minimumFractionalPartSize = FormatNumberSubPictureSupport.countMandatoryDigits(
            fractionalPart,
            decimalFormat
        );

        // Analysis phase:
        // maximum-fractional-part-size = total number of digit signs in the fractional part
        int maximumFractionalPartSize = FormatNumberSubPictureSupport.countActiveDigitSigns(
            fractionalPart,
            decimalFormat
        );

        return new FormatNumberSubPicture(
                prefix,
                suffix,
                integerPart,
                fractionalPart,
                hasPercent,
                hasPerMille,
                integerPartGroupingPositions,
                repeatingIntegerGroupingInterval,
                fractionalPartGroupingPositions,
                minimumIntegerPartSize,
                minimumFractionalPartSize,
                maximumFractionalPartSize
        );
    }

    private static FormatNumberSubPicture deriveNegativeSubpictureFromPositive(
            FormatNumberSubPicture positiveSubpicture,
            DecimalFormatDefinition decimalFormat
    ) {
        String negativePrefix =
            new String(Character.toChars(decimalFormat.getMinusSign())) + positiveSubpicture.prefix();

        return new FormatNumberSubPicture(
                negativePrefix,
                positiveSubpicture.suffix(),
                positiveSubpicture.integerPart(),
                positiveSubpicture.fractionalPart(),
                positiveSubpicture.hasPercent(),
                positiveSubpicture.hasPerMille(),
                positiveSubpicture.integerPartGroupingPositions(),
                positiveSubpicture.repeatingIntegerGroupingInterval(),
                positiveSubpicture.fractionalPartGroupingPositions(),
                positiveSubpicture.minimumIntegerPartSize(),
                positiveSubpicture.minimumFractionalPartSize(),
                positiveSubpicture.maximumFractionalPartSize()
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
