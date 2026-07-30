package org.rumbledb.runtime.functions.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ContextOrArgumentLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.List;

public class LocalNameFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public LocalNameFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ContextOrArgumentLocalCursor.mapFirstArgumentOrContext(
            this.getChildren(),
            context,
            this::evaluate,
            getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(getContextNode(context));
    }

    private Item evaluate(Item node) {
        if (node == null) {
            return ItemFactory.getInstance().createStringItem("");
        }
        if (!node.isNode()) {
            throw new UnexpectedTypeException("The argument must be a reference to an XML node", getMetadata());
        }
        Name nodeName = node.nodeName();
        if (nodeName == null) {
            return ItemFactory.getInstance().createStringItem("");
        }
        return ItemFactory.getInstance().createStringItem(nodeName.getLocalName());
    }

    private Item getContextNode(DynamicContext context) {
        if (!this.getChildren().isEmpty()) {
            return this.getChild(0).materializeFirstItemOrNull(context);
        }
        return context.getVariableValues()
            .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
            .get(0);
    }
}
