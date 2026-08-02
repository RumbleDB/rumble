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

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class IdRefFunctionIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item> {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Pattern NCNAME_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9._-]*");

    public IdRefFunctionIterator(
            List<ItemRuntimePlan> arguments,
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
        Set<String> candidateIds = new HashSet<>();
        for (Item item : argument) {
            String value = item.getStringValue();
            if (NCNAME_PATTERN.matcher(value).matches()) {
                candidateIds.add(value);
            }
        }

        Item node = getContextNode(context);
        if (node == null || !node.isNode()) {
            throw new UnexpectedTypeException("The argument to fn:idref must be a node", getMetadata());
        }
        Item root = node;
        while (root.parent() != null) {
            root = root.parent();
        }
        if (!root.isDocumentNode()) {
            throw new NodeNotInDocumentException(
                    "fn:idref: the node is not part of a tree rooted in a document node",
                    getMetadata()
            );
        }

        List<Item> matches = new ArrayList<>();
        collectIdrefs(root, candidateIds, matches);
        matches.sort((left, right) -> left.getXmlDocumentPosition().compareTo(right.getXmlDocumentPosition()));
        return matches;
    }

    private static void collectIdrefs(Item node, Set<String> candidateIds, List<Item> matches) {
        if (
            (node.isElementNode() || node.isAttributeNode())
                && node.isIdrefs()
                && containsCandidate(node.getStringValue(), candidateIds)
        ) {
            matches.add(node);
        }
        if (node.isElementNode()) {
            for (Item attribute : node.attributes()) {
                collectIdrefs(attribute, candidateIds, matches);
            }
            for (Item child : node.children()) {
                collectIdrefs(child, candidateIds, matches);
            }
        }
    }

    private static boolean containsCandidate(String value, Set<String> candidateIds) {
        String normalized = value.trim().replaceAll("\\s+", " ");
        if (normalized.isEmpty()) {
            return false;
        }
        for (String token : normalized.split(" ")) {
            if (candidateIds.contains(token)) {
                return true;
            }
        }
        return false;
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
