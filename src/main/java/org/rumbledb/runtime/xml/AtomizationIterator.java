package org.rumbledb.runtime.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.xml.AttributeItem;
import org.rumbledb.items.xml.DocumentItem;
import org.rumbledb.items.xml.ElementItem;
import org.rumbledb.items.xml.TextItem;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.Collections;
import java.util.List;

public class AtomizationIterator extends LocalRuntimeIterator {
    private RuntimeIterator pathExpr;
    private List<Item> result;
    private int currentResultCounter;
    private Item nextResult;

    public AtomizationIterator(RuntimeIterator pathExpr, RuntimeStaticContext staticContext) {
        super(Collections.singletonList(pathExpr), staticContext);
        this.pathExpr = pathExpr;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.currentResultCounter = 0;
        setNextResult();
    }

    private void setNextResult() {
        if (this.result == null) {
            this.result = this.pathExpr.materialize(this.currentDynamicContextForLocalExecution);
            for (int i = 0; i < this.result.size(); ++i) {
                if (
                    this.result.get(i) instanceof AttributeItem
                        || this.result.get(i) instanceof ElementItem
                        || this.result.get(i) instanceof TextItem
                        || this.result.get(i) instanceof DocumentItem
                ) {
                    this.result.set(i, this.result.get(i).typedValue());
                }
            }
        }
        if (this.currentResultCounter < this.result.size()) {
            this.nextResult = this.result.get(this.currentResultCounter++);
        } else {
            this.hasNext = false;
        }
    }

    @Override
    public void close() {
        super.close();
        this.pathExpr.close();
        this.result = null;
        this.hasNext = false;
        this.currentResultCounter = 0;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item result = this.nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in AtomizationIterator",
                getMetadata()
        );
    }
}
