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
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FoldLeftFunctionIterator extends HybridRuntimeIterator {

    @Serial
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

        ReusableFunctionCall reusableCall = null;

        for (Item inputItem : inputItems) {
            if (accumulator.size() == 1) {
                if (reusableCall == null) {
                    RuntimeStaticContext localItemStarContext = new RuntimeStaticContext(
                            getConfiguration(),
                            SequenceType.createSequenceType("item*"),
                            ExecutionMode.LOCAL,
                            getMetadata()
                    );
                    ConstantRuntimeIterator accumulatorArgument = new ConstantRuntimeIterator(
                            accumulator.get(0),
                            localItemStarContext
                    );
                    ConstantRuntimeIterator currentItemArgument = new ConstantRuntimeIterator(
                            inputItem,
                            localItemStarContext
                    );
                    RuntimeIterator functionCall = NamedFunctions.buildFunctionItemCallIterator(
                        functionItem,
                        this.staticContext,
                        ExecutionMode.LOCAL,
                        Arrays.asList(accumulatorArgument, currentItemArgument),
                        false
                    );
                    reusableCall = new ReusableFunctionCall(accumulatorArgument, currentItemArgument, functionCall);
                } else {
                    reusableCall.accumulatorArgument.setItemForReuse(accumulator.get(0));
                    reusableCall.currentItemArgument.setItemForReuse(inputItem);
                }
                accumulator = reusableCall.functionCall.materialize(context);
            } else {
                accumulator = applyFunction(functionItem, accumulator, Collections.singletonList(inputItem), context);
            }
        }

        this.resultSequence = accumulator;
    }

    private static final class ReusableFunctionCall {
        private final ConstantRuntimeIterator accumulatorArgument;
        private final ConstantRuntimeIterator currentItemArgument;
        private final RuntimeIterator functionCall;

        private ReusableFunctionCall(
                ConstantRuntimeIterator accumulatorArgument,
                ConstantRuntimeIterator currentItemArgument,
                RuntimeIterator functionCall
        ) {
            this.accumulatorArgument = accumulatorArgument;
            this.currentItemArgument = currentItemArgument;
            this.functionCall = functionCall;
        }
    }

    private RuntimeIterator createSequenceIterator(List<Item> items) {
        RuntimeStaticContext localItemStarContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item*"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        if (items.isEmpty()) {
            return new CommaExpressionIterator(Collections.emptyList(), localItemStarContext);
        }

        List<RuntimeIterator> childIterators = new ArrayList<>(items.size());
        for (Item item : items) {
            childIterators.add(new ConstantRuntimeIterator(item, localItemStarContext));
        }
        return new CommaExpressionIterator(childIterators, localItemStarContext);
    }

    private List<Item> applyFunction(
            Item functionItem,
            List<Item> accumulator,
            List<Item> currentItemSequence,
            DynamicContext context
    ) {
        List<RuntimeIterator> arguments = new ArrayList<>(2);
        arguments.add(createSequenceIterator(accumulator));
        arguments.add(createSequenceIterator(currentItemSequence));

        RuntimeIterator functionCall = NamedFunctions.buildFunctionItemCallIterator(
            functionItem,
            this.staticContext,
            ExecutionMode.LOCAL,
            arguments,
            false
        );
        return functionCall.materialize(context);
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
