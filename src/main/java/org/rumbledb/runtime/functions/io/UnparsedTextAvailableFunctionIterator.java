package org.rumbledb.runtime.functions.io;

import org.rumbledb.runtime.plan.ItemRuntimePlan;


import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

import java.io.Serial;
import java.util.List;

public class UnparsedTextAvailableFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public UnparsedTextAvailableFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return evaluate(context);
    }

    private Item evaluate(DynamicContext context) {
        Item hrefItem = this.getChild(0).materializeFirstOrNull(context);
        if (hrefItem == null) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        String encoding = null;
        if (this.getChildren().size() == 2) {
            Item encodingItem = this.getChild(1).materializeFirstOrNull(context);
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
