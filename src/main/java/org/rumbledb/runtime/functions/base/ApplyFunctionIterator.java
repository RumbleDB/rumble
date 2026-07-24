package org.rumbledb.runtime.functions.base;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.EmptySequenceIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ApplyFunctionIterator extends HybridRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
    private RuntimeIterator delegate;
    private Item nextResult;

    public ApplyFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    protected void openLocal() {
        this.delegate = buildDelegate(this.currentDynamicContextForLocalExecution);
        this.delegate.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + "in fn:apply",
                    getMetadata()
            );
        }
        Item result = this.nextResult;
        setNextResult();
        return result;
    }

    @Override
    protected void closeLocal() {
        if (this.delegate != null && this.delegate.isOpen()) {
            this.delegate.close();
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        return buildDelegate(context).getRDD(context);
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        return buildDelegate(context).getDataFrame(context);
    }

    private RuntimeIterator buildDelegate(DynamicContext context) {
        Item functionItem;
        Item argumentsArray;
        try {
            functionItem = this.children.get(0).materializeAtMostOneItemOrNull(context);
            argumentsArray = this.children.get(1).materializeAtMostOneItemOrNull(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "fn:apply expects exactly one function item and exactly one array item.",
                    getMetadata()
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
                    getMetadata()
            );
        }

        List<RuntimeIterator> argumentIterators = new ArrayList<>();
        for (List<Item> memberSequence : argumentsArray.getSequenceMembers()) {
            argumentIterators.add(buildArgumentIterator(memberSequence, localItemStarContext));
        }

        RuntimeIterator functionItemIterator = new ConstantRuntimeIterator(
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

    private RuntimeIterator buildArgumentIterator(
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

    private void setNextResult() {
        this.nextResult = null;
        if (this.delegate.hasNext()) {
            this.nextResult = this.delegate.next();
        }
        if (this.nextResult == null) {
            this.hasNext = false;
            this.delegate.close();
        } else {
            this.hasNext = true;
        }
    }
}
