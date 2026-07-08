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

public class FoldRightFunctionIterator extends HybridRuntimeIterator {
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator sequenceIterator;
    private final RuntimeIterator zeroIterator;
    private final RuntimeIterator functionIterator;

    private List<Item> resultSequence;
    private int resultIndex;

    public FoldRightFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("fn:fold-right must have exactly three arguments.");
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
        MaterializedItemsRuntimeIterator currentItemArgument = new MaterializedItemsRuntimeIterator(
                localItemStarContext
        );
        MaterializedItemsRuntimeIterator accumulatorArgument = new MaterializedItemsRuntimeIterator(
                localItemStarContext
        );
        RuntimeIterator functionCall = NamedFunctions.buildFunctionItemCallIterator(
            functionItem,
            this.staticContext,
            ExecutionMode.LOCAL,
            Arrays.asList(currentItemArgument, accumulatorArgument),
            false
        );

        for (int i = inputItems.size() - 1; i >= 0; i--) {
            currentItemArgument.setItems(Collections.singletonList(inputItems.get(i)));
            accumulatorArgument.setItems(accumulator);
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
        throw new OurBadException("fn:fold-right is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:fold-right is currently supported only in local execution mode.");
    }
}
