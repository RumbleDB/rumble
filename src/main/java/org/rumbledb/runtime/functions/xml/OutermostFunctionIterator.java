package org.rumbledb.runtime.functions.xml;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OutermostFunctionIterator extends HybridRuntimeIterator {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> computeResults(context).iterator(),
                getMetadata()
        );
    }

    private static final long serialVersionUID = 1L;

    public OutermostFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    private List<Item> computeResults(DynamicContext context) {
        List<Item> nodes = LocalCursorUtils.materialize(this.getChild(0), context);
        for (Item node : nodes) {
            if (!node.isNode()) {
                throw new UnexpectedTypeException("fn:outermost requires a sequence of nodes", getMetadata());
            }
        }
        Set<Item> nodeSet = new HashSet<>(nodes);
        List<Item> distinctResult = new ArrayList<>();
        Set<Item> seen = new HashSet<>();
        for (Item node : nodes) {
            if (seen.contains(node)) {
                continue;
            }
            boolean hasAncestorInSet = false;
            Item current = node.parent();
            while (current != null) {
                if (nodeSet.contains(current)) {
                    hasAncestorInSet = true;
                    break;
                }
                current = current.parent();
            }
            if (!hasAncestorInSet) {
                distinctResult.add(node);
                seen.add(node);
            }
        }
        distinctResult.sort((a, b) -> a.getXmlDocumentPosition().compareTo(b.getXmlDocumentPosition()));
        return distinctResult;
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("fn:outermost is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:outermost is currently supported only in local execution mode.");
    }
}
