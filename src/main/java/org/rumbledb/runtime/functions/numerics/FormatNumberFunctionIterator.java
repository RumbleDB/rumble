package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

import static java.lang.Float.NaN;

public class FormatNumberFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    public FormatNumberFunctionIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item valueItem = this.children.get(0).materializeFirstItemOrNull(context);
        Item pictureItem = this.children.get(1).materializeFirstItemOrNull(context);

        Item decimalFormatNameItem = this.children.size() > 2
            ? this.children.get(2).materializeFirstItemOrNull(context)
            : null; // TODO IMPLEMENT

        if (valueItem == null) {
            return ItemFactory.getInstance().createFloatItem(NaN);
        }
        if (valueItem.isNull()) {
            return valueItem;
        }

        String result = "";
        return ItemFactory.getInstance().createStringItem(result);
    }
}
