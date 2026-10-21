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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlanBindings;
import org.rumbledb.runtime.plan.UpdatingRuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

public class FunctionItemCallIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item>, DataFrameRuntimePlan<Item>, UpdatingRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    // parametrized fields
    private final Item functionItem;
    private final List<ItemRuntimePlan> functionArguments;

    // calculated fields
    private final boolean isPartialApplication;
    private final boolean isTailOptimization;

    public FunctionItemCallIterator(
            Item functionItem,
            List<ItemRuntimePlan> functionArguments,
            RuntimeStaticContext staticContext,
            boolean isTailOptimization) {
        super(
                functionArguments.stream().filter(arg -> arg != null).toList(),
                staticContext.toBuilder()
                        .isUpdating(functionItem.getSignature().isUpdating())
                        .isSequential(functionItem
                                .getBodyIterator()
                                .getRuntimeStaticContext()
                                .isSequential())
                        .build());
        // A tail call is evaluated as a partial application that returns a thunk.
        this.isPartialApplication =
                isTailOptimization || functionArguments.stream().anyMatch(arg -> arg == null);
        this.isTailOptimization = isTailOptimization;
        this.functionItem = functionItem;
        this.functionArguments = functionArguments;
        this.validateNumberOfArguments();
        this.wrapArgumentIteratorsWithTypeCheckingIterators();
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        if (this.isPartialApplication) {
            return new IteratorLocalCursor<>(
                    () -> List.<Item>of(createPartiallyAppliedFunction(
                                    this.functionItem, this.functionArguments, this.isTailOptimization, context))
                            .iterator(),
                    getMetadata());
        }
        return this.functionItem.getBodyIterator().getCursor(createCallContext(context));
    }

    private DynamicContext createCallContext(DynamicContext context) {
        return createCallContext(this.functionItem, this.functionArguments, context);
    }

    static DynamicContext createCallContext(
            Item functionItem, List<ItemRuntimePlan> functionArguments, DynamicContext context) {
        // A call context belongs to one invocation. Reusing it would retain parameters and function-local variables.
        Map<Name, List<Item>> localArgumentValues = new LinkedHashMap<>(functionItem.getLocalVariablesInClosure());
        Map<Name, JavaRDD<Item>> RDDArgumentValues = new LinkedHashMap<>(functionItem.getRDDVariablesInClosure());
        Map<Name, HomogeneousItemDataFrame> DFArgumentValues =
                new LinkedHashMap<>(functionItem.getDFVariablesInClosure());

        DynamicContext callContext = new DynamicContext(
                functionItem.getModuleDynamicContext(), localArgumentValues, RDDArgumentValues, DFArgumentValues);
        populateDynamicContextWithArguments(functionItem, functionArguments, context, callContext);
        return callContext;
    }

    private void validateNumberOfArguments() {
        if (this.functionItem.getParameterNames().size() != this.functionArguments.size()) {
            throw new UnexpectedTypeException(
                    "Dynamic function "
                            + this.functionItem.getIdentifier().getName()
                            + " invoked with incorrect number of arguments. Expected: "
                            + this.functionItem.getParameterNames().size()
                            + ", Found: "
                            + this.functionArguments.size(),
                    getMetadata());
        }
    }

    private void wrapArgumentIteratorsWithTypeCheckingIterators() {
        FunctionCallArgumentConversion.wrapAccordingToSignature(
                this.functionItem.getIdentifier().getName(),
                this.functionItem.getSignature(),
                this.functionArguments,
                getRuntimeStaticContext());
    }

    /**
     * Partial application generates a new function:
     *
     * <ul>
     * <li>Supplied parameters are set as NonLocalVariables</li>
     * <li>Argument placeholders form the parameters</li>
     * </ul>
     *
     * For a tail call, all arguments are supplied, and the outermost call that is not a tail call then calls the
     * returned function item.
     *
     * @return the partially applied function item
     */
    static FunctionItem createPartiallyAppliedFunction(
            Item functionItem,
            List<ItemRuntimePlan> functionArguments,
            boolean tailOptimization,
            DynamicContext context) {
        Name argName;
        ItemRuntimePlan argIterator;

        Map<Name, List<Item>> localArgumentValues = new LinkedHashMap<>(functionItem.getLocalVariablesInClosure());
        Map<Name, JavaRDD<Item>> RDDArgumentValues = new LinkedHashMap<>(functionItem.getRDDVariablesInClosure());
        Map<Name, HomogeneousItemDataFrame> DFArgumentValues =
                new LinkedHashMap<>(functionItem.getDFVariablesInClosure());

        List<Name> partialApplicationParamNames = new ArrayList<>();
        List<SequenceType> partialApplicationParamTypes = new ArrayList<>();

        for (int i = 0; i < functionArguments.size(); i++) {
            argName = functionItem.getParameterNames().get(i);
            argIterator = functionArguments.get(i);

            if (argIterator == null) { // == ArgumentPlaceholder
                partialApplicationParamNames.add(argName);
                partialApplicationParamTypes.add(
                        functionItem.getSignature().getParameterTypes().get(i));
            } else {
                if (argIterator.getRuntimeStaticContext().getExecutionMode().isDataFrame()) {
                    DFArgumentValues.put(argName, ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(argIterator, context));
                } else if (argIterator
                        .getRuntimeStaticContext()
                        .getExecutionMode()
                        .isRDDOrDataFrame()) {
                    RDDArgumentValues.put(argName, argIterator.getRDD(context));
                } else {
                    localArgumentValues.put(argName, argIterator.materialize(context));
                }
            }
        }

        Name functionItemName = functionItem.getIdentifier().getName();
        if (tailOptimization) {
            functionItemName = Name.TAIL_CALL_OPTIMIZATION;
        }
        FunctionItem partiallyAppliedFunction = new FunctionItem(
                new FunctionIdentifier(functionItemName, partialApplicationParamNames.size()),
                partialApplicationParamNames,
                new FunctionSignature(
                        partialApplicationParamTypes,
                        functionItem.getSignature().getReturnType(),
                        functionItem.getSignature().isUpdating()),
                functionItem.getModuleDynamicContext(),
                functionItem.getBodyIterator(),
                localArgumentValues,
                RDDArgumentValues,
                DFArgumentValues);
        if (functionItem instanceof FunctionItem function) {
            partiallyAppliedFunction.shareCalleePlan(function);
        }
        return partiallyAppliedFunction;
    }

    private static void populateDynamicContextWithArguments(
            Item functionItem,
            List<ItemRuntimePlan> functionArguments,
            DynamicContext context,
            DynamicContext callContext) {
        Name argName;
        ItemRuntimePlan argIterator;

        for (int i = 0; i < functionArguments.size(); i++) {
            argName = functionItem.getParameterNames().get(i);
            argIterator = functionArguments.get(i);

            RuntimePlanBindings.bind(argIterator, callContext, argName, context);
        }
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        if (this.isPartialApplication) {
            throw new OurBadException(
                    "Unexpected program state reached. Partially applied function calls must be evaluated locally.");
        }

        DynamicContext callContext = createCallContext(dynamicContext);
        ItemRuntimePlan bodyIterator = this.functionItem.getBodyIterator();
        return bodyIterator.getRDD(callContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        if (this.isPartialApplication) {
            throw new OurBadException(
                    "Unexpected program state reached. Partially applied function calls must be evaluated locally.");
        }

        DynamicContext callContext = createCallContext(dynamicContext);
        ItemRuntimePlan bodyIterator = this.functionItem.getBodyIterator();
        return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(bodyIterator, callContext);
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!this.staticContext.isUpdating()) {
            return new PendingUpdateList();
        }
        DynamicContext callContext = createCallContext(context);
        DynamicContext contextForUpdates = new DynamicContext(callContext);
        contextForUpdates.setCurrentMutabilityLevel(context.getCurrentMutabilityLevel());
        ItemRuntimePlan bodyIterator = this.functionItem.getBodyIterator();
        return UpdatingRuntimePlan.get(bodyIterator, contextForUpdates);
    }
}
