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
import java.util.List;
import java.util.stream.Stream;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.arrays.ArrayFunctionCallIterator;
import org.rumbledb.runtime.functions.maps.MapFunctionCallIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.types.SequenceType;

public class DynamicFunctionCallIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item>, DataFrameRuntimePlan<Item> {
    // dynamic: functionIdentifier is not known at compile time
    // it is known only after evaluating postfix expression at runtime

    @Serial
    private static final long serialVersionUID = 1L;
    // parametrized fields
    private final ItemRuntimePlan functionItemIterator;
    private final List<? extends ItemRuntimePlan> functionArguments;
    private final boolean isPartialApplication;

    public DynamicFunctionCallIterator(
            ItemRuntimePlan functionItemIterator,
            List<? extends ItemRuntimePlan> functionArguments,
            RuntimeStaticContext staticContext) {
        super(
                Stream.concat(
                                functionArguments.stream().filter(arg -> arg != null),
                                functionArguments.contains(functionItemIterator)
                                        ? Stream.empty()
                                        : Stream.of(functionItemIterator))
                        .toList(),
                staticContext);

        this.isPartialApplication = functionArguments.stream().anyMatch(arg -> arg == null);
        this.functionItemIterator = functionItemIterator;
        this.functionArguments = functionArguments;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        // ML functions raise invalid parameters when they build their result, that is, when the cursor is created.
        try {
            return resolveFunctionCall(context).getCursor();
        } catch (InvalidRumbleMLParamException e) {
            throw new InvalidRumbleMLParamException(e.getMLMessage(), getMetadata());
        }
    }

    private FunctionCall resolveFunctionCall(DynamicContext context) {
        return resolveFunctionCall(
                this.functionItemIterator,
                this.functionArguments,
                this.isPartialApplication,
                this.staticContext.getExecutionMode(),
                this.staticContext,
                context);
    }

    private static FunctionCall resolveFunctionCall(
            ItemRuntimePlan functionItemPlan,
            List<? extends ItemRuntimePlan> functionArguments,
            boolean partialApplication,
            ExecutionMode callerExecutionMode,
            RuntimeStaticContext staticContext,
            DynamicContext context) {
        Item functionItem;
        try {
            functionItem = functionItemPlan.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "A dynamic function call can not be performed on a sequence of more than one item.",
                    staticContext.getMetadata());
        }
        if (functionItem == null) {
            throw new UnexpectedTypeException(
                    "Dynamic function calls can only be performed on functions, arrays, or maps.",
                    staticContext.getMetadata());
        }
        if (functionItem.isArray()) {
            if (partialApplication) {
                throw new UnexpectedTypeException(
                        "Partial application is not supported when calling arrays as functions.",
                        staticContext.getMetadata());
            }
            if (functionArguments.size() != 1 || functionArguments.get(0) == null) {
                throw new UnexpectedTypeException(
                        "Array function calls must have exactly one argument.", staticContext.getMetadata());
            }
            ItemRuntimePlan keyIterator = functionArguments.get(0);
            RuntimeStaticContext callStaticContext = RuntimeStaticContext.builder()
                    .configuration(staticContext.getConfiguration())
                    .staticType(SequenceType.createSequenceType("item*"))
                    .executionMode(ExecutionMode.LOCAL)
                    .metadata(staticContext.getMetadata())
                    .build();
            return new FunctionCall(
                    new ArrayFunctionCallIterator(functionItem, keyIterator, callStaticContext), context);
        }
        if (functionItem.isMap()) {
            if (partialApplication) {
                throw new UnexpectedTypeException(
                        "Partial application is not supported when calling maps as functions.",
                        staticContext.getMetadata());
            }
            if (functionArguments.size() != 1 || functionArguments.get(0) == null) {
                throw new UnexpectedTypeException(
                        "Map function calls must have exactly one argument.", staticContext.getMetadata());
            }
            ItemRuntimePlan keyIterator = functionArguments.get(0);
            RuntimeStaticContext callStaticContext = RuntimeStaticContext.builder()
                    .configuration(staticContext.getConfiguration())
                    .staticType(SequenceType.createSequenceType("item*"))
                    .executionMode(ExecutionMode.LOCAL)
                    .metadata(staticContext.getMetadata())
                    .build();
            return new FunctionCall(new MapFunctionCallIterator(functionItem, keyIterator, callStaticContext), context);
        }
        if (!functionItem.isFunction()) {
            throw new UnexpectedTypeException(
                    "Dynamic function calls can only be performed on functions, arrays, or maps.",
                    staticContext.getMetadata());
        }
        ExecutionMode calleeExecutionMode =
                FunctionCall.calleeExecutionMode(functionItem, functionArguments, partialApplication, staticContext);
        if (calleeExecutionMode.equals(ExecutionMode.LOCAL) && callerExecutionMode.equals(ExecutionMode.DATAFRAME)) {
            throw new OurBadException(
                    "Execution mode mismatch in dynamic function call. At this point, Rumble only supports higher-order functions "
                            + "that are either machine learning models or estimators (which process validated and structured sequences of objects at any scale), or that take and return just one item at a time.",
                    staticContext.getMetadata());
        }
        return FunctionCall.of(functionItem, functionArguments, staticContext, context);
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        return resolveFunctionCall(dynamicContext).getRDD();
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        try {
            return resolveFunctionCall(dynamicContext).getDataFrame();
        } catch (InvalidRumbleMLParamException e) {
            String m = e.getMLMessage();
            throw new InvalidRumbleMLParamException(m, getMetadata());
        }
    }
}
