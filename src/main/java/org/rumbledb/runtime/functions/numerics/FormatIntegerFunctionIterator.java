package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.formatting.NumericPictureFormatter;

import java.util.List;

public class FormatIntegerFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    public FormatIntegerFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item valueItem = this.children.get(0).materializeFirstItemOrNull(context);
        Item pictureItem = this.children.get(1).materializeFirstItemOrNull(context);

        Item langItem = this.children.size() > 2
            ? this.children.get(2).materializeFirstItemOrNull(context)
            : null; // TODO IMPLEMENT

        if (valueItem == null)
            return ItemFactory.getInstance().createStringItem("");

        if (valueItem.isNull()) {
            return valueItem;
        }

        String result = NumericPictureFormatter.formatInteger(valueItem, pictureItem.getStringValue(), getMetadata());

        return ItemFactory.getInstance().createStringItem(result);
    }
}
