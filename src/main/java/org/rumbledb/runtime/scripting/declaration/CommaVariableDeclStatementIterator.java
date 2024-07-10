package org.rumbledb.runtime.scripting.declaration;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

/*
 * It is expected that no results are returned for this iterator.
 */
public class CommaVariableDeclStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    private RuntimeIterator currentChild;
    private int childIndex;

    public CommaVariableDeclStatementIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        this.childIndex = 0;

        if (!this.children.isEmpty()) {
            this.currentChild = this.children.get(this.childIndex);
            this.currentChild.open(this.currentDynamicContextForLocalExecution);
            materializeChildren();
        } else {
            this.currentChild = null;
        }
        return null;
    }

    public void materializeChildren() {
        while (this.currentChild != null) {
            if (this.currentChild.hasNext()) {
                this.currentChild.next();
            } else {
                this.currentChild.close();
                if (++this.childIndex == this.children.size()) {
                    this.currentChild = null;
                } else {
                    this.currentChild = this.children.get(this.childIndex);
                    this.currentChild.open(this.currentDynamicContextForLocalExecution);
                }
            }
        }
        this.hasNext = false;
    }
}
