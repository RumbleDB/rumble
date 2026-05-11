package org.rumbledb.runtime.xml.axis;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class AxisIterator extends LocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    protected List<Item> results;
    protected int resultCounter = 0;
    protected Item nextResult;

    public AxisIterator(RuntimeStaticContext staticContext) {
        super(null, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        setNextResult();
    }


    protected abstract void setNextResult();

    protected void storeNextResult() {
        if (this.resultCounter == 0) {
            // Remove duplicates
            this.results = new ArrayList<>(new LinkedHashSet<>(this.results));
            // Sort values in document order.
            this.results.sort(Comparator.comparing(Item::getXmlDocumentPosition));
        }
        if (this.resultCounter < this.results.size()) {
            this.nextResult = this.results.get(this.resultCounter++);
        } else {
            this.hasNext = false;
        }
    }

    protected List<Item> getDescendants(Item node) {
        List<Item> descendants = new ArrayList<>();
        for (Item child : node.children()) {
            descendants.add(child);
            descendants.addAll(getDescendants(child));
        }
        return descendants;
    }

    protected List<Item> getAncestors(Item node) {
        List<Item> ancestors = new ArrayList<>();
        Item parent = node.parent();
        while (parent != null) {
            ancestors.add(parent);
            parent = parent.parent();
        }
        return ancestors;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item nextResult = this.nextResult;
            setNextResult();
            return nextResult;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in axis",
                getMetadata()
        );
    }

    @Override
    public void close() {
        super.close();
        this.hasNext = false;
        this.nextResult = null;
        this.results = null;
        this.resultCounter = 0;
    }
}
