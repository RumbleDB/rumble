package org.rumbledb.runtime.functions.base.formatting;

import org.rumbledb.api.Item;
import org.rumbledb.context.DecimalFormatDefinition;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber.FormatNumberPicture;
import org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber.FormatNumberPictureParser;
import org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber.FormatNumberSubPicture;

public class NumberPictureFormatter {
    private NumberPictureFormatter() {
    }

    public static String format(
            Item valueItem,
            Item pictureItem,
            Item decimalFormatNameItem,
            DecimalFormatDefinition defaultDecimalFormat,
            ExceptionMetadata metadata
    ) {
        DecimalFormatDefinition decimalFormat = null;
        if (decimalFormatNameItem == null) {
            decimalFormat = defaultDecimalFormat;
        } else {
            // TODO resolve decimalFormatName and handle accordingly
        }
        String pictureString = pictureItem.getStringValue();

        FormatNumberPicture picture = FormatNumberPictureParser.parse(pictureString, decimalFormat, metadata);



        return debugFormatNumberPicture(picture);
    }

    private static String debugFormatNumberPicture(FormatNumberPicture picture) {
        return "positive["
            + debugSubpicture(picture.getPositiveSubPicture())
            + "]"
            + " negative["
            + debugSubpicture(picture.getNegativeSubPicture())
            + "]";
    }

    private static String debugSubpicture(FormatNumberSubPicture subpicture) {
        if (subpicture == null) {
            return "null";
        }

        return "prefix="
            + subpicture.prefix()
            + ",integer="
            + subpicture.integerPart()
            + ",fractional="
            + subpicture.fractionalPart()
            + ",suffix="
            + subpicture.suffix()
            + ",percent="
            + subpicture.hasPercent()
            + ",permille="
            + subpicture.hasPerMille()
            + ",intGroups="
            + subpicture.integerPartGroupingPositions()
            + ",repeat="
            + subpicture.repeatingIntegerGroupingInterval()
            + ",fracGroups="
            + subpicture.fractionalPartGroupingPositions()
            + ",minInt="
            + subpicture.minimumIntegerPartSize()
            + ",minFrac="
            + subpicture.minimumFractionalPartSize()
            + ",maxFrac="
            + subpicture.maximumFractionalPartSize();
    }
}
