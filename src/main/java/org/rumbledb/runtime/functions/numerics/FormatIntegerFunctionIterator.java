package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.util.formatting.pictures.FormatInteger.IntegerPictureFormatter;
import scala.language;

import java.util.List;

public class FormatIntegerFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    // TODO neue defaults aus dem dynamic context nutzen, verifizieren, dass der context nun alles hält
    public FormatIntegerFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item valueItem = this.children.get(0).materializeFirstItemOrNull(context);
        Item pictureItem = this.children.get(1).materializeFirstItemOrNull(context);

        Item languageItem = this.children.size() > 2
            ? this.children.get(2).materializeFirstItemOrNull(context)
            : null;

        String language = languageItem != null && !languageItem.isNull() ? languageItem.getStringValue() : null;

        if (valueItem == null)
            return ItemFactory.getInstance().createStringItem("");

        if (valueItem.isNull()) {
            return valueItem;
        }

        if (language == null) {
            language = getConfiguration().getDefaultFormattingLanguage();
        }

        String result = IntegerPictureFormatter.format(
            valueItem,
            pictureItem.getStringValue(),
            language,
            getMetadata()
        );

        return ItemFactory.getInstance().createStringItem(result);
    }
}
