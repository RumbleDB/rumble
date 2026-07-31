package org.rumbledb.runtime.functions.xml;


import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InnermostFunctionIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> computeResults(context).iterator(),
                getMetadata()
        );
    }

    private static final long serialVersionUID = 1L;

    public InnermostFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    private List<Item> computeResults(DynamicContext context) {
        List<Item> nodes = this.getChild(0).materialize(context);
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
        return distinctResult;
    }
}
