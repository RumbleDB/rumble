package org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber;

public final class FormatNumberPicture {

    private final FormatNumberSubPicture positiveSubPicture;
    private final FormatNumberSubPicture negativeSubPicture;

    public FormatNumberPicture(
            FormatNumberSubPicture positiveSubPicture,
            FormatNumberSubPicture negativeSubPicture
    ) {
        this.positiveSubPicture = positiveSubPicture;
        this.negativeSubPicture = negativeSubPicture;
    }

    public FormatNumberSubPicture getPositiveSubPicture() {
        return this.positiveSubPicture;
    }

    public FormatNumberSubPicture getNegativeSubPicture() {
        return this.negativeSubPicture;
    }
}
