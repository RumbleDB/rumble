package org.rumbledb.runtime.functions.numerics;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.functions.util.formatting.pictures.FormatInteger.IntegerPictureFormatter;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class FormatIntegerFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public FormatIntegerFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item valueItem = this.getChild(0).materializeFirstOrNull(context);
        Item pictureItem = this.getChild(1).materializeFirstOrNull(context);
        Item languageItem = this.getChildren().size() > 2 ? this.getChild(2).materializeFirstOrNull(context) : null;
        return evaluate(valueItem, pictureItem, languageItem);
    }

    private Item evaluate(Item valueItem, Item pictureItem, Item languageItem) {
        String language = languageItem != null && !languageItem.isNull() ? languageItem.getStringValue() : null;

        if (valueItem == null) return ItemFactory.getInstance().createStringItem("");

        if (language == null) {
            language = getConfiguration().formatting().defaultFormattingLanguage();
        }

        String result = IntegerPictureFormatter.format(
                valueItem.getIntegerValue(), pictureItem.getStringValue(), language, getMetadata());

        return ItemFactory.getInstance().createStringItem(result);
    }
}
