package org.rumbledb.runtime.functions.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

import java.io.Serial;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GenerateIdFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public GenerateIdFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item node = getContextNode(context);
        if (node == null) {
            return ItemFactory.getInstance().createStringItem("");
        }
        if (!node.isNode()) {
            throw new UnexpectedTypeException("The argument to fn:generate-id must be a node", getMetadata());
        }
        return ItemFactory.getInstance().createStringItem(generateId(node));
    }

    private static String generateId(Item node) {
        XMLDocumentPosition position = node.getXmlDocumentPosition();
        if (position == null) {
            return "N" + Long.toUnsignedString(System.identityHashCode(node), 36);
        }
        BigInteger pathValue = new BigInteger(1, position.getPath().getBytes(StandardCharsets.UTF_8));
        return "N" + pathValue.toString(36) + "P" + Integer.toUnsignedString(position.getDocPosition(), 36);
    }

    private Item getContextNode(DynamicContext context) {
        if (this.getChildren().size() == 1) {
            return this.getChild(0).materializeFirstOrNull(context);
        }
        return context.getVariableValues()
            .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
            .get(0);
    }

}
