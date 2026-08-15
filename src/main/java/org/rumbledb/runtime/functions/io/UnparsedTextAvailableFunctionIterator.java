package org.rumbledb.runtime.functions.io;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class UnparsedTextAvailableFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public UnparsedTextAvailableFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
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
                    getConfiguration().semantics().xmlVersion(),
                    getMetadata());
            return ItemFactory.getInstance().createBooleanItem(true);
        } catch (Exception e) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
    }
}
