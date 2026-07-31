package org.rumbledb.runtime.functions.sequences.general;


import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FoldLeftFunctionIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> computeResult(context).iterator(), getMetadata());
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimePlan<Item> sequenceIterator;
    private final RuntimePlan<Item> zeroIterator;
    private final RuntimePlan<Item> functionIterator;

    public FoldLeftFunctionIterator(
            List<RuntimePlan<Item>> arguments,
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

    private List<Item> computeResult(DynamicContext context) {
        List<Item> inputItems = this.sequenceIterator.materialize(context);
        List<Item> accumulator = this.zeroIterator.materialize(context);
        Item functionItem = this.functionIterator.materialize(context).get(0);

        ReusableFunctionCall reusableCall = null;

        for (Item inputItem : inputItems) {
            if (accumulator.size() == 1) {
                if (reusableCall == null) {
                    RuntimeStaticContext localItemStarContext = RuntimeStaticContext.builder()
                        .configuration(getConfiguration())
                        .staticType(SequenceType.createSequenceType("item*"))
                        .executionMode(ExecutionMode.LOCAL)
                        .metadata(getMetadata())
                        .build();
                    ConstantRuntimeIterator accumulatorArgument =
                        new ConstantRuntimeIterator(
                                accumulator.get(0),
                                localItemStarContext
                        );
                    ConstantRuntimeIterator currentItemArgument =
                        new ConstantRuntimeIterator(
                                inputItem,
                                localItemStarContext
                        );
                    RuntimePlan<Item> functionCall = NamedFunctions
                        .buildFunctionItemCallIterator(
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

        return accumulator;
    }

    private static final class ReusableFunctionCall {
        private final ConstantRuntimeIterator accumulatorArgument;
        private final ConstantRuntimeIterator currentItemArgument;
        private final RuntimePlan<Item> functionCall;

        private ReusableFunctionCall(
                ConstantRuntimeIterator accumulatorArgument,
                ConstantRuntimeIterator currentItemArgument,
                RuntimePlan<Item> functionCall
        ) {
            this.accumulatorArgument = accumulatorArgument;
            this.currentItemArgument = currentItemArgument;
            this.functionCall = functionCall;
        }
    }

    private RuntimePlan<Item> createSequenceIterator(List<Item> items) {
        RuntimeStaticContext localItemStarContext = RuntimeStaticContext.builder()
            .configuration(getConfiguration())
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(getMetadata())
            .build();
        if (items.isEmpty()) {
            return new CommaExpressionIterator(Collections.emptyList(), localItemStarContext);
        }

        List<RuntimePlan<Item>> childIterators = new ArrayList<>(
                items.size()
        );
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
        List<RuntimePlan<Item>> arguments = new ArrayList<>(2);
        arguments.add(createSequenceIterator(accumulator));
        arguments.add(createSequenceIterator(currentItemSequence));

        RuntimePlan<Item> functionCall = NamedFunctions
            .buildFunctionItemCallIterator(
                functionItem,
                this.staticContext,
                ExecutionMode.LOCAL,
                arguments,
                false
            );
        return functionCall.materialize(context);
    }
}
