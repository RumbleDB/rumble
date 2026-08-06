package org.rumbledb.runtime.functions.xml;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.NodeNotInDocumentException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class IdFunctionIterator extends HybridRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Pattern NCNAME_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9._-]*");

    private List<Item> results;
    private int currentIndex;

    public IdFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    protected void openLocal() {
        computeResults();
    }

    private void computeResults() {
        List<Item> arg = this.getChild(0).materialize(this.currentDynamicContextForLocalExecution);
        Set<String> tokens = new HashSet<>();
        for (Item item : arg) {
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

        Item node = getContextNode(this.currentDynamicContextForLocalExecution);
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
        matches.sort((a, b) -> a.getXmlDocumentPosition().compareTo(b.getXmlDocumentPosition()));

        this.results = matches;
        this.currentIndex = 0;
        this.hasNext = !this.results.isEmpty();
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
            return this.getChild(1).materializeFirstItemOrNull(context);
        }
        return context.getVariableValues()
            .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
            .get(0);
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " fn:id", getMetadata());
        }
        Item result = this.results.get(this.currentIndex++);
        this.hasNext = this.currentIndex < this.results.size();
        return result;
    }

    @Override
    protected void closeLocal() {
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("fn:id is currently supported only in local execution mode.");
    }

    @Override
    public HomogeneousItemDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:id is currently supported only in local execution mode.");
    }
}
