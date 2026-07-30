package org.rumbledb.runtime.functions.base;

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
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractDelegatingLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ApplyFunctionIterator extends HybridRuntimeIterator implements DataFrameRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ApplyLocalCursor(this.getChild(0), this.getChild(1), this.staticContext, context);
    }

    private static final class ApplyLocalCursor extends AbstractDelegatingLocalCursor<Item> {
        private final RuntimeIterator functionPlan;
        private final RuntimeIterator argumentsPlan;
        private final RuntimeStaticContext staticContext;
        private final DynamicContext context;

        private ApplyLocalCursor(
                RuntimeIterator functionPlan,
                RuntimeIterator argumentsPlan,
                RuntimeStaticContext staticContext,
                DynamicContext context
        ) {
            super(staticContext.getMetadata());
            this.functionPlan = functionPlan;
            this.argumentsPlan = argumentsPlan;
            this.staticContext = staticContext;
            this.context = context;
        }

        @Override
        protected Cursor<Item> createDelegateCursor() {
            return buildDelegate(
                this.functionPlan,
                this.argumentsPlan,
                this.staticContext,
                this.context
            ).getCursor(this.context);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    public ApplyFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        return buildDelegate(context).getRDD(context);
    }

    @Override
    public HomogeneousItemDataFrame getNativeDataFrame(DynamicContext context) {
        return buildDelegate(context).getDataFrame(context);
    }

    private RuntimeIterator buildDelegate(DynamicContext context) {
        return buildDelegate(this.getChild(0), this.getChild(1), this.staticContext, context);
    }

    private static RuntimeIterator buildDelegate(
            RuntimeIterator functionPlan,
            RuntimeIterator argumentsPlan,
            RuntimeStaticContext staticContext,
            DynamicContext context
    ) {
        Item functionItem;
        Item argumentsArray;
        try {
            functionItem = functionPlan.materializeAtMostOne(context);
            argumentsArray = argumentsPlan.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "fn:apply expects exactly one function item and exactly one array item.",
                    staticContext.getMetadata()
            );
        }
        RuntimeStaticContext localItemStarContext = staticContext
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
                    staticContext.getMetadata()
            );
        }

        List<RuntimeIterator> argumentIterators = new ArrayList<>();
        for (List<Item> memberSequence : argumentsArray.getSequenceMembers()) {
            argumentIterators.add(buildArgumentIterator(memberSequence, localItemStarContext));
        }

        RuntimeIterator functionItemIterator = new ConstantRuntimeIterator(
                functionItem,
                staticContext
                    .toBuilder()
                    .staticType(SequenceType.createSequenceType("function(*)"))
                    .executionMode(ExecutionMode.LOCAL)
                    .build()
        );
        return new DynamicFunctionCallIterator(
                functionItemIterator,
                argumentIterators,
                staticContext
        );
    }

    private static RuntimeIterator buildArgumentIterator(
            List<Item> memberSequence,
            RuntimeStaticContext localItemStarContext
    ) {
        if (memberSequence.isEmpty()) {
            return new EmptySequenceIterator(localItemStarContext);
        }
        if (memberSequence.size() == 1) {
            return new ConstantRuntimeIterator(memberSequence.get(0), localItemStarContext);
        }
        List<RuntimeIterator> sequenceItems = new ArrayList<>(memberSequence.size());
        for (Item item : memberSequence) {
            sequenceItems.add(new ConstantRuntimeIterator(item, localItemStarContext));
        }
        return new CommaExpressionIterator(sequenceItems, localItemStarContext);
    }

}
