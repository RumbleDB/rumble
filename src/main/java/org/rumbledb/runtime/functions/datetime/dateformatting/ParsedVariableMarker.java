package org.rumbledb.runtime.functions.datetime.dateformatting;

import com.ibm.icu.util.Calendar;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.runtime.functions.util.formatting.FormattingContext;
import org.rumbledb.runtime.functions.util.formatting.NumericPicture;
import org.rumbledb.runtime.functions.util.formatting.calendar.CalendarFields;
import org.rumbledb.runtime.functions.util.formatting.calendar.DateNames;

import java.time.OffsetDateTime;

final class ParsedVariableMarker {

    public static final class Kind {
        private Kind() {
        }

        public static final String DEFAULT = "DEFAULT";
        public static final String NUMERIC = "NUMERIC";
        public static final String ROMAN = "ROMAN";
        public static final String ALPHABETIC = "ALPHABETIC";
        public static final String NAME = "NAME";
        public static final String WORDS = "WORDS";
        public static final String AM_PM = "AM_PM";
        public static final String FRACTIONAL_SECONDS = "FRACTIONAL_SECONDS";
        public static final String TIMEZONE = "TIMEZONE";
    }

    public static final class NameForm {
        private NameForm() {
        }

        public static final String UPPER = "UPPER";
        public static final String LOWER = "LOWER";
        public static final String TITLE = "TITLE";
    }

    public static final class WordCase {
        private WordCase() {
        }

        public static final String UPPER = "UPPER";
        public static final String LOWER = "LOWER";
        public static final String TITLE = "TITLE";
    }

    final char component;
    final String presentation;
    final int minWidth;
    final int maxWidth;
    final String kind;

    final char secondPresentationModifier;

    final NumericPicture numericPicture;
    final boolean explicitNumeric;
    final boolean lowerCaseRoman;
    final boolean lowerCaseAlphabetic;
    final ParsedTimezonePicture timezonePicture;
    final String nameForm;
    final String wordCase;
    final String formatSpecifier;

    private ParsedVariableMarker(
            char component,
            String presentation,
            int minWidth,
            int maxWidth,
            String kind,
            char secondPresentationModifier,
            NumericPicture numericPicture,
            boolean explicitNumeric,
            boolean lowerCaseRoman,
            boolean lowerCaseAlphabetic,
            ParsedTimezonePicture timezonePicture,
            String nameForm,
            String wordCase
    ) {
        this(
            component,
            presentation,
            minWidth,
            maxWidth,
            kind,
            secondPresentationModifier,
            numericPicture,
            explicitNumeric,
            lowerCaseRoman,
            lowerCaseAlphabetic,
            timezonePicture,
            nameForm,
            wordCase,
            null
        );
    }

    private ParsedVariableMarker(
            char component,
            String presentation,
            int minWidth,
            int maxWidth,
            String kind,
            char secondPresentationModifier,
            NumericPicture numericPicture,
            boolean explicitNumeric,
            boolean lowerCaseRoman,
            boolean lowerCaseAlphabetic,
            ParsedTimezonePicture timezonePicture,
            String nameForm,
            String wordCase,
            String formatSpecifier
    ) {
        this.component = component;
        this.presentation = presentation;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.kind = kind;
        this.secondPresentationModifier = secondPresentationModifier;
        this.numericPicture = numericPicture;
        this.explicitNumeric = explicitNumeric;
        this.lowerCaseRoman = lowerCaseRoman;
        this.lowerCaseAlphabetic = lowerCaseAlphabetic;
        this.timezonePicture = timezonePicture;
        this.nameForm = nameForm;
        this.wordCase = wordCase;
        this.formatSpecifier = formatSpecifier;
    }

    boolean isOrdinal() {
        return this.secondPresentationModifier == ParsedPresentationModifier.ORDINAL;
    }

    int numericValue(
            OffsetDateTime value,
            FormattingContext formattingContext,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        switch (this.component) {
            case 'Y':
                return CalendarFields.year(value, formattingContext);
            case 'M':
                return CalendarFields.monthInYear(value, formattingContext);
            case 'D':
                return CalendarFields.dayInMonth(value, formattingContext);
            case 'd':
                return CalendarFields.dayInYear(value, formattingContext);
            case 'W':
                return CalendarFields.weekInYear(value, formattingContext);
            case 'w':
                return CalendarFields.weekInMonth(value, formattingContext);
            case 'F':
                return CalendarFields.isoDayOfWeek(value, formattingContext);
            case 'H':
                return value.getHour();
            case 'h':
                return hour12(value.getHour());
            case 'm':
                return value.getMinute();
            case 's':
                return value.getSecond();
            case 'P':
                return value.getHour() < 12 ? 0 : 1;
            default:
                throw unsupported(pictureString, metadata, String.valueOf(this.component));
        }
    }

    String nameValue(
            OffsetDateTime value,
            FormattingContext formattingContext,
            String pictureString,
            ExceptionMetadata metadata
    ) {
        switch (this.component) {
            case 'F':
                return DateNames.dayName(
                    value,
                    formattingContext,
                    this.minWidth,
                    this.maxWidth
                );
            case 'M':
                return DateNames.monthName(
                    value,
                    formattingContext,
                    this.minWidth,
                    this.maxWidth
                );
            case 'P':
                Calendar calendar = CalendarFields.calendar(value, formattingContext);
                return DateNames.amPmName(calendar, formattingContext);
            default:
                throw unsupported(pictureString, metadata, this.presentation);
        }
    }

    int defaultNumericMinWidth() {
        switch (this.component) {
            case 'm':
            case 's':
                return 2;
            default:
                return 1;
        }
    }

    boolean appliesYearMaximumWidthRule() {
        return this.component == 'Y';
    }

    static ParsedVariableMarker forDefault(char c, String p, int min, int max, char secondPresentationModifier) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.DEFAULT,
                secondPresentationModifier,
                null,
                false,
                false,
                false,
                null,
                null,
                null
        );
    }

    static ParsedVariableMarker forNumeric(
            char c,
            String p,
            int min,
            int max,
            char secondPresentationModifier,
            NumericPicture numericpicture,
            boolean explicit
    ) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.NUMERIC,
                secondPresentationModifier,
                numericpicture,
                explicit,
                false,
                false,
                null,
                null,
                null
        );
    }

    static ParsedVariableMarker forRoman(
            char c,
            String p,
            int min,
            int max,
            char secondPresentationModifier,
            boolean lower
    ) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.ROMAN,
                secondPresentationModifier,
                null,
                false,
                lower,
                false,
                null,
                null,
                null
        );
    }

    static ParsedVariableMarker forAlphabetic(
            char c,
            String p,
            int min,
            int max,
            char secondPresentationModifier,
            boolean lower
    ) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.ALPHABETIC,
                secondPresentationModifier,
                null,
                false,
                false,
                lower,
                null,
                null,
                null
        );
    }

    static ParsedVariableMarker forName(
            char c,
            String p,
            int min,
            int max,
            char secondPresentationModifier,
            String nameForm
    ) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.NAME,
                secondPresentationModifier,
                null,
                false,
                false,
                false,
                null,
                nameForm,
                null
        );
    }

    static ParsedVariableMarker forWords(
            char c,
            String p,
            int min,
            int max,
            char secondPresentationModifier,
            String wordCase,
            String formatSpecifier
    ) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.WORDS,
                secondPresentationModifier,
                null,
                false,
                false,
                false,
                null,
                null,
                wordCase,
                formatSpecifier
        );
    }

    static ParsedVariableMarker forFractionalSeconds(
            char c,
            String p,
            int min,
            int max,
            char secondPresentationModifier
    ) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.FRACTIONAL_SECONDS,
                secondPresentationModifier,
                null,
                false,
                false,
                false,
                null,
                null,
                null
        );
    }

    static ParsedVariableMarker forAmPm(
            char c,
            String p,
            int min,
            int max,
            char secondPresentationModifier
    ) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.AM_PM,
                secondPresentationModifier,
                null,
                false,
                false,
                false,
                null,
                null,
                null
        );
    }

    static ParsedVariableMarker forTimezone(
            char c,
            String p,
            int min,
            int max,
            char secondPresentationModifier,
            ParsedTimezonePicture tz
    ) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.TIMEZONE,
                secondPresentationModifier,
                null,
                false,
                false,
                false,
                tz,
                null,
                null
        );
    }

    private static int hour12(int hour24) {
        int mod = hour24 % 12;
        return mod == 0 ? 12 : mod;
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
