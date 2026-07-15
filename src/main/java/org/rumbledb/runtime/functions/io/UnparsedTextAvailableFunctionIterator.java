package org.rumbledb.runtime.functions.io;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

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
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item hrefItem = this.children.get(0).materializeFirstItemOrNull(context);
        if (hrefItem == null) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        String encoding = null;
        if (this.children.size() == 2) {
            Item encodingItem = this.children.get(1).materializeFirstItemOrNull(context);
            encoding = encodingItem.getStringValue();
        }
        try {
            UnparsedTextReader.read(
                this.staticURI,
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
