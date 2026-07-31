package org.rumbledb.runtime.functions.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.NodeNotInDocumentException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class IdFunctionIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item> {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Pattern NCNAME_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9._-]*");

    public IdFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
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
                if (NCNAME_PATTERN.matcher(token).matches()) {
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
                    "fn:id: the node is not part of a tree rooted in a document node",
                    getMetadata()
            );
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
            for (Item attribute : node.attributes()) {
                Name name = attribute.nodeName();
                if (
                    name != null
                        && Name.XML_NS.equals(name.getNamespace())
                        && "id".equals(name.getLocalName())
                ) {
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
