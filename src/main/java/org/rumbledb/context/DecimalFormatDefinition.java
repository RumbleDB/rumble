package org.rumbledb.context;

import java.io.Serializable;

public class DecimalFormatDefinition implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
     * The default values for the properties of a decimal format definition, as specified in the XQuery/XPath 3.1
     * functions specification, section 4.7.2
     */
    public static final int DEFAULT_DECIMAL_SEPARATOR = '.';
    public static final int DEFAULT_GROUPING_SEPARATOR = ',';
    public static final String DEFAULT_INFINITY = "Infinity";
    public static final int DEFAULT_MINUS_SIGN = '-';
    public static final String DEFAULT_NAN_SYMBOL = "NaN";
    public static final int DEFAULT_PERCENT = '%';
    public static final int DEFAULT_PER_MILLE = '‰';
    public static final int DEFAULT_ZERO_DIGIT = '0';
    public static final int DEFAULT_OPTIONAL_DIGIT = '#';
    public static final int DEFAULT_PATTERN_SEPARATOR = ';';
    public static final int DEFAULT_EXPONENT_SEPARATOR = 'e';

    private final int decimalSeparator;
    private final int groupingSeparator;
    private final String infinity;
    private final int minusSign;
    private final String nanSymbol;
    private final int percent;
    private final int perMille;
    private final int zeroDigit;
    private final int optionalDigit;
    private final int patternSeparator;
    private final int exponentSeparator;

    private DecimalFormatDefinition() {
        this(
            DEFAULT_DECIMAL_SEPARATOR,
            DEFAULT_GROUPING_SEPARATOR,
            DEFAULT_INFINITY,
            DEFAULT_MINUS_SIGN,
            DEFAULT_NAN_SYMBOL,
            DEFAULT_PERCENT,
            DEFAULT_PER_MILLE,
            DEFAULT_ZERO_DIGIT,
            DEFAULT_OPTIONAL_DIGIT,
            DEFAULT_PATTERN_SEPARATOR,
            DEFAULT_EXPONENT_SEPARATOR
        );
    }

    public static DecimalFormatDefinition defaultInstance() {
        return new DecimalFormatDefinition();
    }

    public DecimalFormatDefinition(
            int decimalSeparator,
            int groupingSeparator,
            String infinity,
            int minusSign,
            String nanSymbol,
            int percent,
            int perMille,
            int zeroDigit,
            int optionalDigit,
            int patternSeparator,
            int exponentSeparator
    ) {
        this.decimalSeparator = decimalSeparator;
        this.groupingSeparator = groupingSeparator;
        this.infinity = infinity;
        this.minusSign = minusSign;
        this.nanSymbol = nanSymbol;
        this.percent = percent;
        this.perMille = perMille;
        this.zeroDigit = zeroDigit;
        this.optionalDigit = optionalDigit;
        this.patternSeparator = patternSeparator;
        this.exponentSeparator = exponentSeparator;
    }

    public int getDecimalSeparator() {
        return this.decimalSeparator;
    }

    public String getInfinity() {
        return this.infinity;
    }

    public int getGroupingSeparator() {
        return this.groupingSeparator;
    }

    public String getNanSymbol() {
        return this.nanSymbol;
    }

    public int getPercent() {
        return this.percent;
    }

    public int getPerMille() {
        return this.perMille;
    }

    public int getZeroDigit() {
        return this.zeroDigit;
    }

    public int getOptionalDigit() {
        return this.optionalDigit;
    }

    public int getPatternSeparator() {
        return this.patternSeparator;
    }

    public int getMinusSign() {
        return this.minusSign;
    }

    public int getExponentSeparator() {
        return this.exponentSeparator;
    }

    @Override
    public String toString() {
        return "DecimalFormatDefinition{"
            + "decimalSeparator='"
            + new String(Character.toChars(this.decimalSeparator))
            + '\''
            + ", groupingSeparator='"
            + new String(Character.toChars(this.groupingSeparator))
            + '\''
            + ", infinity='"
            + this.infinity
            + '\''
            + ", minusSign='"
            + new String(Character.toChars(this.minusSign))
            + '\''
            + ", naN='"
            + this.nanSymbol
            + '\''
            + ", percent='"
            + new String(Character.toChars(this.percent))
            + '\''
            + ", perMille='"
            + new String(Character.toChars(this.perMille))
            + '\''
            + ", zeroDigit='"
            + new String(Character.toChars(this.zeroDigit))
            + '\''
            + ", optionalDigit='"
            + new String(Character.toChars(this.optionalDigit))
            + '\''
            + ", patternSeparator='"
            + new String(Character.toChars(this.patternSeparator))
            + '\''
            + ", exponentSeparator='"
            + new String(Character.toChars(this.exponentSeparator))
            + '\''
            + '}';
    }
}
