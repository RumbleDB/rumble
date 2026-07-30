package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.util.List;

public class CollationKeyFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public CollationKeyFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item keyItem = this.getChild(0).materializeFirstOrNull(context);
        String collationUri = null;
        if (this.getChildren().size() > 1) {
            Item collationItem = this.getChild(1).materializeFirstOrNull(context);
            collationUri = collationItem == null ? null : collationItem.getStringValue();
        } else {
            collationUri = this.staticContext.getDefaultCollation();
        }
        byte[] bytes = CollationResolver.collationKeyBytes(keyItem.getStringValue(), collationUri, getMetadata());
        return ItemFactory.getInstance().createBase64BinaryItem(java.util.Base64.getEncoder().encodeToString(bytes));
    }
}
