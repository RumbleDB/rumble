package org.rumbledb.runtime.functions.util.formatting.pictures.FormatInteger;

public class FormatIntegerPicture {

    private final PrimaryFormatToken primaryFormatToken;
    private final IntegerFormatModifier formatModifier;

    FormatIntegerPicture(
            PrimaryFormatToken primaryFormatToken,
            IntegerFormatModifier formatModifier
    ) {
        this.primaryFormatToken = primaryFormatToken;
        this.formatModifier = formatModifier;
    }

    public PrimaryFormatToken getPrimaryFormatToken() {
        return this.primaryFormatToken;
    }

    public IntegerFormatModifier getFormatModifier() {
        return this.formatModifier;
    }
}
