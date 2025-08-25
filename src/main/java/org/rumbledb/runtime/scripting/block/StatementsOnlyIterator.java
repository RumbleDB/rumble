package org.rumbledb.runtime.scripting.block;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class StatementsOnlyIterator extends AtMostOneItemLocalRuntimeIterator {
    private RuntimeIterator currentChild;
    private int childIndex;

    public StatementsOnlyIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
        for (RuntimeIterator child : children) {
            if (child.isSequential()) {
                this.isSequential = child.isSequential();
            }
        }
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        startLocal(dynamicContext);
        return null;
    }

    private void startLocal(DynamicContext dynamicContext) {
        // Case exists for when an empty bracket is provided for some statement
        if (this.children.isEmpty()) {
            this.hasNext = false;
            return;
        }
        this.childIndex = 0;
        this.currentChild = this.children.get(this.childIndex);
        this.currentDynamicContextForLocalExecution = dynamicContext;
        this.currentChild.open(this.currentDynamicContextForLocalExecution);

        iterateChildren();
    }

    public void iterateChildren() {
        while (this.currentChild != null) {
            if (!this.currentChild.hasNext()) {
                this.currentChild.close();
                if (++this.childIndex == this.children.size()) {
                    this.currentChild = null;
                } else {
                    this.currentChild = this.children.get(this.childIndex);
                    this.currentChild.open(this.currentDynamicContextForLocalExecution);
                }
            } else {
                // We have a statement with next. Result is ignored
                this.currentChild.next();
            }
        }

        this.hasNext = false;
    }

    @Override
    public void open(DynamicContext dynamicContext) {
        startLocal(dynamicContext);
    }

    @Override
    public void close() {
        if (this.currentChild != null) {
            this.currentChild.close();
        }
    }

    @Override
    public void reset(DynamicContext dynamicContext) {
        startLocal(dynamicContext);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            iterateChildren();
            // Always return null
            return ItemFactory.getInstance().createNullItem();
        }
        throw new IteratorFlowException("Invalid next() call in StatementsWithExpression", getMetadata());
    }
}
