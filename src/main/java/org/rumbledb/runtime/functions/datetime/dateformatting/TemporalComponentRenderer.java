package org.rumbledb.runtime.functions.datetime.dateformatting;

import org.rumbledb.exceptions.ComponentSpecifierNotAvailableException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.runtime.functions.util.formatting.FormattingContext;
import org.rumbledb.runtime.functions.util.formatting.NumericFormattingSupport;
import org.rumbledb.runtime.functions.util.formatting.NumericPicture;
import org.rumbledb.runtime.functions.util.formatting.NumberWords;

import java.math.BigInteger;
import java.time.OffsetDateTime;

final class TemporalComponentRenderer {

    private TemporalComponentRenderer() {
    }

    static String render(
            OffsetDateTime value,
            VariableMarker variableMarker,
            boolean hasExplicitTimezone,
            FormattingContext formattingContext,
            TemporalPictureFormatter.ComponentSupport componentSupport,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        if (!componentSupport.supports(variableMarker.component)) {
            throw new ComponentSpecifierNotAvailableException(
                    "\""
                        + pictureString
                        + "\": a component specifier refers to components that are not available in the date type",
                    metadata
            );
        }

        switch (variableMarker.kind) {
            case VariableMarker.Kind.ROMAN:
                return formatRoman(value, variableMarker, formattingContext, pictureString, metadata);

            case VariableMarker.Kind.ALPHABETIC:
                return formatAlphabetic(value, variableMarker, formattingContext, pictureString, metadata);

            case VariableMarker.Kind.NUMERIC:
                return formatNumericComponent(value, variableMarker, formattingContext, pictureString, metadata);

            case VariableMarker.Kind.FRACTIONAL_SECONDS:
                return FractionalSecondsFormatter.format(
                    value,
                    variableMarker,
                    formattingContext,
                    pictureString,
                    metadata
                );

            case VariableMarker.Kind.NAME:
                return formatNamedComponent(value, variableMarker, formattingContext, pictureString, metadata);

            case VariableMarker.Kind.AM_PM:
                return value.getHour() < 12 ? "am" : "pm";

            case VariableMarker.Kind.WORDS:
                return formatWordsComponent(value, variableMarker, formattingContext, pictureString, metadata);

            case VariableMarker.Kind.TIMEZONE:
                return TemporalFormattingSupport.formatTimezone(
                    value,
                    variableMarker.timezonePicture,
                    hasExplicitTimezone,
                    formattingContext
                );

            case VariableMarker.Kind.DEFAULT:
            default:
                throw unsupported(pictureString, metadata, variableMarker.presentation);
        }
    }

    private static String formatRoman(
            OffsetDateTime dt,
            VariableMarker variableMarker,
            FormattingContext formattingContext,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        int numericValue = variableMarker.numericValue(dt, formattingContext, pictureString, metadata);

        numericValue = applyYearMaximumWidthRule(numericValue, variableMarker);

        String roman = numericValue >= 1 && numericValue <= 3999
            ? NumberWords.roman(numericValue, variableMarker.lowerCaseRoman)
            : Integer.toString(numericValue);
        roman = maybeAppendOrdinal(roman, numericValue, variableMarker, formattingContext);

        return padRightWithSpaces(roman, variableMarker.minWidth);
    }

    private static String formatAlphabetic(
            OffsetDateTime dt,
            VariableMarker variableMarker,
            FormattingContext formattingContext,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        int numericValue = variableMarker.numericValue(dt, formattingContext, pictureString, metadata);

        numericValue = applyYearMaximumWidthRule(numericValue, variableMarker);

        String alpha = NumericFormattingSupport.integerToAlphabetic(numericValue, variableMarker.lowerCaseAlphabetic);
        alpha = maybeAppendOrdinal(alpha, numericValue, variableMarker, formattingContext);

        return padRightWithSpaces(alpha, variableMarker.minWidth);
    }

    private static String formatNamedComponent(
            OffsetDateTime dt,
            VariableMarker variableMarker,
            FormattingContext formattingContext,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        String value = variableMarker.nameValue(dt, formattingContext, pictureString, metadata);

        return TemporalFormattingSupport.applyNameCase(value, variableMarker, formattingContext.locale);
    }

    private static String formatWordsComponent(
            OffsetDateTime dt,
            VariableMarker variableMarker,
            FormattingContext formattingContext,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        int numericValue = variableMarker.numericValue(dt, formattingContext, pictureString, metadata);

        numericValue = applyYearMaximumWidthRule(numericValue, variableMarker);

        String words = variableMarker.isOrdinal()
            ? NumberWords.ordinalWords(numericValue, formattingContext.uLocale, variableMarker.formatSpecifier)
            : NumberWords.cardinal(numericValue, formattingContext.uLocale, variableMarker.formatSpecifier);

        return TemporalFormattingSupport.applyWordCase(words, variableMarker.wordCase, formattingContext.locale);
    }

    private static String maybeAppendOrdinal(
            String base,
            int numericValue,
            VariableMarker variableMarker,
            FormattingContext formattingContext
    ) {
        if (!variableMarker.isOrdinal()) {
            return base;
        }

        return base
            + NumberWords.ordinalSuffix(
                BigInteger.valueOf(numericValue),
                formattingContext.uLocale
            );
    }

    private static String formatNumericComponent(
            OffsetDateTime dt,
            VariableMarker variableMarker,
            FormattingContext formattingContext,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        int value = variableMarker.numericValue(dt, formattingContext, pictureString, metadata);

        NumericPicture pic = variableMarker.numericPicture;

        if (pic == null) {
            int defaultMinWidth = variableMarker.defaultNumericMinWidth();
            String digits = Integer.toString(value);
            int minWidth = Math.max(variableMarker.minWidth, defaultMinWidth);
            digits = leftPad(digits, minWidth);

            if (variableMarker.maxWidth > 0 && digits.length() > variableMarker.maxWidth) {
                digits = digits.substring(digits.length() - variableMarker.maxWidth);
            }

            return maybeAppendOrdinal(digits, value, variableMarker, formattingContext);
        }

        String digits = Integer.toString(value);

        if (
            variableMarker.appliesYearMaximumWidthRule()
                && variableMarker.explicitNumeric
                && variableMarker.maxWidth < 0
                && pic.getActiveDigitCount() > 1
                && digits.length() > pic.getActiveDigitCount()
        ) {
            digits = digits.substring(digits.length() - pic.getActiveDigitCount());
        }

        int minWidth = variableMarker.explicitNumeric
            ? Math.max(variableMarker.minWidth, pic.getMandatoryDigitCount())
            : variableMarker.minWidth;

        digits = leftPad(digits, minWidth);

        if (variableMarker.maxWidth > 0 && digits.length() > variableMarker.maxWidth) {
            digits = digits.substring(digits.length() - variableMarker.maxWidth);
        }

        digits = NumericFormattingSupport.applyGrouping(digits, pic);
        digits = NumericFormattingSupport.mapAsciiDigits(digits, pic.getZeroDigit());

        return maybeAppendOrdinal(digits, value, variableMarker, formattingContext);
    }

    private static int applyYearMaximumWidthRule(int value, VariableMarker variableMarker) {
        if (variableMarker.appliesYearMaximumWidthRule() && variableMarker.maxWidth > 0) {
            String digits = Integer.toString(Math.abs(value));

            if (digits.length() > variableMarker.maxWidth) {
                int reduced = Integer.parseInt(digits.substring(digits.length() - variableMarker.maxWidth));
                return value < 0 ? -reduced : reduced;
            }
        }

        return value;
    }

    private static String leftPad(String value, int width) {
        if (width <= 0 || value.length() >= width) {
            return value;
        }

        return "0".repeat(width - value.length()) + value;
    }

    private static String padRightWithSpaces(String value, int width) {
        if (width <= 0 || value.length() >= width) {
            return value;
        }

        return value + " ".repeat(width - value.length());
    }

    private static UnsupportedFeatureException unsupported(
            String pictureString,
            ExceptionMetadata metadata,
            String modifier
    ) {
        String message = String.format(
            "\"%s\": first presentation modifier not supported: %s",
            pictureString,
            modifier
        );

        return new UnsupportedFeatureException(message, metadata);
    }
}
