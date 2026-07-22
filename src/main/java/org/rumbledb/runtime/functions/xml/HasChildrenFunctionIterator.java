package org.rumbledb.runtime.functions.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.util.List;

public class HasChildrenFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public HasChildrenFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item node = getContextNode(context);
        if (node == null) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        if (!node.isNode()) {
            throw new UnexpectedTypeException("The argument to fn:has-children must be a node", getMetadata());
        }
        return ItemFactory.getInstance().createBooleanItem(!node.children().isEmpty());
    }

    private Item getContextNode(DynamicContext context) {
        if (!this.children.isEmpty()) {
            return this.children.get(0).materializeFirstItemOrNull(context);
        }
        return context.getVariableValues()
            .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
            .get(0);
    }
}
