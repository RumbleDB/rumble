package org.rumbledb.runtime.functions.numerics;

public class IntegerPicture {

    private final String rawPicture;
    private final String rawPrimaryFormatToken;
    private final String rawFormatModifier;
    private final PrimaryFormatToken primaryFormatToken;
    private final IntegerFormatModifier formatModifier;

    public IntegerPicture(
            String rawPicture,
            String rawPrimaryFormatToken,
            String rawFormatModifier,
            PrimaryFormatToken primaryFormatToken,
            IntegerFormatModifier formatModifier
    ) {
        this.rawPicture = rawPicture;
        this.rawPrimaryFormatToken = rawPrimaryFormatToken;
        this.rawFormatModifier = rawFormatModifier;
        this.primaryFormatToken = primaryFormatToken;
        this.formatModifier = formatModifier;
    }

    public String getRawPicture() {
        return this.rawPicture;
    }

    public String getRawPrimaryFormatToken() {
        return this.rawPrimaryFormatToken;
    }

    public String getRawFormatModifier() {
        return this.rawFormatModifier;
    }

    public PrimaryFormatToken getPrimaryFormatToken() {
        return this.primaryFormatToken;
    }

    public IntegerFormatModifier getFormatModifier() {
        return this.formatModifier;
    }
}
