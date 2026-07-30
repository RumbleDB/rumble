package org.rumbledb.runtime.functions.io;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.List;

public class UnparsedTextAvailableFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public UnparsedTextAvailableFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ComputedLocalCursor.fromArguments(
            this.getChildren(),
            context,
            arguments -> evaluate(arguments, context),
            getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(
            ComputedLocalCursor.arguments(
                this.getChildren().size(),
                index -> this.getChild(index).materializeFirstItemOrNull(context)
            ),
            context
        );
    }

    private Item evaluate(ComputedLocalCursor.Arguments<Item> arguments, DynamicContext context) {
        Item hrefItem = arguments.get(0);
        if (hrefItem == null) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        String encoding = null;
        if (arguments.size() == 2) {
            Item encodingItem = arguments.get(1);
            encoding = encodingItem.getStringValue();
        }
        try {
            UnparsedTextReader.read(
                this.staticContext.getStaticURI(),
                hrefItem.getStringValue(),
                encoding,
                context.getRumbleRuntimeConfiguration(),
                getConfiguration().getXmlVersion(),
                getMetadata()
            );
            return ItemFactory.getInstance().createBooleanItem(true);
        } catch (Exception e) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
    }
}
