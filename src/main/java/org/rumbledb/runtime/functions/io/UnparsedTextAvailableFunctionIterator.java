package org.rumbledb.runtime.functions.io;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.io.Serial;
import java.net.URI;
import java.nio.charset.Charset;
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
        if (this.children.size() == 2) {
            Item encodingItem = this.children.get(1).materializeFirstItemOrNull(context);
            try {
                Charset.forName(encodingItem.getStringValue());
            } catch (Exception e) {
                return ItemFactory.getInstance().createBooleanItem(false);
            }
        }
        try {
            String href = hrefItem.getStringValue();
            URI uri = href.isEmpty() ? this.staticURI : FileSystemUtil.resolveURI(this.staticURI, href, getMetadata());
            FileSystemUtil.readContent(uri, getConfiguration(), getMetadata());
            return ItemFactory.getInstance().createBooleanItem(true);
        } catch (Exception e) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
    }
}
