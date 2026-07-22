package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class FilterFunctionIterator extends HybridRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator sequenceIterator;
    private final RuntimeIterator predicateIterator;
    private List<Item> inputItems;
    private Item predicate;
    private Item nextResult;
    private int itemIndex;
    private RuntimeStaticContext argumentContext;
    private MutableArgumentIterator mutableArgumentIterator;
    private RuntimeIterator currentCallbackIterator;

    public FilterFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("fn:filter must have exactly two arguments.");
        }
        this.sequenceIterator = arguments.get(0);
        this.predicateIterator = arguments.get(1);
    }

    @Override
    protected void openLocal() {
        initializeState(this.currentDynamicContextForLocalExecution);
        setNextResult(this.currentDynamicContextForLocalExecution);
    }

    private void initializeState(DynamicContext context) {
        this.inputItems = this.sequenceIterator.materialize(context);

        List<Item> predicateItems = this.predicateIterator.materialize(context);
        if (predicateItems.size() != 1) {
            throw new UnexpectedTypeException(
                    "The second argument of fn:filter must be a single function item [err:XPTY0004].",
                    getMetadata()
            );
        }
        this.predicate = predicateItems.get(0);
        if (!acceptsSingleArgument(this.predicate)) {
            throw new UnexpectedTypeException(
                    "The function passed to fn:filter must accept exactly one argument [err:XPTY0004].",
                    getMetadata()
            );
        }

        this.argumentContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        this.itemIndex = 0;
        this.mutableArgumentIterator = new MutableArgumentIterator(this.argumentContext);
        List<RuntimeIterator> callbackArguments = new ArrayList<>(1);
        callbackArguments.add(this.mutableArgumentIterator);
        RuntimeStaticContext functionItemContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item*"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        this.currentCallbackIterator = new DynamicFunctionCallIterator(
                new ConstantRuntimeIterator(this.predicate, functionItemContext),
                callbackArguments,
                functionItemContext
        );
    }

    private void setNextResult(DynamicContext context) {
        this.nextResult = null;
        while (this.inputItems != null && this.itemIndex < this.inputItems.size()) {
            Item candidate = this.inputItems.get(this.itemIndex++);
            this.mutableArgumentIterator.setCurrentItem(candidate);
            if (this.currentCallbackIterator.isOpen()) {
                this.currentCallbackIterator.close();
            }
            if (this.currentCallbackIterator.getEffectiveBooleanValue(context)) {
                this.nextResult = candidate;
                this.hasNext = true;
                return;
            }
        }
        this.hasNext = false;
    }

    private static boolean acceptsSingleArgument(Item item) {
        if (item.isMap() || item.isArray()) {
            return true;
        }
        return item.isFunction() && item.getIdentifier().getArity() == 1;
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        Item result = this.nextResult;
        setNextResult(this.currentDynamicContextForLocalExecution);
        return result;
    }

    @Override
    protected void resetLocal() {
        if (this.currentCallbackIterator != null && this.currentCallbackIterator.isOpen()) {
            this.currentCallbackIterator.close();
        }
        this.sequenceIterator.reset(this.currentDynamicContextForLocalExecution);
        this.predicateIterator.reset(this.currentDynamicContextForLocalExecution);
        initializeState(this.currentDynamicContextForLocalExecution);
        setNextResult(this.currentDynamicContextForLocalExecution);
    }

    @Override
    protected void closeLocal() {
        if (this.sequenceIterator.isOpen()) {
            this.sequenceIterator.close();
        }
        if (this.predicateIterator.isOpen()) {
            this.predicateIterator.close();
        }
        if (this.currentCallbackIterator != null && this.currentCallbackIterator.isOpen()) {
            this.currentCallbackIterator.close();
        }
        this.inputItems = null;
        this.predicate = null;
        this.nextResult = null;
        this.itemIndex = 0;
        this.argumentContext = null;
        this.mutableArgumentIterator = null;
        this.currentCallbackIterator = null;
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("fn:filter is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:filter is currently supported only in local execution mode.");
    }

    private static class MutableArgumentIterator extends AtMostOneItemLocalRuntimeIterator {
        @Serial
        private static final long serialVersionUID = 1L;
        private Item currentItem;

        MutableArgumentIterator(RuntimeStaticContext staticContext) {
            super(null, staticContext);
        }

        void setCurrentItem(Item item) {
            this.currentItem = item;
        }

        @Override
        public Item materializeFirstItemOrNull(DynamicContext context) {
            return this.currentItem;
        }
    }
}
