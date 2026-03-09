package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.runtime.functions.base.formatting.NumericPicture;

public class PrimaryFormatToken {

    public static final String DECIMAL = "DECIMAL";
    public static final String ALPHABETIC_UPPER = "ALPHABETIC_UPPER";
    public static final String ALPHABETIC_LOWER = "ALPHABETIC_LOWER";
    public static final String ROMAN_UPPER = "ROMAN_UPPER";
    public static final String ROMAN_LOWER = "ROMAN_LOWER";
    public static final String WORDS_LOWER = "WORDS_LOWER";
    public static final String WORDS_UPPER = "WORDS_UPPER";
    public static final String WORDS_TITLE = "WORDS_TITLE";
    public static final String OTHER = "OTHER";

    private final String type;
    private final NumericPicture numericPicture;
    private final String otherToken;

    private PrimaryFormatToken(
            String type,
            NumericPicture numericPicture,
            String otherToken
    ) {
        this.type = type;
        this.numericPicture = numericPicture;
        this.otherToken = otherToken;
    }

    public static PrimaryFormatToken decimal(NumericPicture numericPicture) {
        return new PrimaryFormatToken(DECIMAL, numericPicture, null);
    }

    public static PrimaryFormatToken alphabeticUpper() {
        return new PrimaryFormatToken(ALPHABETIC_UPPER, null, null);
    }

    public static PrimaryFormatToken alphabeticLower() {
        return new PrimaryFormatToken(ALPHABETIC_LOWER, null, null);
    }

    public static PrimaryFormatToken romanUpper() {
        return new PrimaryFormatToken(ROMAN_UPPER, null, null);
    }

    public static PrimaryFormatToken romanLower() {
        return new PrimaryFormatToken(ROMAN_LOWER, null, null);
    }

    public static PrimaryFormatToken wordsLower() {
        return new PrimaryFormatToken(WORDS_LOWER, null, null);
    }

    public static PrimaryFormatToken wordsUpper() {
        return new PrimaryFormatToken(WORDS_UPPER, null, null);
    }

    public static PrimaryFormatToken wordsTitle() {
        return new PrimaryFormatToken(WORDS_TITLE, null, null);
    }

    public static PrimaryFormatToken other(String otherToken) {
        return new PrimaryFormatToken(OTHER, null, otherToken);
    }

    public String getType() {
        return this.type;
    }

    public NumericPicture getNumericPicture() {
        return this.numericPicture;
    }

    public String getOtherToken() {
        return this.otherToken;
    }
}
