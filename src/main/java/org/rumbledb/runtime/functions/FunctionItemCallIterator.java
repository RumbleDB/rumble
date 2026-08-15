/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
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
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
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
    private boolean isPartialApplication;
    private boolean isTailOptimization;

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
        this.isPartialApplication = functionArguments.stream().anyMatch(arg -> arg == null);
        if (isTailOptimization) {
            this.isPartialApplication = true;
            this.isTailOptimization = true;
        }
        this.functionItem = functionItem;
        this.functionArguments = functionArguments;
        this.validateNumberOfArguments();
        this.wrapArgumentIteratorsWithTypeCheckingIterators();
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new FunctionCallLocalCursor(
                this.functionItem,
                this.functionArguments,
                this.isPartialApplication,
                this.isTailOptimization,
                this.staticContext,
                context);
    }

    private static final class FunctionCallLocalCursor extends AbstractLocalCursor<Item> {
        private final Item functionItem;
        private final List<ItemRuntimePlan> functionArguments;
        private final boolean partialApplication;
        private final boolean tailOptimization;
        private final RuntimeStaticContext staticContext;
        private final DynamicContext context;
        private Cursor<Item> body;

        private FunctionCallLocalCursor(
                Item functionItem,
                List<ItemRuntimePlan> functionArguments,
                boolean partialApplication,
                boolean tailOptimization,
                RuntimeStaticContext staticContext,
                DynamicContext context) {
            super(staticContext.getMetadata());
            this.functionItem = functionItem;
            this.functionArguments = functionArguments;
            this.partialApplication = partialApplication;
            this.tailOptimization = tailOptimization;
            this.staticContext = staticContext;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            ItemRuntimePlan bodyPlan;
            DynamicContext bodyContext;
            if (this.partialApplication) {
                bodyPlan = generatePartiallyAppliedFunction(
                        this.functionItem,
                        this.functionArguments,
                        this.tailOptimization,
                        this.staticContext,
                        this.context);
                bodyContext = this.context;
            } else {
                bodyPlan = this.functionItem.getBodyIterator();
                bodyContext = createCallContext(this.functionItem, this.functionArguments, this.context);
            }
            this.body = bodyPlan.getCursor(bodyContext);
        }

        @Override
        protected boolean hasNextLocal() {
            return this.body.hasNext();
        }

        @Override
        protected Item nextLocal() {
            return this.body.next();
        }

        @Override
        protected void closeLocal() {
            if (this.body != null) {
                this.body.close();
                this.body = null;
            }
        }
    }

    private DynamicContext createCallContext(DynamicContext context) {
        return createCallContext(this.functionItem, this.functionArguments, context);
    }

    private static DynamicContext createCallContext(
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
                this.functionItem, this.functionArguments, getRuntimeStaticContext());
    }

    /**
     * Partial application generates a new function:
     *
     * <ul>
     * <li>Supplied parameters are set as NonLocalVariables</li>
     * <li>Argument placeholders form the parameters</li>
     * </ul>
     *
     * @return a one-item iterator containing the partially applied function item
     */
    private static ItemRuntimePlan generatePartiallyAppliedFunction(
            Item functionItem,
            List<ItemRuntimePlan> functionArguments,
            boolean tailOptimization,
            RuntimeStaticContext staticContext,
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
        return new ConstantRuntimeIterator(
                partiallyAppliedFunction,
                staticContext.toBuilder()
                        .staticType(SequenceType.createSequenceType("function(*)"))
                        .executionMode(ExecutionMode.LOCAL)
                        .metadata(staticContext.getMetadata())
                        .build());
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

            if (argIterator.getRuntimeStaticContext().getExecutionMode().isDataFrame()) {
                callContext
                        .getVariableValues()
                        .addVariableValue(argName, ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(argIterator, context));
            } else if (argIterator.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
                callContext.getVariableValues().addVariableValue(argName, argIterator.getRDD(context));
            } else {
                callContext.getVariableValues().addVariableValue(argName, argIterator.materialize(context));
            }
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
