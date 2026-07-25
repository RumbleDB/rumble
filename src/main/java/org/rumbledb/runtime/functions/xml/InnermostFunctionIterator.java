package org.rumbledb.runtime.functions.xml;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InnermostFunctionIterator extends HybridRuntimeIterator {
    private static final long serialVersionUID = 1L;

    private List<Item> results;
    private int currentIndex;

    public InnermostFunctionIterator(
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
        List<Item> nodes = this.getChild(0).materialize(this.currentDynamicContextForLocalExecution);
        for (Item node : nodes) {
            if (!node.isNode()) {
                throw new UnexpectedTypeException("fn:innermost requires a sequence of nodes", getMetadata());
            }
        }
        Set<Item> nodeSet = new HashSet<>(nodes);
        Set<Item> excluded = new HashSet<>();
        for (Item node : nodes) {
            Item current = node.parent();
            while (current != null) {
                if (nodeSet.contains(current)) {
                    excluded.add(current);
                }
                current = current.parent();
            }
        }
        List<Item> distinctResult = new ArrayList<>();
        Set<Item> seen = new HashSet<>();
        for (Item node : nodes) {
            if (!excluded.contains(node) && seen.add(node)) {
                distinctResult.add(node);
            }
        }
        distinctResult.sort((a, b) -> a.getXmlDocumentPosition().compareTo(b.getXmlDocumentPosition()));
        this.results = distinctResult;
        this.currentIndex = 0;
        this.hasNext = !this.results.isEmpty();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " fn:innermost", getMetadata());
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
        throw new OurBadException("fn:innermost is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:innermost is currently supported only in local execution mode.");
    }
}
