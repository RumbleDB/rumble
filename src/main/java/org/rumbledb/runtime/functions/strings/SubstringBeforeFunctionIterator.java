package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnsupportedCollationException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.List;

public class SubstringBeforeFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public SubstringBeforeFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ComputedLocalCursor.fromArguments(this.getChildren(), context, this::evaluate, getMetadata());
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(
            ComputedLocalCursor.arguments(
                this.getChildren().size(),
                index -> this.getChild(index).materializeFirstItemOrNull(context)
            )
        );
    }

    private Item evaluate(ComputedLocalCursor.Arguments<Item> arguments) {
        Item stringItem = arguments.get(0);
        Item substringItem = arguments.get(1);
        if (arguments.size() == 3) {
            String collation = arguments.get(2).getStringValue();
            if (!collation.equals("http://www.w3.org/2005/xpath-functions/collation/codepoint")) {
                throw new UnsupportedCollationException("Wrong collation parameter", getMetadata());
            }
        }
        if (
            substringItem == null
                || substringItem.getStringValue().isEmpty()
                ||
                stringItem == null
                || stringItem.getStringValue().isEmpty()
        ) {
            return ItemFactory.getInstance().createStringItem("");
        }
        int indexOfOccurrence = stringItem.getStringValue().indexOf(substringItem.getStringValue());
        return indexOfOccurrence == -1
            ? ItemFactory.getInstance().createStringItem("")
            : ItemFactory.getInstance()
                .createStringItem(
                    stringItem.getStringValue()
                        .substring(
                            0,
                            indexOfOccurrence
                        )
                );
    }

}
