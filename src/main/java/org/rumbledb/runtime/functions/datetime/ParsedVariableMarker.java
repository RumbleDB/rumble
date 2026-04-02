package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.runtime.functions.base.formatting.NumericPicture;

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

    final boolean ordinal;

    final NumericPicture numericPicture;
    final boolean explicitNumeric;
    final boolean lowerCaseRoman;
    final boolean lowerCaseAlphabetic;
    final ParsedTimezonePicture timezonePicture;
    final String nameForm;
    final String wordCase;

    private ParsedVariableMarker(
            char component,
            String presentation,
            int minWidth,
            int maxWidth,
            String kind,
            boolean ordinal,
            NumericPicture numericPicture,
            boolean explicitNumeric,
            boolean lowerCaseRoman,
            boolean lowerCaseAlphabetic,
            ParsedTimezonePicture timezonePicture,
            String nameForm,
            String wordCase
    ) {
        this.component = component;
        this.presentation = presentation;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.kind = kind;
        this.ordinal = ordinal;
        this.numericPicture = numericPicture;
        this.explicitNumeric = explicitNumeric;
        this.lowerCaseRoman = lowerCaseRoman;
        this.lowerCaseAlphabetic = lowerCaseAlphabetic;
        this.timezonePicture = timezonePicture;
        this.nameForm = nameForm;
        this.wordCase = wordCase;
    }

    static ParsedVariableMarker forDefault(char c, String p, int min, int max, boolean ordinal) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.DEFAULT,
                ordinal,
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
            boolean ordinal,
            NumericPicture np,
            boolean explicit
    ) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.NUMERIC,
                ordinal,
                np,
                explicit,
                false,
                false,
                null,
                null,
                null
        );
    }

    static ParsedVariableMarker forRoman(char c, String p, int min, int max, boolean ordinal, boolean lower) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.ROMAN,
                ordinal,
                null,
                false,
                lower,
                false,
                null,
                null,
                null
        );
    }

    static ParsedVariableMarker forAlphabetic(char c, String p, int min, int max, boolean ordinal, boolean lower) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.ALPHABETIC,
                ordinal,
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
            boolean ordinal,
            String nameForm
    ) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.NAME,
                ordinal,
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
            boolean ordinal,
            String wordCase
    ) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.WORDS,
                ordinal,
                null,
                false,
                false,
                false,
                null,
                null,
                wordCase
        );
    }

    static ParsedVariableMarker forTimezone(
            char c,
            String p,
            int min,
            int max,
            boolean ordinal,
            ParsedTimezonePicture tz
    ) {
        return new ParsedVariableMarker(
                c,
                p,
                min,
                max,
                Kind.TIMEZONE,
                ordinal,
                null,
                false,
                false,
                false,
                tz,
                null,
                null
        );
    }
}
