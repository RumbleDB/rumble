package org.rumbledb.runtime.functions.typing;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.QNameItem;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

import java.io.Serial;
import java.util.List;

public class PrefixFromQNameFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public PrefixFromQNameFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item item = this.getChild(0).materializeFirstOrNull(context);
        QNameItem qnameItem = (QNameItem) item;
        if (qnameItem == null) {
            return null;
        }
        String prefix = qnameItem.getQNameValue().getPrefix();
        if (prefix == null)
            return null;
        return ItemFactory.getInstance().createNCNameItem(prefix);
    }


}
