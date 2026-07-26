package org.rumbledb.runtime.functions;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.functions.arrays.ArrayFunctionCallIterator;
import org.rumbledb.runtime.functions.maps.MapFunctionCallIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class FunctionCoercionRuntimeIterator extends HybridRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Item callableItem;
    private final List<Name> parameterNames;
    private final SequenceType expectedReturnType;
    private final String exceptionMessage;

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

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new CoercionLocalCursor(this, context);
    }

    private static final class CoercionLocalCursor extends AbstractLocalCursor<Item> {
        private final FunctionCoercionRuntimeIterator plan;
        private final DynamicContext context;
        private LocalCursor<Item> delegate;

        private CoercionLocalCursor(FunctionCoercionRuntimeIterator plan, DynamicContext context) {
            super(plan.getMetadata());
            this.plan = plan;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.delegate = this.plan.buildDelegate(this.context).createLocalCursor(this.context);
        }

        @Override
        protected boolean hasNextLocal() {
            return this.delegate.hasNext();
        }

        @Override
        protected Item nextLocal() {
            return this.delegate.next();
        }

        @Override
        protected void closeLocal() {
            if (this.delegate != null) {
                this.delegate.close();
                this.delegate = null;
            }
        }
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

    private RuntimeIterator buildDelegate(DynamicContext context) {
        List<RuntimeIterator> arguments = new ArrayList<>(this.parameterNames.size());
        for (Name parameterName : this.parameterNames) {
            arguments.add(buildArgumentIterator(parameterName, context));
        }

        ExecutionMode wrappedCallableExecutionMode = getWrappedCallableExecutionMode();
        RuntimeStaticContext callStaticContext = getRuntimeStaticContext()
            .toBuilder()
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(wrappedCallableExecutionMode)
            .build();

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
        return FunctionCallArgumentConversion.wrapForFunctionConversion(
            callIterator,
            this.expectedReturnType,
            this.exceptionMessage,
            callStaticContext.toBuilder().staticType(this.expectedReturnType).build()
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
            .toBuilder()
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(parameterExecutionMode)
            .build();
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
