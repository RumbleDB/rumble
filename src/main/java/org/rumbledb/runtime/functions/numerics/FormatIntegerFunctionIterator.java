package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
import org.rumbledb.runtime.functions.util.formatting.pictures.FormatInteger.IntegerPictureFormatter;

import java.io.Serial;
import java.util.List;

public class FormatIntegerFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public FormatIntegerFunctionIterator(List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> evaluate(
                    LocalCursorUtils.materializeFirst(this.getChild(0), context),
                    LocalCursorUtils.materializeFirst(this.getChild(1), context),
                    this.getChildren().size() > 2
                        ? LocalCursorUtils.materializeFirst(this.getChild(2), context)
                        : null
                ),
                getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item valueItem = this.getChild(0).materializeFirstItemOrNull(context);
        Item pictureItem = this.getChild(1).materializeFirstItemOrNull(context);
        Item languageItem = this.getChildren().size() > 2
            ? this.getChild(2).materializeFirstItemOrNull(context)
            : null;
        return evaluate(valueItem, pictureItem, languageItem);
    }

    private Item evaluate(Item valueItem, Item pictureItem, Item languageItem) {
        String language = languageItem != null && !languageItem.isNull() ? languageItem.getStringValue() : null;

        if (valueItem == null)
            return ItemFactory.getInstance().createStringItem("");

        if (language == null) {
            language = getConfiguration().getDefaultFormattingLanguage();
        }

        String result = IntegerPictureFormatter.format(
            valueItem.getIntegerValue(),
            pictureItem.getStringValue(),
            language,
            getMetadata()
        );

        return ItemFactory.getInstance().createStringItem(result);
    }
}
