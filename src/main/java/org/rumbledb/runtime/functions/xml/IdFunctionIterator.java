package org.rumbledb.runtime.functions.xml;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.NodeNotInDocumentException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.xml.NamespaceBindingUtils;

public class IdFunctionIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {
    @Serial
    private static final long serialVersionUID = 1L;

    public IdFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> computeResults(context).iterator(), getMetadata());
    }

    private List<Item> computeResults(DynamicContext context) {
        List<Item> argument = this.getChild(0).materialize(context);
        Set<String> tokens = new HashSet<>();
        for (Item item : argument) {
            String normalized = item.getStringValue().trim().replaceAll("\\s+", " ");
            if (normalized.isEmpty()) {
                continue;
            }
            for (String token : normalized.split(" ")) {
                if (NamespaceBindingUtils.isValidNcName(token)) {
                    tokens.add(token);
                }
            }
        }

        Item node = getContextNode(context);
        if (node == null || !node.isNode()) {
            throw new UnexpectedTypeException("The argument to fn:id must be a node", getMetadata());
        }
        Item root = node;
        while (root.parent() != null) {
            root = root.parent();
        }
        if (!root.isDocumentNode()) {
            throw new NodeNotInDocumentException(
                    "fn:id: the node is not part of a tree rooted in a document node", getMetadata());
        }

        Map<String, Item> firstElementByIdValue = new HashMap<>();
        indexIds(root, firstElementByIdValue);

        List<Item> matches = new ArrayList<>();
        Set<Item> seen = new HashSet<>();
        for (String token : tokens) {
            Item element = firstElementByIdValue.get(token);
            if (element != null && seen.add(element)) {
                matches.add(element);
            }
        }
        matches.sort((left, right) -> left.getXmlDocumentPosition().compareTo(right.getXmlDocumentPosition()));
        return matches;
    }

    private static void indexIds(Item node, Map<String, Item> firstElementByIdValue) {
        if (node.isElementNode()) {
            // Schema validation can mark the element's own typed value as an ID.
            if (node.isId()) {
                firstElementByIdValue.putIfAbsent(node.getStringValue().trim().replaceAll("\\s+", " "), node);
            }
            for (Item attribute : node.attributes()) {
                Name name = attribute.nodeName();
                // An ID attribute identifies its owning element, including the existing xml:id path.
                if (attribute.isId()
                        || (name != null
                                && Name.XML_NS.equals(name.getNamespace())
                                && "id".equals(name.getLocalName()))) {
                    String normalizedId = attribute.getStringValue().trim().replaceAll("\\s+", " ");
                    firstElementByIdValue.putIfAbsent(normalizedId, node);
                }
            }
        }
        for (Item child : node.children()) {
            indexIds(child, firstElementByIdValue);
        }
    }

    private Item getContextNode(DynamicContext context) {
        if (this.getChildren().size() == 2) {
            return this.getChild(1).materializeFirstOrNull(context);
        }
        return context.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
                .get(0);
    }
}
