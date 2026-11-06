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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.plan.UpdatingRuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.types.FunctionSignature;

public class StaticUserDefinedFunctionCallIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item>, DataFrameRuntimePlan<Item>, UpdatingRuntimePlan {
    // static: functionIdentifier known at compile time

    @Serial
    private static final long serialVersionUID = 1L;
    // parametrized fields
    private final FunctionIdentifier functionIdentifier;
    private final boolean tailCallOptimizationCandidate;
    // The arguments converted to the declared parameter types, with null for the placeholders of a partial
    // application. A call then only looks up the function, instead of building a call plan for every call.
    private final List<ItemRuntimePlan> convertedArguments;
    private final boolean isPartialApplication;

    public StaticUserDefinedFunctionCallIterator(
            FunctionIdentifier functionIdentifier,
            List<ItemRuntimePlan> functionArguments,
            FunctionSignature signature,
            RuntimeStaticContext staticContext,
            boolean tailCallOptimization) {
        super(List.of(), staticContext);
        this.functionIdentifier = functionIdentifier;
        this.tailCallOptimizationCandidate = tailCallOptimization;
        this.isPartialApplication = functionArguments.contains(null);
        List<ItemRuntimePlan> convertedArguments = new ArrayList<>(functionArguments);
        FunctionCallArgumentConversion.wrapAccordingToSignature(
                functionIdentifier.getName(), signature, convertedArguments, staticContext);
        this.convertedArguments = Collections.unmodifiableList(convertedArguments);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        if (this.isPartialApplication || this.tailCallOptimizationCandidate) {
            // The result is a function item with the supplied arguments bound. For a tail call, the body of the
            // function evaluates it (see FunctionBodyIterator).
            return new IteratorLocalCursor<>(
                    () -> List.<Item>of(FunctionCall.partiallyApply(
                                    getFunction(context),
                                    this.convertedArguments,
                                    this.tailCallOptimizationCandidate,
                                    context))
                            .iterator(),
                    getMetadata());
        }
        FunctionItem function = getFunction(context);
        return function.getCalleePlan().getCursor(createCallContext(function, context));
    }

    private FunctionItem getFunction(DynamicContext context) {
        return context.getNamedFunctions().getSharedUserDefinedFunction(this.functionIdentifier, getMetadata());
    }

    private DynamicContext createCallContext(FunctionItem function, DynamicContext context) {
        if (this.isPartialApplication) {
            throw new OurBadException(
                    "Unexpected program state reached. Partially applied function calls must be evaluated locally.");
        }
        return FunctionCall.createCallContext(function, this.convertedArguments, context);
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        FunctionItem function = getFunction(dynamicContext);
        return function.getCalleePlan().getRDD(createCallContext(function, dynamicContext));
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        FunctionItem function = getFunction(dynamicContext);
        return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(
                function.getCalleePlan(), createCallContext(function, dynamicContext));
    }

    @Override
    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>(super.getVariableDependencies());
        for (ItemRuntimePlan iterator : this.convertedArguments) {
            if (iterator == null) {
                continue;
            }
            result.putAll(iterator.getVariableDependencies());
        }
        return result;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!this.staticContext.isUpdating()) {
            return new PendingUpdateList();
        }
        // Updates come from the body: the return type conversion does not apply.
        FunctionItem function = getFunction(context);
        DynamicContext contextForUpdates = new DynamicContext(createCallContext(function, context));
        contextForUpdates.setCurrentMutabilityLevel(context.getCurrentMutabilityLevel());
        return UpdatingRuntimePlan.get(function.getBodyIterator(), contextForUpdates);
    }
}
