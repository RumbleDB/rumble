package org.rumbledb.runtime.functions;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.EmptySequenceIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.functions.arrays.ArrayFunctionCallIterator;
import org.rumbledb.runtime.functions.maps.MapFunctionCallIterator;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public class FunctionCoercionRuntimeIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final Item callableItem;
    private final List<Name> parameterNames;
    private RuntimeIterator delegate;
    private Item nextResult;

    public FunctionCoercionRuntimeIterator(
            Item callableItem,
            List<Name> parameterNames,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.callableItem = callableItem;
        this.parameterNames = parameterNames;
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
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        Item result = this.nextResult;
        setNextResult();
        return result;
    }

    @Override
    protected void resetLocal() {
        if (this.delegate != null) {
            this.delegate.reset(this.currentDynamicContextForLocalExecution);
        } else {
            this.delegate = buildDelegate(this.currentDynamicContextForLocalExecution);
            this.delegate.open(this.currentDynamicContextForLocalExecution);
        }
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        if (this.delegate != null && this.delegate.isOpen()) {
            this.delegate.close();
        }
    }

    private void setNextResult() {
        this.nextResult = null;
        if (this.delegate.hasNext()) {
            this.nextResult = this.delegate.next();
        }
        this.hasNext = this.nextResult != null;
    }

    private RuntimeIterator buildDelegate(DynamicContext context) {
        List<RuntimeIterator> arguments = new ArrayList<>(this.parameterNames.size());
        RuntimeStaticContext localItemStarContext = getRuntimeStaticContext()
            .withStaticType(SequenceType.createSequenceType("item*"))
            .withExecutionMode(ExecutionMode.LOCAL);
        for (Name parameterName : this.parameterNames) {
            List<Item> argumentItems = context.getVariableValues().getLocalVariableValue(parameterName, getMetadata());
            arguments.add(buildArgumentIterator(argumentItems, localItemStarContext));
        }

        if (this.callableItem.isArray()) {
            return new ArrayFunctionCallIterator(this.callableItem, arguments.get(0), localItemStarContext);
        }
        if (this.callableItem.isMap()) {
            return new MapFunctionCallIterator(this.callableItem, arguments.get(0), localItemStarContext);
        }
        if (!this.callableItem.isFunction()) {
            throw new OurBadException(
                    "Function coercion can only wrap functions, maps, or arrays.",
                    getMetadata()
            );
        }
        return NamedFunctions.buildFunctionItemCallIterator(
            this.callableItem,
            getRuntimeStaticContext(),
            ExecutionMode.LOCAL,
            arguments,
            false
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

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("Function coercion is currently supported only in local execution mode.");
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("Function coercion is currently supported only in local execution mode.");
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeClauseContext.NoNativeQuery;
    }
}
