package org.rumbledb.runtime.functions.typing;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.QNameItem;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

import java.io.Serial;
import java.util.List;

public class NamespaceURIFromQNameFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public NamespaceURIFromQNameFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return evaluate(this.getChild(0).materializeFirstOrNull(context));
    }

    private static Item evaluate(Item item) {
        QNameItem qnameItem = (QNameItem) item;
        if (qnameItem == null) {
            return null;
        }
        Name qname = qnameItem.getQNameValue();
        String namespace = qname.getNamespace();
        return ItemFactory.getInstance().createAnyURIItem(namespace == null ? "" : namespace);
    }
}
