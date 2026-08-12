package org.rumbledb.runtime.functions;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import lombok.Getter;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.functions.arrays.ArrayFunctionCallIterator;
import org.rumbledb.runtime.functions.maps.MapFunctionCallIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.types.SequenceType;

public class FunctionCoercionRuntimeIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item>, DataFrameRuntimePlan<Item>, NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private final Item callableItem;

    private final List<Name> parameterNames;
    private final SequenceType expectedReturnType;
    private final String exceptionMessage;

    public FunctionCoercionRuntimeIterator(
            Item callableItem,
            List<Name> parameterNames,
            SequenceType expectedReturnType,
            String exceptionMessage,
            RuntimeStaticContext staticContext) {
        super(List.of(), staticContext);
        this.callableItem = callableItem;
        this.parameterNames = parameterNames;
        this.expectedReturnType = expectedReturnType;
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return this.getPlan(context).getCursor(context);
    }

    public ExecutionMode getWrappedCallableExecutionMode() {
        return getWrappedCallableExecutionMode(this.callableItem);
    }

    private static ExecutionMode getWrappedCallableExecutionMode(Item callableItem) {
        if (!callableItem.isFunction()) {
            return ExecutionMode.LOCAL;
        }
        return callableItem.getBodyIterator().getRuntimeStaticContext().getExecutionMode();
    }

    private ItemRuntimePlan getPlan(DynamicContext context) {
        List<ItemRuntimePlan> arguments = new ArrayList<>(this.parameterNames.size());
        for (Name parameterName : this.parameterNames) {
            arguments.add(buildArgumentIterator(parameterName, context, this.staticContext));
        }

        ExecutionMode wrappedCallableExecutionMode = getWrappedCallableExecutionMode(this.callableItem);
        RuntimeStaticContext callStaticContext = this.staticContext.toBuilder()
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
                    "Function coercion can only wrap functions, maps, or arrays.", this.staticContext.getMetadata());
        }
        ItemRuntimePlan callIterator = NamedFunctions.buildFunctionItemCallIterator(
                this.callableItem, callStaticContext, wrappedCallableExecutionMode, arguments, false);
        return FunctionCallArgumentConversion.wrapForFunctionConversion(
                callIterator,
                this.expectedReturnType,
                this.exceptionMessage,
                callStaticContext.toBuilder()
                        .staticType(this.expectedReturnType)
                        .build());
    }

    private static ItemRuntimePlan buildArgumentIterator(
            Name parameterName, DynamicContext context, RuntimeStaticContext staticContext) {
        ExecutionMode parameterExecutionMode = ExecutionMode.LOCAL;
        if (context.getVariableValues().contains(parameterName)) {
            if (context.getVariableValues().isDataFrame(parameterName, staticContext.getMetadata())) {
                parameterExecutionMode = ExecutionMode.DATAFRAME;
            } else if (context.getVariableValues().isRDD(parameterName, staticContext.getMetadata())) {
                parameterExecutionMode = ExecutionMode.RDD;
            }
        }
        RuntimeStaticContext parameterStaticContext = staticContext.toBuilder()
                .staticType(SequenceType.createSequenceType("item*"))
                .executionMode(parameterExecutionMode)
                .build();
        return new VariableReferenceIterator(parameterName, parameterStaticContext);
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        ItemRuntimePlan call = this.getPlan(context);
        return call.getRDD(context);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        ItemRuntimePlan call = this.getPlan(dynamicContext);
        return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(call, dynamicContext);
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeClauseContext.NoNativeQuery;
    }
}
