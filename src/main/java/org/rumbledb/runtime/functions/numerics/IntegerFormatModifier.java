package org.rumbledb.runtime.functions.numerics;

public class IntegerFormatModifier {

    public static final String CARDINAL = "CARDINAL";
    public static final String ORDINAL = "ORDINAL";

    public static final String ALPHABETIC = "ALPHABETIC";
    public static final String TRADITIONAL = "TRADITIONAL";

    private final String numberType;
    private final String formatSpecifier; // not supported yet
    private final String numberingVariant; // not supported yet

    public IntegerFormatModifier(
            String numberType,
            String formatSpecifier,
            String numberingVariant
    ) {
        this.numberType = numberType;
        this.formatSpecifier = formatSpecifier;
        this.numberingVariant = numberingVariant;
    }

    public String getNumberType() {
        return this.numberType;
    }

    public String getFormatSpecifier() {
        return this.formatSpecifier;
    }

    public String getNumberingVariant() {
        return this.numberingVariant;
    }
}
