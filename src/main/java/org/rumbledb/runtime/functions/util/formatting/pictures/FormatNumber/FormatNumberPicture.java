package org.rumbledb.runtime.functions.util.formatting.pictures.FormatNumber;

import lombok.Getter;

@Getter
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

}
