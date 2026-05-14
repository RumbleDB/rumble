package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.exceptions.ComponentSpecifierNotAvailableException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.runtime.functions.util.formatting.NumericFormattingSupport;
import org.rumbledb.runtime.functions.util.formatting.NumericPicture;
import org.rumbledb.runtime.functions.util.formatting.NumericPictureParser;
import org.rumbledb.runtime.functions.util.formatting.calendar.CalendarRegistry;
import org.rumbledb.runtime.functions.util.formatting.calendar.formatter.CalendarFormatter;
import org.rumbledb.runtime.functions.util.formatting.language.LanguageRegistry;
import org.rumbledb.runtime.functions.util.formatting.language.formatter.LanguageFormatter;

import java.math.BigInteger;
import java.time.OffsetDateTime;

final class TemporalComponentRenderer {

    private TemporalComponentRenderer() {
    }

    static String render(
            OffsetDateTime value,
            ParsedVariableMarker variableMarker,
            boolean hasExplicitTimezone,
            FormattingOptions formattingOptions,
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
            case ParsedVariableMarker.Kind.ROMAN:
                return formatRoman(value, variableMarker, formattingOptions, pictureString, metadata);

            case ParsedVariableMarker.Kind.ALPHABETIC:
                return formatAlphabetic(value, variableMarker, formattingOptions, pictureString, metadata);

            case ParsedVariableMarker.Kind.NUMERIC:
                if (variableMarker.component == 'f') {
                    return FractionalSecondsFormatter.format(value, variableMarker, formattingOptions);
                }
                return formatNumericComponent(value, variableMarker, formattingOptions, pictureString, metadata);

            case ParsedVariableMarker.Kind.NAME:
                return formatNamedComponent(value, variableMarker, formattingOptions, pictureString, metadata);

            case ParsedVariableMarker.Kind.WORDS:
                return formatWordsComponent(value, variableMarker, formattingOptions, pictureString, metadata);

            case ParsedVariableMarker.Kind.TIMEZONE:
                return TemporalFormattingSupport.formatTimezone(
                    value,
                    variableMarker.timezonePicture,
                    hasExplicitTimezone,
                    formattingOptions
                );

            case ParsedVariableMarker.Kind.DEFAULT:
            default:
                if (variableMarker.component == 'f') {
                    return FractionalSecondsFormatter.format(value, variableMarker, formattingOptions);
                }
                return formatDefaultComponent(
                    value,
                    variableMarker,
                    hasExplicitTimezone,
                    formattingOptions,
                    pictureString,
                    metadata
                );
        }
    }

    private static String formatRoman(
            OffsetDateTime dt,
            ParsedVariableMarker variableMarker,
            FormattingOptions formattingOptions,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        int numericValue;
        if (variableMarker.component == 'f') {
            numericValue = FractionalSecondsFormatter.fractionAsInteger(dt);
        } else {
            numericValue = getNumericComponentValue(
                dt,
                variableMarker.component,
                formattingOptions,
                pictureString,
                metadata
            );
        }

        numericValue = applyYearMaximumWidthRule(numericValue, variableMarker);

        String roman = NumericFormattingSupport.integerToRoman(numericValue);
        roman = maybeAppendOrdinal(roman, numericValue, variableMarker, formattingOptions);
        roman = variableMarker.lowerCaseRoman ? roman.toLowerCase(formattingOptions.locale) : roman;

        return padRightWithSpaces(roman, variableMarker.minWidth);
    }

    private static String formatAlphabetic(
            OffsetDateTime dt,
            ParsedVariableMarker variableMarker,
            FormattingOptions formattingOptions,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        int numericValue = getNumericComponentValue(
            dt,
            variableMarker.component,
            formattingOptions,
            pictureString,
            metadata
        );

        numericValue = applyYearMaximumWidthRule(numericValue, variableMarker);

        String alpha = NumericFormattingSupport.integerToAlphabetic(numericValue, variableMarker.lowerCaseAlphabetic);
        alpha = maybeAppendOrdinal(alpha, numericValue, variableMarker, formattingOptions);

        return padRightWithSpaces(alpha, variableMarker.minWidth);
    }

    private static String formatNamedComponent(
            OffsetDateTime dt,
            ParsedVariableMarker variableMarker,
            FormattingOptions formattingOptions,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        CalendarFormatter calendar = calendar(formattingOptions);
        LanguageFormatter language = language(formattingOptions);

        String value;
        switch (variableMarker.component) {
            case 'F':
                value = language.dayName(
                    calendar.dayForName(dt),
                    variableMarker.minWidth,
                    variableMarker.maxWidth
                );
                break;

            case 'M':
                value = language.monthName(
                    calendar.monthForName(dt),
                    variableMarker.minWidth,
                    variableMarker.maxWidth
                );
                break;

            case 'P':
                value = language.amPmName(dt.getHour() < 12, amPmNameForm(variableMarker));
                break;

            default:
                throw unsupported(pictureString, metadata, variableMarker.presentation);
        }

        return TemporalFormattingSupport.applyNameCase(value, variableMarker, formattingOptions.locale);
    }

    private static String formatWordsComponent(
            OffsetDateTime dt,
            ParsedVariableMarker variableMarker,
            FormattingOptions formattingOptions,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        int numericValue = getNumericComponentValue(
            dt,
            variableMarker.component,
            formattingOptions,
            pictureString,
            metadata
        );

        numericValue = applyYearMaximumWidthRule(numericValue, variableMarker);

        String words = variableMarker.ordinal
            ? language(formattingOptions).toOrdinal(numericValue)
            : language(formattingOptions).toCardinal(numericValue);

        return TemporalFormattingSupport.applyWordCase(words, variableMarker.wordCase, formattingOptions.locale);
    }

    private static String formatDefaultComponent(
            OffsetDateTime dt,
            ParsedVariableMarker variableMarker,
            boolean hasExplicitTimezone,
            FormattingOptions formattingOptions,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        switch (variableMarker.component) {
            case 'Y':
            case 'M':
            case 'D':
            case 'd':
            case 'W':
            case 'w':
                return formatDefaultNumericCalendarComponent(
                    dt,
                    variableMarker,
                    formattingOptions,
                    pictureString,
                    metadata
                );

            case 'F':
                return language(formattingOptions)
                    .dayAbbreviation(calendar(formattingOptions).dayForName(dt), 3);

            case 'H':
                return Integer.toString(dt.getHour());

            case 'h':
                return Integer.toString(hour12(dt.getHour()));

            case 'm':
                return zeroPad(dt.getMinute(), 2);

            case 's':
                return zeroPad(dt.getSecond(), 2);

            case 'P':
                return dt.getHour() < 12 ? "am" : "pm";

            case 'Z':
                return TemporalFormattingSupport.formatTimezone(
                    dt,
                    ParsedTimezonePicture.defaultNumeric(),
                    hasExplicitTimezone,
                    formattingOptions
                );

            case 'z':
                return TemporalFormattingSupport.formatTimezone(
                    dt,
                    ParsedTimezonePicture.defaultGmt(),
                    hasExplicitTimezone,
                    formattingOptions
                );

            default:
                throw unsupported(pictureString, metadata, variableMarker.presentation);
        }
    }

    private static String formatDefaultNumericCalendarComponent(
            OffsetDateTime dt,
            ParsedVariableMarker variableMarker,
            FormattingOptions formattingOptions,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        int numericValue = getNumericComponentValue(
            dt,
            variableMarker.component,
            formattingOptions,
            pictureString,
            metadata
        );

        return maybeAppendOrdinal(
            Integer.toString(numericValue),
            numericValue,
            variableMarker,
            formattingOptions
        );
    }

    private static String maybeAppendOrdinal(
            String base,
            int numericValue,
            ParsedVariableMarker variableMarker,
            FormattingOptions formattingOptions
    ) {
        if (!variableMarker.ordinal) {
            return base;
        }

        return base + language(formattingOptions).ordinalSuffix(BigInteger.valueOf(numericValue));
    }

    private static int getNumericComponentValue(
            OffsetDateTime dt,
            char component,
            FormattingOptions formattingOptions,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        CalendarFormatter calendar = calendar(formattingOptions);

        switch (component) {
            case 'Y':
                return calendar.year(dt);

            case 'M':
                return calendar.monthInYear(dt);

            case 'D':
                return calendar.dayInMonth(dt);

            case 'd':
                return calendar.dayInYear(dt);

            case 'W':
                return calendar.weekInYear(dt);

            case 'w':
                return calendar.weekInMonth(dt);

            case 'F':
                return calendar.dayOfWeek(dt);

            case 'H':
                return dt.getHour();

            case 'h':
                return hour12(dt.getHour());

            case 'm':
                return dt.getMinute();

            case 's':
                return dt.getSecond();

            case 'P':
                return dt.getHour() < 12 ? 0 : 1;

            default:
                throw unsupported(pictureString, metadata, String.valueOf(component));
        }
    }

    private static String formatNumericComponent(
            OffsetDateTime dt,
            ParsedVariableMarker variableMarker,
            FormattingOptions formattingOptions,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        int value = getNumericComponentValue(
            dt,
            variableMarker.component,
            formattingOptions,
            pictureString,
            metadata
        );

        NumericPicture pic = variableMarker.numericPicture;

        if (pic == null) {
            int defaultMinWidth = defaultNumericMinWidth(variableMarker.component);
            String digits = Integer.toString(value);
            int minWidth = Math.max(variableMarker.minWidth, defaultMinWidth);
            digits = leftPad(digits, minWidth);

            if (variableMarker.maxWidth > 0 && digits.length() > variableMarker.maxWidth) {
                digits = digits.substring(digits.length() - variableMarker.maxWidth);
            }

            return maybeAppendOrdinal(digits, value, variableMarker, formattingOptions);
        }

        String digits = Integer.toString(value);

        if (
            variableMarker.component == 'Y'
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
        digits = NumericPictureParser.mapAsciiDigits(digits, pic.getZeroDigit());

        return maybeAppendOrdinal(digits, value, variableMarker, formattingOptions);
    }

    private static CalendarFormatter calendar(FormattingOptions formattingOptions) {
        return CalendarRegistry.forCalendar(formattingOptions.calendarMode);
    }

    private static LanguageFormatter language(FormattingOptions formattingOptions) {
        return LanguageRegistry.forLanguage(formattingOptions.locale.getLanguage());
    }

    private static String amPmNameForm(ParsedVariableMarker variableMarker) {
        switch (variableMarker.nameForm) {
            case ParsedVariableMarker.NameForm.LOWER:
                return "lower";
            case ParsedVariableMarker.NameForm.UPPER:
                return "upper";
            case ParsedVariableMarker.NameForm.TITLE:
            default:
                return "title";
        }
    }

    private static int defaultNumericMinWidth(char component) {
        switch (component) {
            case 'm':
            case 's':
                return 2;

            default:
                return 1;
        }
    }

    private static int applyYearMaximumWidthRule(int value, ParsedVariableMarker variableMarker) {
        if (variableMarker.component == 'Y' && variableMarker.maxWidth > 0) {
            String digits = Integer.toString(Math.abs(value));

            if (digits.length() > variableMarker.maxWidth) {
                int reduced = Integer.parseInt(digits.substring(digits.length() - variableMarker.maxWidth));
                return value < 0 ? -reduced : reduced;
            }
        }

        return value;
    }

    private static int hour12(int hour24) {
        int mod = hour24 % 12;
        return mod == 0 ? 12 : mod;
    }

    private static String zeroPad(int value, int width) {
        return leftPad(Integer.toString(value), width);
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
