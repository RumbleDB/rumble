/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.functions;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import lombok.Getter;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.Cursor;
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
        return this.getCall(context).getCursor();
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

    // The call of the wrapped item, whose result is converted to the expected return type.
    private FunctionCall getCall(DynamicContext context) {
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
            return new FunctionCall(
                    new ArrayFunctionCallIterator(this.callableItem, arguments.get(0), callStaticContext), context);
        }
        if (this.callableItem.isMap()) {
            return new FunctionCall(
                    new MapFunctionCallIterator(this.callableItem, arguments.get(0), callStaticContext), context);
        }
        if (!this.callableItem.isFunction()) {
            throw new OurBadException(
                    "Function coercion can only wrap functions, maps, or arrays.", this.staticContext.getMetadata());
        }
        FunctionCall call = FunctionCall.of(this.callableItem, arguments, callStaticContext, context);
        // The conversion only passes its context on to the call, so it is evaluated in the context of the call.
        ItemRuntimePlan convertedResult = FunctionCallArgumentConversion.wrapForFunctionConversion(
                call.plan(),
                this.expectedReturnType,
                this.exceptionMessage,
                callStaticContext.toBuilder()
                        .staticType(this.expectedReturnType)
                        .build());
        return new FunctionCall(convertedResult, call.context());
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
        return this.getCall(context).getRDD();
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        return this.getCall(dynamicContext).getDataFrame();
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeClauseContext.NoNativeQuery;
    }
}
