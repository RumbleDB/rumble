package org.rumbledb.runtime.functions.util.formatting.pictures.FormatInteger;

import lombok.Getter;

public class IntegerFormatModifier {

    public static final String CARDINAL = "CARDINAL";
    public static final String ORDINAL = "ORDINAL";

    public static final String ALPHABETIC = "ALPHABETIC";
    public static final String TRADITIONAL = "TRADITIONAL";

    @Getter
    private final String numberType;
    @Getter
    private final String formatSpecifier;

    @SuppressWarnings("unused")
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

}
