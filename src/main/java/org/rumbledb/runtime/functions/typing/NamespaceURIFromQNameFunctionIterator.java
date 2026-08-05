package org.rumbledb.runtime.functions.typing;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.QNameItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

public class NamespaceURIFromQNameFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial private static final long serialVersionUID = 1L;

    public NamespaceURIFromQNameFunctionIterator(
            List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        QNameItem qnameItem = (QNameItem) this.getChild(0).materializeFirstItemOrNull(context);
        if (qnameItem == null) {
            return null;
        }
        Name qname = qnameItem.getQNameValue();
        String namespace = qname.getNamespace();
        return ItemFactory.getInstance().createAnyURIItem(namespace == null ? "" : namespace);
    }
}
