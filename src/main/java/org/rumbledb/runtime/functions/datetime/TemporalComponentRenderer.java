package org.rumbledb.runtime.functions.datetime;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;

import org.rumbledb.exceptions.ComponentSpecifierNotAvailableException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.runtime.functions.base.formatting.IntegerFormattingSupport;
import org.rumbledb.runtime.functions.base.formatting.NumericPicture;
import org.rumbledb.runtime.functions.base.formatting.NumericPictureParser;

final class TemporalComponentRenderer {

    private TemporalComponentRenderer() {
    }

    static String render(
            OffsetDateTime value,
            ParsedVariableMarker parsed,
            boolean hasExplicitTimezone,
            FormattingOptions formattingOptions,
            TemporalPictureFormatter.ComponentSupport componentSupport,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        if (!componentSupport.supports(parsed.component)) {
            throw new ComponentSpecifierNotAvailableException(
                    "\""
                        + pictureStringForErrors
                        + "\": a component specifier refers to components that are not available in the date type",
                    metadata
            );
        }

        switch (parsed.kind) {
            case ROMAN:
                return formatRoman(value, parsed, formattingOptions, pictureStringForErrors, metadata);
            case ALPHABETIC:
                return formatAlphabetic(value, parsed, formattingOptions, pictureStringForErrors, metadata);
            case NUMERIC:
                if (parsed.component == 'f') {
                    return formatFractionalSeconds(value, parsed, formattingOptions, pictureStringForErrors, metadata);
                }
                return formatNumericComponent(value, parsed, formattingOptions, pictureStringForErrors, metadata);
            case NAME:
                return formatNamedComponent(value, parsed, formattingOptions, pictureStringForErrors, metadata);
            case WORDS:
                return formatWordsComponent(value, parsed, formattingOptions, pictureStringForErrors, metadata);
            case TIMEZONE:
                return TemporalFormattingSupport.formatTimezone(
                    value.getOffset(),
                    parsed.timezonePicture,
                    hasExplicitTimezone
                );
            case DEFAULT:
            default:
                if (parsed.component == 'f') {
                    return formatFractionalSeconds(value, parsed, formattingOptions, pictureStringForErrors, metadata);
                }
                return formatDefaultComponent(
                    value,
                    parsed,
                    hasExplicitTimezone,
                    formattingOptions,
                    pictureStringForErrors,
                    metadata
                );
        }
    }

    private static String formatRoman(
            OffsetDateTime dt,
            ParsedVariableMarker parsed,
            FormattingOptions formattingOptions,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        int numericValue;
        if (parsed.component == 'f') {
            numericValue = fractionAsInteger(dt);
        } else {
            numericValue = getNumericComponentValue(
                dt,
                parsed.component,
                formattingOptions,
                pictureStringForErrors,
                metadata
            );
        }

        numericValue = adjustValueForWidthBeforePresentation(numericValue, parsed);

        String roman = IntegerFormattingSupport.integerToRoman(numericValue);
        if (parsed.ordinal) {
            roman = roman + IntegerFormattingSupport.ordinalSuffix(numericValue, formattingOptions.locale);
        }
        roman = parsed.lowerCaseRoman ? roman.toLowerCase(formattingOptions.locale) : roman;
        return padRightWithSpaces(roman, parsed.minWidth);
    }

    private static String formatAlphabetic(
            OffsetDateTime dt,
            ParsedVariableMarker parsed,
            FormattingOptions formattingOptions,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        int numericValue = getNumericComponentValue(
            dt,
            parsed.component,
            formattingOptions,
            pictureStringForErrors,
            metadata
        );

        numericValue = adjustValueForWidthBeforePresentation(numericValue, parsed);

        String alpha = IntegerFormattingSupport.integerToAlphabetic(numericValue, parsed.lowerCaseAlphabetic);
        if (parsed.ordinal) {
            alpha = alpha + IntegerFormattingSupport.ordinalSuffix(numericValue, formattingOptions.locale);
        }
        return padRightWithSpaces(alpha, parsed.minWidth);
    }

    private static String formatNamedComponent(
            OffsetDateTime dt,
            ParsedVariableMarker parsed,
            FormattingOptions formattingOptions,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        String value;
        switch (parsed.component) {
            case 'F':
                value = TemporalFormattingSupport.getDayName(dt.getDayOfWeek(), parsed, formattingOptions);
                break;
            case 'M':
                value = TemporalFormattingSupport.getMonthName(dt.getMonth(), parsed, formattingOptions);
                break;
            case 'P':
                value = getAmPmName(dt, parsed, formattingOptions);
                break;
            default:
                throw unsupported(pictureStringForErrors, metadata, parsed.presentation);
        }

        return TemporalFormattingSupport.applyNameCase(value, parsed, formattingOptions.locale);
    }

    private static String getAmPmName(
            OffsetDateTime dt,
            ParsedVariableMarker parsed,
            FormattingOptions formattingOptions
    ) {
        boolean am = dt.getHour() < 12;
        switch (parsed.nameForm) {
            case LOWER:
                return am ? "am" : "pm";
            case UPPER:
                return am ? "AM" : "PM";
            case TITLE:
            default:
                return am ? "Am" : "Pm";
        }
    }

    private static String formatWordsComponent(
            OffsetDateTime dt,
            ParsedVariableMarker parsed,
            FormattingOptions formattingOptions,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        int numericValue = getNumericComponentValue(
            dt,
            parsed.component,
            formattingOptions,
            pictureStringForErrors,
            metadata
        );

        numericValue = adjustValueForWidthBeforePresentation(numericValue, parsed);

        String words;
        if (parsed.ordinal) {
            words = IntegerFormattingSupport.toEnglishOrdinalWords(numericValue);
        } else {
            words = IntegerFormattingSupport.toEnglishCardinalWords(numericValue);
        }

        return TemporalFormattingSupport.applyWordCase(words, parsed.wordCase, formattingOptions.locale);
    }

    private static String formatDefaultComponent(
            OffsetDateTime dt,
            ParsedVariableMarker parsed,
            boolean hasExplicitTimezone,
            FormattingOptions formattingOptions,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        switch (parsed.component) {
            case 'Y':
                return maybeAppendOrdinal(Integer.toString(dt.getYear()), dt.getYear(), parsed, formattingOptions);
            case 'M':
                return maybeAppendOrdinal(
                    Integer.toString(dt.getMonthValue()),
                    dt.getMonthValue(),
                    parsed,
                    formattingOptions
                );
            case 'D':
                return maybeAppendOrdinal(
                    Integer.toString(dt.getDayOfMonth()),
                    dt.getDayOfMonth(),
                    parsed,
                    formattingOptions
                );
            case 'd':
                return maybeAppendOrdinal(
                    Integer.toString(dt.getDayOfYear()),
                    dt.getDayOfYear(),
                    parsed,
                    formattingOptions
                );
            case 'F':
                return dt.getDayOfWeek().getDisplayName(TextStyle.FULL, formattingOptions.locale);
            case 'W':
                return maybeAppendOrdinal(
                    Integer.toString(dt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)),
                    dt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR),
                    parsed,
                    formattingOptions
                );
            case 'w':
                int wom = weekOfMonth(dt, formattingOptions);
                return maybeAppendOrdinal(Integer.toString(wom), wom, parsed, formattingOptions);
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
                    dt.getOffset(),
                    ParsedTimezonePicture.defaultNumeric(),
                    hasExplicitTimezone
                );
            case 'z':
                return TemporalFormattingSupport.formatTimezone(
                    dt.getOffset(),
                    ParsedTimezonePicture.defaultGmt(),
                    hasExplicitTimezone
                );
            default:
                throw unsupported(pictureStringForErrors, metadata, parsed.presentation);
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
            ParsedVariableMarker parsed,
            FormattingOptions formattingOptions
    ) {
        if (parsed.ordinal) {
            return base + IntegerFormattingSupport.ordinalSuffix(numericValue, formattingOptions.locale);
        }
        return base;
    }

    private static int getNumericComponentValue(
            OffsetDateTime dt,
            char component,
            FormattingOptions formattingOptions,
            String pictureStringForErrors,
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
                throw unsupported(pictureStringForErrors, metadata, String.valueOf(component));
        }
    }

    private static String formatNumericComponent(
            OffsetDateTime dt,
            ParsedVariableMarker parsed,
            FormattingOptions formattingOptions,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        int value = getNumericComponentValue(dt, parsed.component, formattingOptions, pictureStringForErrors, metadata);

        NumericPicture pic = parsed.numericPicture;
        if (pic == null) {
            int defaultMinWidth = defaultNumericMinWidth(parsed.component);
            String digits = Integer.toString(value);
            int minWidth = Math.max(parsed.minWidth, defaultMinWidth);
            digits = leftPad(digits, minWidth);

            if (parsed.maxWidth > 0 && digits.length() > parsed.maxWidth) {
                digits = digits.substring(digits.length() - parsed.maxWidth);
            }

            if (parsed.ordinal) {
                digits = digits + IntegerFormattingSupport.ordinalSuffix(value, formattingOptions.locale);
            }
            return digits;
        }

        String digits = Integer.toString(value);

        if (
            parsed.component == 'Y'
                && parsed.explicitNumeric
                && parsed.maxWidth < 0
                && pic.getActiveDigitCount() > 1
                && digits.length() > pic.getActiveDigitCount()
        ) {
            digits = digits.substring(digits.length() - pic.getActiveDigitCount());
        }

        int minWidth = parsed.explicitNumeric
            ? Math.max(parsed.minWidth, pic.getMandatoryDigitCount())
            : parsed.minWidth;

        digits = leftPad(digits, minWidth);

        if (parsed.maxWidth > 0 && digits.length() > parsed.maxWidth) {
            digits = digits.substring(digits.length() - parsed.maxWidth);
        }

        digits = IntegerFormattingSupport.applyGrouping(digits, pic);
        digits = NumericPictureParser.mapAsciiDigits(digits, pic.getZeroDigit());

        if (parsed.ordinal) {
            digits = digits + IntegerFormattingSupport.ordinalSuffix(value, formattingOptions.locale);
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
            ParsedVariableMarker parsed,
            FormattingOptions formattingOptions,
            String pictureStringForErrors,
            ExceptionMetadata metadata
    ) {
        if ("I".equals(parsed.presentation) || "i".equals(parsed.presentation)) {
            int value = fractionAsInteger(dt);
            String roman = IntegerFormattingSupport.integerToRoman(value);
            return "i".equals(parsed.presentation) ? roman.toLowerCase(formattingOptions.locale) : roman;
        }

        FractionalPattern pattern = FractionalPattern.parse(parsed.presentation);
        String fractionDigits = canonicalFractionDigits(dt);

        int minDigits;
        int maxDigits;

        if (pattern.isDefaultLike()) {
            minDigits = parsed.minWidth == -1 ? 0 : parsed.minWidth;
            maxDigits = parsed.maxWidth > 0 ? parsed.maxWidth : Integer.MAX_VALUE;
        } else {
            minDigits = Math.max(pattern.mandatoryDigits, parsed.minWidth == -1 ? 0 : parsed.minWidth);

            int pictureMaxDigits = pattern.activeDigits;
            int widthMaxDigits = parsed.maxWidth > 0 ? parsed.maxWidth : -1;

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

        String mapped = mapDigitsAndInsertSeparators(
            fractionDigits,
            pattern.zeroDigit,
            pattern
        );

        return mapped;
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

    private static int adjustValueForWidthBeforePresentation(int value, ParsedVariableMarker parsed) {
        if (parsed.component == 'Y' && parsed.maxWidth > 0) {
            String digits = Integer.toString(Math.abs(value));
            if (digits.length() > parsed.maxWidth) {
                int reduced = Integer.parseInt(digits.substring(digits.length() - parsed.maxWidth));
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
            String pictureStringForErrors,
            ExceptionMetadata metadata,
            String modifier
    ) {
        String message = String.format(
            "\"%s\": first presentation modifier not supported: %s",
            pictureStringForErrors,
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
            if (!pendingSeparator.isEmpty()) {
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
