package org.rumbledb.runtime.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class PathExprIterator extends LocalRuntimeIterator {
    private final RuntimeIterator getRootIterator;
    private final List<RuntimeIterator> stepIterators;
    private List<Item> results = null;
    private int nextResultCounter = 0;
    private Item nextResult;

    public PathExprIterator(
            List<RuntimeIterator> stepIterators,
            RuntimeIterator getRootIterator,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.children.addAll(stepIterators);
        if (getRootIterator != null) {
            this.children.add(getRootIterator);
        }
        this.stepIterators = stepIterators;
        this.getRootIterator = getRootIterator;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        setNextResult();
    }

    @Override
    public void close() {
        super.close();
        this.hasNext = false;
        this.results = null;
        this.nextResult = null;
        this.nextResultCounter = 0;
        this.stepIterators.forEach(RuntimeIterator::close);
    }

    private void setNextResult() {
        if (this.results == null) {
            RuntimeIterator finalIterator = this.stepIterators.get(this.stepIterators.size() - 1);
            if (this.getRootIterator != null) {
                List<Item> root = this.getRootIterator.materialize(this.currentDynamicContextForLocalExecution);
                this.currentDynamicContextForLocalExecution.getVariableValues()
                    .addVariableValue(Name.CONTEXT_ITEM, root);
            }
            for (int i = 0; i < this.stepIterators.size() - 1; ++i) {
                // TODO: Verify that the type of each item is node
                List<Item> nextContext = this.stepIterators.get(i)
                    .materialize(this.currentDynamicContextForLocalExecution);
                this.currentDynamicContextForLocalExecution.getVariableValues()
                    .addVariableValue(Name.CONTEXT_ITEM, nextContext);
            }
            results = finalIterator.materialize(this.currentDynamicContextForLocalExecution);
        }
        if (this.nextResultCounter < this.results.size()) {
            this.nextResult = this.results.get(nextResultCounter++);
        } else {
            this.hasNext = false;
        }
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item nextResult = this.nextResult;
            setNextResult();
            return nextResult;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in Path Expression",
                getMetadata()
        );
    }

    @Override
    public boolean hasNext() {
        return super.hasNext();
    }
}
