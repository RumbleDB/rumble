package org.rumbledb.runtime.xml.axis;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedNodeException;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class AxisIterator extends LocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Comparator<Item> DOCUMENT_ORDER_COMPARATOR = Comparator.comparing(
        Item::getXmlDocumentPosition,
        Comparator.nullsLast(Comparator.naturalOrder())
    );

    protected enum ResultOrder {
        DOCUMENT_ORDER_DISTINCT,
        PRESERVE_SELECTION_ORDER
    }

    private final ResultOrder resultOrder;
    private transient List<Item> results;
    private transient int resultCounter = 0;
    private transient Item nextResult;

    public AxisIterator(RuntimeStaticContext staticContext) {
        this(staticContext, ResultOrder.DOCUMENT_ORDER_DISTINCT);
    }

    protected AxisIterator(RuntimeStaticContext staticContext, ResultOrder resultOrder) {
        super(null, staticContext);
        this.resultOrder = resultOrder;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        setNextResult();
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new AxisLocalCursor(this, context);
    }

    private void setNextResult() {
        if (this.results == null) {
            this.results = prepareResults(this.currentDynamicContextForLocalExecution);
        }
        if (this.resultCounter < this.results.size()) {
            this.nextResult = this.results.get(this.resultCounter++);
        } else {
            this.hasNext = false;
        }
    }

    protected abstract List<Item> selectAxis(List<Item> contextItems);

    private List<Item> prepareResults(DynamicContext context) {
        List<Item> contextItems = context.getVariableValues()
            .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata());
        if (contextItems.isEmpty()) {
            throw new UnexpectedNodeException("Expected at least a node type as context item", getMetadata());
        }
        List<Item> selectedItems = selectAxis(contextItems);
        if (this.resultOrder == ResultOrder.PRESERVE_SELECTION_ORDER) {
            return selectedItems;
        }
        List<Item> normalizedItems = new ArrayList<>(new LinkedHashSet<>(selectedItems));
        normalizedItems.sort(DOCUMENT_ORDER_COMPARATOR);
        return normalizedItems;
    }

    protected List<Item> getDescendants(Item node) {
        List<Item> descendants = new ArrayList<>();
        for (Item child : node.children()) {
            descendants.add(child);
            descendants.addAll(getDescendants(child));
        }
        return descendants;
    }

    protected List<Item> getDescendantsOrSelf(Item node) {
        List<Item> descendantsOrSelf = new ArrayList<>();
        descendantsOrSelf.add(node);
        descendantsOrSelf.addAll(getDescendants(node));
        return descendantsOrSelf;
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

    private static final class AxisLocalCursor extends AbstractLocalCursor<Item> {

        private final AxisIterator plan;
        private final DynamicContext context;
        private List<Item> results;
        private int position;

        private AxisLocalCursor(AxisIterator plan, DynamicContext context) {
            super(plan.getMetadata());
            this.plan = plan;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.results = this.plan.prepareResults(this.context);
            this.position = 0;
        }

        @Override
        protected boolean hasNextLocal() {
            return this.position < this.results.size();
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw invalidState(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in axis");
            }
            return this.results.get(this.position++);
        }

        @Override
        protected void closeLocal() {
            this.results = null;
        }
    }
}
