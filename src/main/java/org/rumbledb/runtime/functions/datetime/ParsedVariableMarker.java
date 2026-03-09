package org.rumbledb.runtime.functions.datetime;

import org.rumbledb.runtime.functions.base.formatting.NumericPicture;

final class ParsedVariableMarker {

    enum Kind {
        DEFAULT,
        NUMERIC,
        ROMAN,
        ALPHABETIC,
        NAME,
        WORDS,
        TIMEZONE
    }

    enum NameForm {
        UPPER,
        LOWER,
        TITLE
    }

    enum WordCase {
        UPPER,
        LOWER,
        TITLE
    }

    final char component;
    final String presentation;
    final int minWidth;
    final int maxWidth;
    final Kind kind;

    final boolean ordinal;

    final NumericPicture numericPicture;
    final boolean explicitNumeric;
    final boolean lowerCaseRoman;
    final boolean lowerCaseAlphabetic;
    final ParsedTimezonePicture timezonePicture;
    final NameForm nameForm;
    final WordCase wordCase;

    private ParsedVariableMarker(
            char component,
            String presentation,
            int minWidth,
            int maxWidth,
            Kind kind,
            boolean ordinal,
            NumericPicture numericPicture,
            boolean explicitNumeric,
            boolean lowerCaseRoman,
            boolean lowerCaseAlphabetic,
            ParsedTimezonePicture timezonePicture,
            NameForm nameForm,
            WordCase wordCase
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
            NameForm nameForm
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
            WordCase wordCase
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
