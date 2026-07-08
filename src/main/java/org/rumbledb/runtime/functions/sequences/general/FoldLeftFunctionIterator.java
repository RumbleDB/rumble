package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FoldLeftFunctionIterator extends HybridRuntimeIterator {
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator sequenceIterator;
    private final RuntimeIterator zeroIterator;
    private final RuntimeIterator functionIterator;

    private List<Item> resultSequence;
    private int resultIndex;

    public FoldLeftFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("fn:fold-left must have exactly three arguments.");
        }
        this.sequenceIterator = arguments.get(0);
        this.zeroIterator = arguments.get(1);
        this.functionIterator = arguments.get(2);
    }

    @Override
    protected void openLocal() {
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.resultIndex = 0;
        this.hasNext = this.resultSequence != null && !this.resultSequence.isEmpty();
    }

    private void initializeResult(DynamicContext context) {
        List<Item> inputItems = this.sequenceIterator.materialize(context);
        List<Item> accumulator = this.zeroIterator.materialize(context);
        Item functionItem = this.functionIterator.materialize(context).get(0);

        RuntimeStaticContext localItemStarContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item*"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        MaterializedItemsRuntimeIterator accumulatorArgument = new MaterializedItemsRuntimeIterator(
                localItemStarContext
        );
        MaterializedItemsRuntimeIterator currentItemArgument = new MaterializedItemsRuntimeIterator(
                localItemStarContext
        );
        RuntimeIterator functionCall = NamedFunctions.buildFunctionItemCallIterator(
            functionItem,
            this.staticContext,
            ExecutionMode.LOCAL,
            Arrays.asList(accumulatorArgument, currentItemArgument),
            false
        );

        for (Item inputItem : inputItems) {
            accumulatorArgument.setItems(accumulator);
            currentItemArgument.setItems(Collections.singletonList(inputItem));
            accumulator = functionCall.materialize(context);
        }

        this.resultSequence = accumulator;
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
        Item result = this.resultSequence.get(this.resultIndex++);
        if (this.resultIndex >= this.resultSequence.size()) {
            this.hasNext = false;
        }
        return result;
    }

    @Override
    protected void resetLocal() {
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.resultIndex = 0;
        this.hasNext = this.resultSequence != null && !this.resultSequence.isEmpty();
    }

    @Override
    protected void closeLocal() {
        this.resultSequence = null;
        this.resultIndex = 0;
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("fn:fold-left is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:fold-left is currently supported only in local execution mode.");
    }
}
