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
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.functions.arrays.ArrayFunctionCallIterator;
import org.rumbledb.runtime.functions.maps.MapFunctionCallIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public class FunctionCoercionRuntimeIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final Item callableItem;
    private final List<Name> parameterNames;
    private final SequenceType expectedReturnType;
    private final String exceptionMessage;
    private RuntimeIterator delegate;
    private Item nextResult;

    public FunctionCoercionRuntimeIterator(
            Item callableItem,
            List<Name> parameterNames,
            SequenceType expectedReturnType,
            String exceptionMessage,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.callableItem = callableItem;
        this.parameterNames = parameterNames;
        this.expectedReturnType = expectedReturnType;
        this.exceptionMessage = exceptionMessage;
    }

    public Item getCallableItem() {
        return this.callableItem;
    }

    public ExecutionMode getWrappedCallableExecutionMode() {
        if (!this.callableItem.isFunction()) {
            return ExecutionMode.LOCAL;
        }
        return this.callableItem.getBodyIterator().getHighestExecutionMode();
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
        for (Name parameterName : this.parameterNames) {
            arguments.add(buildArgumentIterator(parameterName, context));
        }

        ExecutionMode wrappedCallableExecutionMode = getWrappedCallableExecutionMode();
        RuntimeStaticContext callStaticContext = getRuntimeStaticContext()
            .withStaticType(SequenceType.createSequenceType("item*"))
            .withExecutionMode(wrappedCallableExecutionMode);

        if (this.callableItem.isArray()) {
            return new ArrayFunctionCallIterator(this.callableItem, arguments.get(0), callStaticContext);
        }
        if (this.callableItem.isMap()) {
            return new MapFunctionCallIterator(this.callableItem, arguments.get(0), callStaticContext);
        }
        if (!this.callableItem.isFunction()) {
            throw new OurBadException(
                    "Function coercion can only wrap functions, maps, or arrays.",
                    getMetadata()
            );
        }
        RuntimeIterator callIterator = NamedFunctions.buildFunctionItemCallIterator(
            this.callableItem,
            callStaticContext,
            wrappedCallableExecutionMode,
            arguments,
            false
        );
        return FunctionCallArgumentCoercion.wrapForFunctionConversion(
            callIterator,
            this.expectedReturnType,
            this.exceptionMessage,
            callStaticContext.withStaticType(this.expectedReturnType)
        );
    }

    private RuntimeIterator buildArgumentIterator(Name parameterName, DynamicContext context) {
        ExecutionMode parameterExecutionMode = ExecutionMode.LOCAL;
        if (context.getVariableValues().contains(parameterName)) {
            if (context.getVariableValues().isDataFrame(parameterName, getMetadata())) {
                parameterExecutionMode = ExecutionMode.DATAFRAME;
            } else if (context.getVariableValues().isRDD(parameterName, getMetadata())) {
                parameterExecutionMode = ExecutionMode.RDD;
            }
        }
        RuntimeStaticContext parameterStaticContext = getRuntimeStaticContext()
            .withStaticType(SequenceType.createSequenceType("item*"))
            .withExecutionMode(parameterExecutionMode);
        return new VariableReferenceIterator(parameterName, parameterStaticContext);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        RuntimeIterator call = buildDelegate(context);
        return call.getRDD(context);
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        RuntimeIterator call = buildDelegate(dynamicContext);
        return call.getDataFrame(dynamicContext);
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeClauseContext.NoNativeQuery;
    }
}
