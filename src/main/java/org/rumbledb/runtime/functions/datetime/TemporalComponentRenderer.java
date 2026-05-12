package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.exceptions.ComponentSpecifierNotAvailableException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.runtime.functions.base.formatting.NumericFormattingSupport;
import org.rumbledb.runtime.functions.base.formatting.NumericPicture;
import org.rumbledb.runtime.functions.base.formatting.NumericPictureParser;
import org.rumbledb.runtime.functions.base.formatting.language.LanguageRegistry;

import java.time.OffsetDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;

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
                    return formatFractionalSeconds(value, variableMarker, formattingOptions);
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
                    return formatFractionalSeconds(value, variableMarker, formattingOptions);
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
            numericValue = fractionAsInteger(dt);
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
        if (variableMarker.ordinal) {
            roman = roman + NumericFormattingSupport.ordinalSuffix(numericValue, formattingOptions.language);
        }
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
        if (variableMarker.ordinal) {
            alpha = alpha + NumericFormattingSupport.ordinalSuffix(numericValue, formattingOptions.language);
        }
        return padRightWithSpaces(alpha, variableMarker.minWidth);
    }

    private static String formatNamedComponent(
            OffsetDateTime dt,
            ParsedVariableMarker variableMarker,
            FormattingOptions formattingOptions,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        String value;
        switch (variableMarker.component) {
            case 'F':
                value = LanguageRegistry.resolve(formattingOptions.language)
                    .dayName(dt.getDayOfWeek(), variableMarker.minWidth, variableMarker.maxWidth);
                break;

            case 'M':
                value = LanguageRegistry.resolve(formattingOptions.language)
                    .monthName(dt.getMonth(), variableMarker.minWidth, variableMarker.maxWidth);
                break;

            case 'P':
                value = getAmPmName(dt, variableMarker);
                break;

            default:
                throw unsupported(pictureString, metadata, variableMarker.presentation);
        }

        return TemporalFormattingSupport.applyNameCase(value, variableMarker, formattingOptions.locale);
    }

    private static String getAmPmName(
            OffsetDateTime dt,
            ParsedVariableMarker variableMarker
    ) {
        boolean am = dt.getHour() < 12;

        switch (variableMarker.nameForm) {
            case ParsedVariableMarker.NameForm.LOWER:
                return am ? "am" : "pm";

            case ParsedVariableMarker.NameForm.UPPER:
                return am ? "AM" : "PM";

            case ParsedVariableMarker.NameForm.TITLE:
            default:
                return am ? "Am" : "Pm";
        }
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

        String words;
        if (variableMarker.ordinal) {
            words = NumericFormattingSupport.toOrdinal(numericValue, formattingOptions.language);
        } else {
            words = NumericFormattingSupport.toCardinal(numericValue, formattingOptions.language);
        }

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
                return maybeAppendOrdinal(
                    Integer.toString(dt.getYear()),
                    dt.getYear(),
                    variableMarker,
                    formattingOptions
                );

            case 'M':
                return maybeAppendOrdinal(
                    Integer.toString(dt.getMonthValue()),
                    dt.getMonthValue(),
                    variableMarker,
                    formattingOptions
                );

            case 'D':
                return maybeAppendOrdinal(
                    Integer.toString(dt.getDayOfMonth()),
                    dt.getDayOfMonth(),
                    variableMarker,
                    formattingOptions
                );

            case 'd':
                return maybeAppendOrdinal(
                    Integer.toString(dt.getDayOfYear()),
                    dt.getDayOfYear(),
                    variableMarker,
                    formattingOptions
                );

            case 'F':
                return LanguageRegistry.resolve(formattingOptions.language)
                    .dayAbbreviation(dt.getDayOfWeek(), 3);

            case 'W':
                return maybeAppendOrdinal(
                    Integer.toString(dt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)),
                    dt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR),
                    variableMarker,
                    formattingOptions
                );

            case 'w':
                int wom = weekOfMonth(dt, formattingOptions);
                return maybeAppendOrdinal(Integer.toString(wom), wom, variableMarker, formattingOptions);

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

    private static int weekOfMonth(OffsetDateTime dt, FormattingOptions formattingOptions) {
        int wom = dt.get(WeekFields.ISO.weekOfMonth());

        if (
            formattingOptions != null
                && formattingOptions.useFiveArgumentSemantics
                && formattingOptions.calendarMode == CalendarMode.ISO
                && wom == 0
        ) {
            OffsetDateTime previousMonth = dt.minusMonths(1);
            return previousMonth.withDayOfMonth(previousMonth.toLocalDate().lengthOfMonth())
                .get(WeekFields.ISO.weekOfMonth());
        }

        return wom;
    }

    private static String maybeAppendOrdinal(
            String base,
            int numericValue,
            ParsedVariableMarker variableMarker,
            FormattingOptions formattingOptions
    ) {
        if (variableMarker.ordinal) {
            return base + NumericFormattingSupport.ordinalSuffix(numericValue, formattingOptions.language);
        }

        return base;
    }

    private static int getNumericComponentValue(
            OffsetDateTime dt,
            char component,
            FormattingOptions formattingOptions,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        switch (component) {
            case 'Y':
                return dt.getYear();

            case 'M':
                return dt.getMonthValue();

            case 'D':
                return dt.getDayOfMonth();

            case 'd':
                return dt.getDayOfYear();

            case 'W':
                return dt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

            case 'w':
                return weekOfMonth(dt, formattingOptions);

            case 'F':
                return dt.getDayOfWeek().getValue();

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

            if (variableMarker.ordinal) {
                digits = digits + NumericFormattingSupport.ordinalSuffix(value, formattingOptions.language);
            }

            return digits;
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

        if (variableMarker.ordinal) {
            digits = digits + NumericFormattingSupport.ordinalSuffix(value, formattingOptions.language);
        }

        return digits;
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

    private static String formatFractionalSeconds(
            OffsetDateTime dt,
            ParsedVariableMarker variableMarker,
            FormattingOptions formattingOptions
    ) {
        if ("I".equals(variableMarker.presentation) || "i".equals(variableMarker.presentation)) {
            int value = fractionAsInteger(dt);
            String roman = NumericFormattingSupport.integerToRoman(value);
            return "i".equals(variableMarker.presentation) ? roman.toLowerCase(formattingOptions.locale) : roman;
        }

        FractionalPattern pattern = FractionalPattern.parse(variableMarker.presentation);
        String fractionDigits = canonicalFractionDigits(dt);

        int minDigits;
        int maxDigits;

        if (pattern.isDefaultLike()) {
            minDigits = variableMarker.minWidth == -1 ? 0 : variableMarker.minWidth;
            maxDigits = variableMarker.maxWidth > 0 ? variableMarker.maxWidth : Integer.MAX_VALUE;
        } else {
            minDigits = Math.max(pattern.mandatoryDigits, variableMarker.minWidth == -1 ? 0 : variableMarker.minWidth);

            int pictureMaxDigits = pattern.activeDigits;
            int widthMaxDigits = variableMarker.maxWidth > 0 ? variableMarker.maxWidth : -1;

            if (widthMaxDigits > 0) {
                maxDigits = Math.max(pictureMaxDigits, widthMaxDigits);
            } else {
                maxDigits = pictureMaxDigits;
            }
        }

        if (maxDigits < Integer.MAX_VALUE && fractionDigits.length() > maxDigits) {
            fractionDigits = fractionDigits.substring(0, maxDigits);
        }

        fractionDigits = suppressTrailingZeros(fractionDigits, minDigits);

        if (fractionDigits.length() < minDigits) {
            fractionDigits = rightPad(fractionDigits, minDigits);
        }

        if (fractionDigits.isEmpty()) {
            fractionDigits = "0";
        }

        return mapDigitsAndInsertSeparators(
            fractionDigits,
            pattern.zeroDigit,
            pattern
        );
    }

    private static String suppressTrailingZeros(String digits, int minDigits) {
        int end = digits.length();

        while (end > minDigits && end > 0 && digits.charAt(end - 1) == '0') {
            end--;
        }

        return digits.substring(0, end);
    }

    private static String mapDigitsAndInsertSeparators(
            String digits,
            int zeroDigit,
            FractionalPattern pattern
    ) {
        StringBuilder sb = new StringBuilder();
        int usedDigits = Math.min(digits.length(), pattern.activeDigits);

        for (int i = 0; i < usedDigits; i++) {
            char d = digits.charAt(i);
            sb.appendCodePoint(zeroDigit + (d - '0'));

            String sep = pattern.separatorAfterIndex(i);
            if (sep != null) {
                sb.append(sep);
            }
        }

        if (digits.length() > usedDigits) {
            for (int i = usedDigits; i < digits.length(); i++) {
                char d = digits.charAt(i);
                sb.appendCodePoint(zeroDigit + (d - '0'));
            }
        }

        return sb.toString();
    }

    private static int fractionAsInteger(OffsetDateTime dt) {
        return Integer.parseInt(canonicalFractionDigits(dt));
    }

    private static String canonicalFractionDigits(OffsetDateTime dt) {
        int nanos = dt.getNano();

        if (nanos == 0) {
            return "0";
        }

        String digits = String.format("%09d", nanos);
        int end = digits.length();

        while (end > 1 && digits.charAt(end - 1) == '0') {
            end--;
        }

        return digits.substring(0, end);
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

    private static String rightPad(String value, int width) {
        if (value.length() >= width) {
            return value;
        }

        return value + "0".repeat(width - value.length());
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

    private static final class FractionalPattern {
        final String raw;
        final int mandatoryDigits;
        final int activeDigits;
        final int zeroDigit;
        final String[] separatorsAfterSlot;

        private FractionalPattern(
                String raw,
                int mandatoryDigits,
                int activeDigits,
                int zeroDigit,
                String[] separatorsAfterSlot
        ) {
            this.raw = raw;
            this.mandatoryDigits = mandatoryDigits;
            this.activeDigits = activeDigits;
            this.zeroDigit = zeroDigit;
            this.separatorsAfterSlot = separatorsAfterSlot;
        }

        static FractionalPattern parse(String raw) {
            if (raw == null || raw.isEmpty()) {
                return new FractionalPattern("", 1, Integer.MAX_VALUE, '0', new String[0]);
            }

            int[] cps = raw.codePoints().toArray();
            int mandatory = 0;
            int active = 0;
            int zeroDigit = -1;
            boolean sawOptional = false;
            boolean lastWasActive = false;
            String[] tmpSeparators = new String[cps.length];
            StringBuilder pendingSeparator = new StringBuilder();

            for (int cp : cps) {
                if (cp == '#') {
                    if (mandatory == 0) {
                        throw new IllegalArgumentException("Invalid fractional pattern");
                    }

                    sawOptional = true;
                    active++;

                    if (pendingSeparator.length() > 0 && active > 1) {
                        tmpSeparators[active - 2] = pendingSeparator.toString();
                        pendingSeparator.setLength(0);
                    }

                    lastWasActive = true;
                    continue;
                }

                if (Character.getType(cp) == Character.DECIMAL_DIGIT_NUMBER) {
                    int z = NumericPictureParser.zeroDigitOf(cp);

                    if (zeroDigit < 0) {
                        zeroDigit = z;
                    } else if (zeroDigit != z) {
                        throw new IllegalArgumentException("Mixed digit families");
                    }

                    if (sawOptional) {
                        throw new IllegalArgumentException("Mandatory digit after optional digit");
                    }

                    mandatory++;
                    active++;

                    if (pendingSeparator.length() > 0 && active > 1) {
                        tmpSeparators[active - 2] = pendingSeparator.toString();
                        pendingSeparator.setLength(0);
                    }

                    lastWasActive = true;
                    continue;
                }

                if (!lastWasActive) {
                    throw new IllegalArgumentException("Invalid fractional separator placement");
                }

                pendingSeparator.appendCodePoint(cp);
                lastWasActive = false;
            }

            if (mandatory == 0) {
                throw new IllegalArgumentException("No mandatory digit");
            }

            if (pendingSeparator.length() > 0) {
                throw new IllegalArgumentException("Trailing separator");
            }

            if (zeroDigit < 0) {
                zeroDigit = '0';
            }

            String[] separators = new String[Math.max(0, active - 1)];
            System.arraycopy(tmpSeparators, 0, separators, 0, separators.length);

            return new FractionalPattern(raw, mandatory, active, zeroDigit, separators);
        }

        boolean isDefaultLike() {
            if (this.raw == null || this.raw.isEmpty()) {
                return true;
            }

            if (this.separatorsAfterSlot.length != 0) {
                return false;
            }
            return this.activeDigits == 1 && this.mandatoryDigits == 1;
        }

        String separatorAfterIndex(int index) {
            if (index < 0 || index >= this.separatorsAfterSlot.length) {
                return null;
            }
            return this.separatorsAfterSlot[index];
        }
    }
}
