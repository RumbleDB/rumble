package org.rumbledb.runtime.xml.axis;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedNodeException;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
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

    protected enum Axis {
        ANCESTOR,
        ANCESTOR_OR_SELF,
        ATTRIBUTE,
        CHILD,
        DESCENDANT,
        DESCENDANT_OR_SELF,
        FOLLOWING,
        FOLLOWING_SIBLING,
        PARENT,
        PRECEDING,
        PRECEDING_SIBLING,
        SELF
    }

    private final Axis axis;
    private final ResultOrder resultOrder;

    protected AxisIterator(RuntimeStaticContext staticContext, Axis axis) {
        this(staticContext, axis, ResultOrder.DOCUMENT_ORDER_DISTINCT);
    }

    protected AxisIterator(RuntimeStaticContext staticContext, Axis axis, ResultOrder resultOrder) {
        super(java.util.List.of(), staticContext);
        this.axis = axis;
        this.resultOrder = resultOrder;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new AxisLocalCursor(
                this.axis,
                this.resultOrder,
                context,
                getRuntimeStaticContext()
        );
    }

    private List<Item> prepareResults(DynamicContext context) {
        return prepareResults(this.axis, this.resultOrder, context, getRuntimeStaticContext());
    }

    private static List<Item> prepareResults(
            Axis axis,
            ResultOrder resultOrder,
            DynamicContext context,
            RuntimeStaticContext staticContext
    ) {
        List<Item> contextItems = context.getVariableValues()
            .getLocalVariableValue(Name.CONTEXT_ITEM, staticContext.getMetadata());
        if (contextItems.isEmpty()) {
            throw new UnexpectedNodeException(
                    "Expected at least a node type as context item",
                    staticContext.getMetadata()
            );
        }
        List<Item> selectedItems = selectAxis(axis, contextItems);
        if (resultOrder == ResultOrder.PRESERVE_SELECTION_ORDER) {
            return selectedItems;
        }
        List<Item> normalizedItems = new ArrayList<>(new LinkedHashSet<>(selectedItems));
        normalizedItems.sort(DOCUMENT_ORDER_COMPARATOR);
        return normalizedItems;
    }

    private static List<Item> selectAxis(Axis axis, List<Item> contextItems) {
        List<Item> results = new ArrayList<>();
        for (Item node : contextItems) {
            switch (axis) {
                case ANCESTOR -> results.addAll(getAncestors(node));
                case ANCESTOR_OR_SELF -> {
                    results.addAll(getAncestors(node));
                    results.add(node);
                }
                case ATTRIBUTE -> results.addAll(node.attributes());
                case CHILD -> results.addAll(node.children());
                case DESCENDANT -> results.addAll(getDescendants(node));
                case DESCENDANT_OR_SELF -> {
                    results.addAll(getDescendants(node));
                    results.add(node);
                }
                case FOLLOWING -> results.addAll(getFollowingNodes(node.parent(), node));
                case FOLLOWING_SIBLING -> results.addAll(getFollowingSiblings(node));
                case PARENT -> {
                    if (node.parent() != null) {
                        results.add(node.parent());
                    }
                }
                case PRECEDING -> results.addAll(getPrecedingNodes(node.parent(), node));
                case PRECEDING_SIBLING -> results.addAll(getPrecedingSiblings(node));
                case SELF -> results.add(node);
            }
        }
        return results;
    }

    private static List<Item> getDescendants(Item node) {
        List<Item> descendants = new ArrayList<>();
        for (Item child : node.children()) {
            descendants.add(child);
            descendants.addAll(getDescendants(child));
        }
        return descendants;
    }

    private static List<Item> getDescendantsOrSelf(Item node) {
        List<Item> descendantsOrSelf = new ArrayList<>();
        descendantsOrSelf.add(node);
        descendantsOrSelf.addAll(getDescendants(node));
        return descendantsOrSelf;
    }

    private static List<Item> getAncestors(Item node) {
        List<Item> ancestors = new ArrayList<>();
        Item parent = node.parent();
        while (parent != null) {
            ancestors.add(parent);
            parent = parent.parent();
        }
        return ancestors;
    }

    private static List<Item> getFollowingNodes(Item parent, Item node) {
        if (parent == null) {
            return Collections.emptyList();
        }
        List<Item> followingNodes = new ArrayList<>();
        List<Item> siblings = parent.children();
        int followingIndex = -1;
        for (int i = 0; i < siblings.size(); i++) {
            if (siblings.get(i).equals(node)) {
                followingIndex = i + 1;
                break;
            }
        }
        for (int i = followingIndex; i > 0 && i < siblings.size(); i++) {
            followingNodes.addAll(getDescendantsOrSelf(siblings.get(i)));
        }
        followingNodes.addAll(getFollowingNodes(parent.parent(), parent));
        return followingNodes;
    }

    private static List<Item> getFollowingSiblings(Item node) {
        Item parent = node.parent();
        if (parent == null || parent.isNull()) {
            return Collections.emptyList();
        }
        List<Item> siblings = parent.children();
        int start = 0;
        for (int i = 0; i < siblings.size(); i++) {
            if (siblings.get(i).equals(node)) {
                start = i + 1;
                break;
            }
        }
        return start == 0 ? Collections.emptyList() : new ArrayList<>(siblings.subList(start, siblings.size()));
    }

    private static List<Item> getPrecedingNodes(Item parent, Item node) {
        if (parent == null) {
            return Collections.emptyList();
        }
        List<Item> precedingNodes = new ArrayList<>();
        List<Item> siblings = parent.children();
        int end = siblings.size();
        for (int i = 0; i < siblings.size(); i++) {
            if (siblings.get(i).equals(node)) {
                end = i;
                break;
            }
        }
        for (int i = 0; i < end; i++) {
            precedingNodes.addAll(getDescendantsOrSelf(siblings.get(i)));
        }
        precedingNodes.addAll(getPrecedingNodes(parent.parent(), parent));
        return precedingNodes;
    }

    private static List<Item> getPrecedingSiblings(Item node) {
        Item parent = node.parent();
        if (parent == null || parent.isNull()) {
            return Collections.emptyList();
        }
        List<Item> siblings = parent.children();
        int end = 0;
        for (int i = 0; i < siblings.size(); i++) {
            if (siblings.get(i).equals(node)) {
                end = i;
                break;
            }
        }
        return new ArrayList<>(siblings.subList(0, end));
    }

    private static final class AxisLocalCursor extends AbstractLocalCursor<Item> {

        private final Axis axis;
        private final ResultOrder resultOrder;
        private final DynamicContext context;
        private final RuntimeStaticContext staticContext;
        private List<Item> results;
        private int position;

        private AxisLocalCursor(
                Axis axis,
                ResultOrder resultOrder,
                DynamicContext context,
                RuntimeStaticContext staticContext
        ) {
            super(staticContext.getMetadata());
            this.axis = axis;
            this.resultOrder = resultOrder;
            this.context = context;
            this.staticContext = staticContext;
        }

        @Override
        protected void openLocal() {
            this.results = prepareResults(
                this.axis,
                this.resultOrder,
                this.context,
                this.staticContext
            );
            this.position = 0;
        }

        @Override
        protected boolean hasNextLocal() {
            return this.position < this.results.size();
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw invalidState(IteratorFlowException.FLOW_EXCEPTION_MESSAGE + " in axis");
            }
            return this.results.get(this.position++);
        }

        @Override
        protected void closeLocal() {
            this.results = null;
        }
    }
}
