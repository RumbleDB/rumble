package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.api.Item;
import org.rumbledb.context.DecimalFormatDefinition;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber.NumberPictureFormatter;

import java.util.List;


public class FormatNumberFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    public FormatNumberFunctionIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item valueItem = this.children.get(0).materializeFirstItemOrNull(context);
        Item pictureItem = this.children.get(1).materializeFirstItemOrNull(context);

        Item decimalFormatNameItem = this.children.size() > 2
            ? this.children.get(2).materializeFirstItemOrNull(context)
            : null;

        DecimalFormatDefinition defaultDecimalFormat = staticContext.getDefaultDecimalFormat();

        if (valueItem == null) {
            return ItemFactory.getInstance().createStringItem(defaultDecimalFormat.getNanSymbol());
        }

        String result = NumberPictureFormatter.format(
            valueItem,
            pictureItem,
            decimalFormatNameItem,
            staticContext,
            getMetadata()
        );
        return ItemFactory.getInstance().createStringItem(result);
    }
}
