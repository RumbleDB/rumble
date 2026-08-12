package org.rumbledb.runtime.functions.base;

import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.EmptySequenceIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ApplyFunctionIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    public ApplyFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return this.buildPlan(context).getCursor(context);
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        return this.buildPlan(context).getRDD(context);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(
            this.buildPlan(context),
            context
        );
    }

    private ItemRuntimePlan buildPlan(DynamicContext context) {
        ItemRuntimePlan functionPlan = this.getChild(0);
        ItemRuntimePlan argumentsPlan = this.getChild(1);

        Item functionItem;
        Item argumentsArray;
        try {
            functionItem = functionPlan.materializeAtMostOne(context);
            argumentsArray = argumentsPlan.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "fn:apply expects exactly one function item and exactly one array item.",
                    this.staticContext.getMetadata()
            );
        }
        RuntimeStaticContext localItemStarContext = this.staticContext
            .toBuilder()
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .build();

        if (functionItem.getParameterNames().size() != argumentsArray.getSize()) {
            throw new RumbleException(
                    "fn:apply called with a function of arity "
                        + functionItem.getParameterNames().size()
                        + " and an array of size "
                        + argumentsArray.getSize()
                        + ".",
                    ErrorCode.ApplyFunctionArityMismatch,
                    this.staticContext.getMetadata()
            );
        }

        List<ItemRuntimePlan> argumentIterators = new ArrayList<>();
        for (List<Item> memberSequence : argumentsArray.getSequenceMembers()) {
            argumentIterators.add(buildArgumentIterator(memberSequence, localItemStarContext));
        }

        ItemRuntimePlan functionItemIterator = new ConstantRuntimeIterator(
                functionItem,
                this.staticContext
                    .toBuilder()
                    .staticType(SequenceType.createSequenceType("function(*)"))
                    .executionMode(ExecutionMode.LOCAL)
                    .build()
        );
        return new DynamicFunctionCallIterator(
                functionItemIterator,
                argumentIterators,
                this.staticContext
        );
    }

    private static ItemRuntimePlan buildArgumentIterator(
            List<Item> memberSequence,
            RuntimeStaticContext localItemStarContext
    ) {
        if (memberSequence.isEmpty()) {
            return new EmptySequenceIterator(localItemStarContext);
        }
        if (memberSequence.size() == 1) {
            return new ConstantRuntimeIterator(memberSequence.get(0), localItemStarContext);
        }
        List<ItemRuntimePlan> sequenceItems = new ArrayList<>(
                memberSequence.size()
        );
        for (Item item : memberSequence) {
            sequenceItems.add(new ConstantRuntimeIterator(item, localItemStarContext));
        }
        return new CommaExpressionIterator(sequenceItems, localItemStarContext);
    }

}
