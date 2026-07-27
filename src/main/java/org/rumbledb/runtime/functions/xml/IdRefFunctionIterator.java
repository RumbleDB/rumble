package org.rumbledb.runtime.functions.xml;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class IdRefFunctionIterator extends HybridRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Pattern NCNAME_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9._-]*");
    private static final ErrorCode NOT_IN_DOCUMENT_ERROR_CODE = new ErrorCode(
            new Name(Name.ERROR_NS, "err", "FODC0001")
    );

    private List<Item> results;
    private int currentIndex;

    public IdRefFunctionIterator(
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
        Set<String> candidateIds = new HashSet<>();
        for (Item item : arg) {
            String value = item.getStringValue();
            if (NCNAME_PATTERN.matcher(value).matches()) {
                candidateIds.add(value);
            }
        }

        Item node = getContextNode(this.currentDynamicContextForLocalExecution);
        if (node == null || !node.isNode()) {
            throw new UnexpectedTypeException("The argument to fn:idref must be a node", getMetadata());
        }
        Item root = node;
        while (root.parent() != null) {
            root = root.parent();
        }
        if (!root.isDocumentNode()) {
            throw new RumbleException(
                    "fn:idref: the node is not part of a tree rooted in a document node",
                    NOT_IN_DOCUMENT_ERROR_CODE,
                    getMetadata()
            );
        }

        List<Item> matches = new ArrayList<>();
        collectIdrefs(root, candidateIds, matches);
        matches.sort((a, b) -> a.getXmlDocumentPosition().compareTo(b.getXmlDocumentPosition()));

        this.results = matches;
        this.currentIndex = 0;
        this.hasNext = !this.results.isEmpty();
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
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " fn:idref", getMetadata());
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
        throw new OurBadException("fn:idref is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:idref is currently supported only in local execution mode.");
    }
}
