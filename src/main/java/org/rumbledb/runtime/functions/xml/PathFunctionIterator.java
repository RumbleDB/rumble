package org.rumbledb.runtime.functions.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class PathFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String ROOT_PREFIX = "Q{http://www.w3.org/2005/xpath-functions}root()";

    public PathFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(() -> materializeFirstItemOrNull(context), getMetadata());
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item node = getContextNode(context);
        if (node == null) {
            return null;
        }
        if (!node.isNode()) {
            throw new UnexpectedTypeException("The argument to fn:path must be a node", getMetadata());
        }
        if (node.isDocumentNode()) {
            return ItemFactory.getInstance().createStringItem("/");
        }

        List<Item> ancestorOrSelf = new ArrayList<>();
        Item current = node;
        while (current.parent() != null) {
            ancestorOrSelf.add(current);
            current = current.parent();
        }
        Item root = current;
        Collections.reverse(ancestorOrSelf);

        StringBuilder result = new StringBuilder();
        if (!root.isDocumentNode()) {
            result.append(ROOT_PREFIX);
        }
        for (Item step : ancestorOrSelf) {
            result.append(stepFor(step));
        }
        return ItemFactory.getInstance().createStringItem(result.toString());
    }

    private static String stepFor(Item node) {
        Item parent = node.parent();
        if (node.isElementNode()) {
            Name name = node.nodeName();
            int position = positionAmong(
                node,
                parent,
                candidate -> candidate.isElementNode() && sameName(candidate.nodeName(), name)
            );
            return "/Q{" + namespaceOrEmpty(name) + "}" + name.getLocalName() + "[" + position + "]";
        }
        if (node.isAttributeNode()) {
            Name name = node.nodeName();
            if (namespaceOrEmpty(name).isEmpty()) {
                return "/@" + name.getLocalName();
            }
            return "/@Q{" + namespaceOrEmpty(name) + "}" + name.getLocalName();
        }
        if (node.isTextNode()) {
            int position = positionAmong(node, parent, Item::isTextNode);
            return "/text()[" + position + "]";
        }
        if (node.isCommentNode()) {
            int position = positionAmong(node, parent, Item::isCommentNode);
            return "/comment()[" + position + "]";
        }
        if (node.isProcessingInstructionNode()) {
            String target = node.nodeName().getLocalName();
            int position = positionAmong(
                node,
                parent,
                candidate -> candidate.isProcessingInstructionNode()
                    && candidate.nodeName().getLocalName().equals(target)
            );
            return "/processing-instruction(" + target + ")[" + position + "]";
        }
        if (node.isNamespaceNode()) {
            Name name = node.nodeName();
            if (name == null) {
                return "/namespace::*[Q{http://www.w3.org/2005/xpath-functions}local-name()=\"\"]";
            }
            return "/namespace::" + name.getLocalName();
        }
        throw new OurBadException("fn:path: unexpected node kind");
    }

    private static int positionAmong(Item node, Item parent, Predicate<Item> matches) {
        if (parent == null) {
            return 1;
        }
        int position = 0;
        for (Item candidate : parent.children()) {
            if (matches.test(candidate)) {
                position++;
                if (sameNode(candidate, node)) {
                    return position;
                }
            }
        }
        return position;
    }

    private static boolean sameNode(Item a, Item b) {
        if (a.getXmlDocumentPosition() != null && b.getXmlDocumentPosition() != null) {
            return a.getXmlDocumentPosition().equals(b.getXmlDocumentPosition());
        }
        return a == b;
    }

    private static boolean sameName(Name a, Name b) {
        return namespaceOrEmpty(a).equals(namespaceOrEmpty(b)) && a.getLocalName().equals(b.getLocalName());
    }

    private static String namespaceOrEmpty(Name name) {
        return name.getNamespace() == null ? "" : name.getNamespace();
    }

    private Item getContextNode(DynamicContext context) {
        if (this.getChildren().size() == 1) {
            return this.getChild(0).materializeFirstItemOrNull(context);
        }
        return context.getVariableValues()
            .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
            .get(0);
    }
}
