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
import org.rumbledb.runtime.cursor.AbstractDelegatingLocalCursor;
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
        return new CoercionLocalCursor(
                this.callableItem,
                this.parameterNames,
                this.expectedReturnType,
                this.exceptionMessage,
                getRuntimeStaticContext(),
                context
        );
    }

    private static final class CoercionLocalCursor extends AbstractDelegatingLocalCursor<Item> {
        private final Item callableItem;
        private final List<Name> parameterNames;
        private final SequenceType expectedReturnType;
        private final String exceptionMessage;
        private final RuntimeStaticContext staticContext;
        private final DynamicContext context;

        private CoercionLocalCursor(
                Item callableItem,
                List<Name> parameterNames,
                SequenceType expectedReturnType,
                String exceptionMessage,
                RuntimeStaticContext staticContext,
                DynamicContext context
        ) {
            super(staticContext.getMetadata());
            this.callableItem = callableItem;
            this.parameterNames = parameterNames;
            this.expectedReturnType = expectedReturnType;
            this.exceptionMessage = exceptionMessage;
            this.staticContext = staticContext;
            this.context = context;
        }

        @Override
        protected LocalCursor<Item> createDelegateCursor() {
            return buildDelegate(
                this.callableItem,
                this.parameterNames,
                this.expectedReturnType,
                this.exceptionMessage,
                this.staticContext,
                this.context
            ).createLocalCursor(this.context);
        }
    }

    public Item getCallableItem() {
        return this.callableItem;
    }

    public ExecutionMode getWrappedCallableExecutionMode() {
        return getWrappedCallableExecutionMode(this.callableItem);
    }

    private static ExecutionMode getWrappedCallableExecutionMode(Item callableItem) {
        if (!callableItem.isFunction()) {
            return ExecutionMode.LOCAL;
        }
        return callableItem.getBodyIterator().getHighestExecutionMode();
    }

    private RuntimeIterator buildDelegate(DynamicContext context) {
        return buildDelegate(
            this.callableItem,
            this.parameterNames,
            this.expectedReturnType,
            this.exceptionMessage,
            getRuntimeStaticContext(),
            context
        );
    }

    private static RuntimeIterator buildDelegate(
            Item callableItem,
            List<Name> parameterNames,
            SequenceType expectedReturnType,
            String exceptionMessage,
            RuntimeStaticContext staticContext,
            DynamicContext context
    ) {
        List<RuntimeIterator> arguments = new ArrayList<>(parameterNames.size());
        for (Name parameterName : parameterNames) {
            arguments.add(buildArgumentIterator(parameterName, context, staticContext));
        }

        ExecutionMode wrappedCallableExecutionMode = getWrappedCallableExecutionMode(callableItem);
        RuntimeStaticContext callStaticContext = staticContext
            .toBuilder()
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(wrappedCallableExecutionMode)
            .build();

        if (callableItem.isArray()) {
            return new ArrayFunctionCallIterator(callableItem, arguments.get(0), callStaticContext);
        }
        if (callableItem.isMap()) {
            return new MapFunctionCallIterator(callableItem, arguments.get(0), callStaticContext);
        }
        if (!callableItem.isFunction()) {
            throw new OurBadException(
                    "Function coercion can only wrap functions, maps, or arrays.",
                    staticContext.getMetadata()
            );
        }
        RuntimeIterator callIterator = NamedFunctions.buildFunctionItemCallIterator(
            callableItem,
            callStaticContext,
            wrappedCallableExecutionMode,
            arguments,
            false
        );
        return FunctionCallArgumentConversion.wrapForFunctionConversion(
            callIterator,
            expectedReturnType,
            exceptionMessage,
            callStaticContext.toBuilder().staticType(expectedReturnType).build()
        );
    }

    private static RuntimeIterator buildArgumentIterator(
            Name parameterName,
            DynamicContext context,
            RuntimeStaticContext staticContext
    ) {
        ExecutionMode parameterExecutionMode = ExecutionMode.LOCAL;
        if (context.getVariableValues().contains(parameterName)) {
            if (context.getVariableValues().isDataFrame(parameterName, staticContext.getMetadata())) {
                parameterExecutionMode = ExecutionMode.DATAFRAME;
            } else if (context.getVariableValues().isRDD(parameterName, staticContext.getMetadata())) {
                parameterExecutionMode = ExecutionMode.RDD;
            }
        }
        RuntimeStaticContext parameterStaticContext = staticContext
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
    public JSoundDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        RuntimeIterator call = buildDelegate(dynamicContext);
        return call.getDataFrame(dynamicContext);
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeClauseContext.NoNativeQuery;
    }
}
