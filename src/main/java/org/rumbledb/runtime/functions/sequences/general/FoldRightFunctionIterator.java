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

public class FoldRightFunctionIterator extends AbstractItemRuntimePlan
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

    public FoldRightFunctionIterator(
            List<RuntimePlan<Item>> arguments,
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

    private List<Item> computeResult(DynamicContext context) {
        List<Item> inputItems = this.sequenceIterator.materialize(context);
        List<Item> accumulator = this.zeroIterator.materialize(context);
        Item functionItem = this.functionIterator.materialize(context).get(0);

        ReusableFunctionCall reusableCall = null;

        for (int i = inputItems.size() - 1; i >= 0; i--) {
            Item inputItem = inputItems.get(i);
            if (accumulator.size() == 1) {
                if (reusableCall == null) {
                    RuntimeStaticContext localItemStarContext = RuntimeStaticContext.builder()
                        .configuration(getConfiguration())
                        .staticType(SequenceType.createSequenceType("item*"))
                        .executionMode(ExecutionMode.LOCAL)
                        .metadata(getMetadata())
                        .build();
                    ConstantRuntimeIterator currentItemArgument =
                        new ConstantRuntimeIterator(
                                inputItem,
                                localItemStarContext
                        );
                    ConstantRuntimeIterator accumulatorArgument =
                        new ConstantRuntimeIterator(
                                accumulator.get(0),
                                localItemStarContext
                        );
                    RuntimePlan<Item> functionCall = NamedFunctions
                        .buildFunctionItemCallIterator(
                            functionItem,
                            this.staticContext,
                            ExecutionMode.LOCAL,
                            Arrays.asList(currentItemArgument, accumulatorArgument),
                            false
                        );
                    reusableCall = new ReusableFunctionCall(currentItemArgument, accumulatorArgument, functionCall);
                } else {
                    reusableCall.currentItemArgument.setItemForReuse(inputItem);
                    reusableCall.accumulatorArgument.setItemForReuse(accumulator.get(0));
                }
                accumulator = reusableCall.functionCall.materialize(context);
            } else {
                accumulator = applyFunction(
                    functionItem,
                    Collections.singletonList(inputItem),
                    accumulator,
                    context
                );
            }
        }

        return accumulator;
    }

    private static final class ReusableFunctionCall {
        private final ConstantRuntimeIterator currentItemArgument;
        private final ConstantRuntimeIterator accumulatorArgument;
        private final RuntimePlan<Item> functionCall;

        private ReusableFunctionCall(
                ConstantRuntimeIterator currentItemArgument,
                ConstantRuntimeIterator accumulatorArgument,
                RuntimePlan<Item> functionCall
        ) {
            this.currentItemArgument = currentItemArgument;
            this.accumulatorArgument = accumulatorArgument;
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
            List<Item> currentItemSequence,
            List<Item> accumulator,
            DynamicContext context
    ) {
        List<RuntimePlan<Item>> arguments = new ArrayList<>(2);
        arguments.add(createSequenceIterator(currentItemSequence));
        arguments.add(createSequenceIterator(accumulator));

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
