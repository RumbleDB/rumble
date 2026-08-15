package org.rumbledb.runtime.functions.util.formatting.pictures.FormatInteger;

import lombok.Getter;

@Getter
public class FormatIntegerPicture {

    private final PrimaryFormatToken primaryFormatToken;
    private final IntegerFormatModifier formatModifier;

    FormatIntegerPicture(PrimaryFormatToken primaryFormatToken, IntegerFormatModifier formatModifier) {
        this.primaryFormatToken = primaryFormatToken;
        this.formatModifier = formatModifier;
    }
}
