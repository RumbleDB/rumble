package org.rumbledb.context;

public class DecimalFormatDefinition {

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

    public DecimalFormatDefinition() {
        this.decimalSeparator = '.';
        this.groupingSeparator = ',';
        this.infinity = "Infinity";
        this.minusSign = '-';
        this.nanSymbol = "NaN";
        this.percent = '%';
        this.perMille = '‰';
        this.zeroDigit = '0';
        this.optionalDigit = '#';
        this.patternSeparator = ';';
        this.exponentSeparator = 'e';
    }

    public int getDecimalSeparator() {
        return this.decimalSeparator;
    } // TODO codepoints besser machen als strings direkt zurückgeben

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
        return exponentSeparator;
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
            + '}';
    }
}
