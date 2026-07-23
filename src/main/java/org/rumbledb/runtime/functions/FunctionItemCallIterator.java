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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.typing.AtMostOneItemTypePromotionIterator;
import org.rumbledb.runtime.typing.TypePromotionIterator;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

public class FunctionItemCallIterator extends HybridRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    // parametrized fields
    private Item functionItem;
    private List<RuntimeIterator> functionArguments;

    // calculated fields
    private boolean isPartialApplication;
    private boolean isTailOptimization;

    // Only used for local execution.
    private transient RuntimeIterator functionBodyIterator;

    private transient Item nextResult;

    public FunctionItemCallIterator(
            Item functionItem,
            List<RuntimeIterator> functionArguments,
            RuntimeStaticContext staticContext,
            boolean isTailOptimization
    ) {
        super(null, staticContext);
        for (RuntimeIterator arg : functionArguments) {
            if (arg == null) {
                this.isPartialApplication = true;
            } else {
                this.children.add(arg);
            }
        }
        if (isTailOptimization) {
            this.isPartialApplication = true;
            this.isTailOptimization = true;
        }
        this.functionItem = functionItem;
        this.functionArguments = functionArguments;
        this.functionBodyIterator = null;
        this.isUpdating = functionItem.getSignature().isUpdating();
        this.isSequential = functionItem.getBodyIterator().isSequential();

        this.validateNumberOfArguments();
        this.wrapArgumentIteratorsWithTypeCheckingIterators();

    }

    private DynamicContext createCallContext(DynamicContext context) {
        // A call context belongs to one invocation. Reusing it would retain parameters and function-local variables.
        Map<Name, List<Item>> localArgumentValues = new LinkedHashMap<>(
                this.functionItem.getLocalVariablesInClosure()
        );
        Map<Name, JavaRDD<Item>> RDDArgumentValues = new LinkedHashMap<>(
                this.functionItem.getRDDVariablesInClosure()
        );
        Map<Name, JSoundDataFrame> DFArgumentValues = new LinkedHashMap<>(
                this.functionItem.getDFVariablesInClosure()
        );

        DynamicContext callContext = new DynamicContext(
                this.functionItem.getModuleDynamicContext(),
                localArgumentValues,
                RDDArgumentValues,
                DFArgumentValues
        );
        populateDynamicContextWithArguments(context, callContext);
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
                    getMetadata()
            );
        }
    }

    private void wrapArgumentIteratorsWithTypeCheckingIterators() {
        if (this.functionItem.getSignature().getParameterTypes() != null) {
            for (int i = 0; i < this.functionArguments.size(); i++) {
                if (
                    this.functionArguments.get(i) != null
                        && !this.functionItem.getSignature()
                            .getParameterTypes()
                            .get(i)
                            .equals(SequenceType.createSequenceType("item*"))
                ) {
                    SequenceType sequenceType = this.functionItem.getSignature().getParameterTypes().get(i);
                    ExecutionMode executionMode = this.functionArguments.get(i).getHighestExecutionMode();
                    if (
                        sequenceType.isEmptySequence()
                            || sequenceType.getArity().equals(Arity.One)
                            || sequenceType.getArity().equals(Arity.OneOrZero)
                    ) {
                        executionMode = ExecutionMode.LOCAL;
                    }
                    RuntimeStaticContext runtimeStaticContext = getRuntimeStaticContext().withStaticType(sequenceType)
                        .withExecutionMode(executionMode)
                        .withMetadata(this.functionArguments.get(i).getMetadata());
                    RuntimeIterator argumentIterator = FunctionCallArgumentConversion.wrapForFunctionConversion(
                        this.functionArguments.get(i),
                        sequenceType,
                        "Invalid argument for " + this.functionItem.getIdentifier().getName() + " function. ",
                        runtimeStaticContext
                    );
                    if (
                        sequenceType.isEmptySequence()
                            || sequenceType.getArity().equals(Arity.One)
                            || sequenceType.getArity().equals(Arity.OneOrZero)
                    ) {
                        RuntimeIterator typePromotionIterator = new AtMostOneItemTypePromotionIterator(
                                argumentIterator,
                                sequenceType,
                                "Invalid argument for " + this.functionItem.getIdentifier().getName() + " function. ",
                                runtimeStaticContext
                        );
                        this.functionArguments.set(i, typePromotionIterator);
                    } else {
                        RuntimeIterator typePromotionIterator = new TypePromotionIterator(
                                argumentIterator,
                                sequenceType,
                                "Invalid argument for " + this.functionItem.getIdentifier().getName() + " function. ",
                                runtimeStaticContext
                        );
                        this.functionArguments.set(i, typePromotionIterator);
                    }
                }
            }
        }
    }

    @Override
    public void openLocal() {
        if (this.isPartialApplication) {
            this.functionBodyIterator = generatePartiallyAppliedFunction(this.currentDynamicContextForLocalExecution);
            this.functionBodyIterator.open(this.currentDynamicContextForLocalExecution);
        } else {
            DynamicContext callContext = createCallContext(this.currentDynamicContextForLocalExecution);
            if (this.functionBodyIterator == null) {
                // The previous body was discarded, or this is the first invocation at this call site.
                this.functionBodyIterator = createFunctionBodyIterator();
            }
            try {
                this.functionBodyIterator.open(callContext);
            } catch (RuntimeException exception) {
                discardBody();
                throw exception;
            }
        }
        try {
            setNextResult();
        } catch (RuntimeException exception) {
            discardBody();
            throw exception;
        }
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
    private RuntimeIterator generatePartiallyAppliedFunction(DynamicContext context) {
        Name argName;
        RuntimeIterator argIterator;

        Map<Name, List<Item>> localArgumentValues = new LinkedHashMap<>(
                this.functionItem.getLocalVariablesInClosure()
        );
        Map<Name, JavaRDD<Item>> RDDArgumentValues = new LinkedHashMap<>(
                this.functionItem.getRDDVariablesInClosure()
        );
        Map<Name, JSoundDataFrame> DFArgumentValues = new LinkedHashMap<>(
                this.functionItem.getDFVariablesInClosure()
        );

        List<Name> partialApplicationParamNames = new ArrayList<>();
        List<SequenceType> partialApplicationParamTypes = new ArrayList<>();

        for (int i = 0; i < this.functionArguments.size(); i++) {
            argName = this.functionItem.getParameterNames().get(i);
            argIterator = this.functionArguments.get(i);

            if (argIterator == null) { // == ArgumentPlaceholder
                partialApplicationParamNames.add(argName);
                partialApplicationParamTypes.add(this.functionItem.getSignature().getParameterTypes().get(i));
            } else {
                if (argIterator.isDataFrame()) {
                    DFArgumentValues.put(argName, argIterator.getDataFrame(context));
                } else if (argIterator.isRDDOrDataFrame()) {
                    RDDArgumentValues.put(argName, argIterator.getRDD(context));
                } else {
                    localArgumentValues.put(argName, argIterator.materialize(context));
                }
            }
        }

        Name functionItemName = this.functionItem.getIdentifier().getName();
        if (this.isTailOptimization) {
            functionItemName = Name.TAIL_CALL_OPTIMIZATION;
        }
        FunctionItem partiallyAppliedFunction = new FunctionItem(
                new FunctionIdentifier(
                        functionItemName,
                        partialApplicationParamNames.size()
                ),
                partialApplicationParamNames,
                new FunctionSignature(
                        partialApplicationParamTypes,
                        this.functionItem.getSignature().getReturnType(),
                        this.functionItem.getSignature().isUpdating()
                ),
                this.functionItem.getModuleDynamicContext(),
                this.functionItem.getBodyIterator(),
                localArgumentValues,
                RDDArgumentValues,
                DFArgumentValues
        );
        return new ConstantRuntimeIterator(
                partiallyAppliedFunction,
                this.staticContext
                    .toBuilder()
                    .staticType(SequenceType.createSequenceType("function(*)"))
                    .executionMode(ExecutionMode.LOCAL)
                    .metadata(getMetadata())
                    .build()
        );
    }

    private void populateDynamicContextWithArguments(DynamicContext context, DynamicContext callContext) {
        Name argName;
        RuntimeIterator argIterator;

        for (int i = 0; i < this.functionArguments.size(); i++) {
            argName = this.functionItem.getParameterNames().get(i);
            argIterator = this.functionArguments.get(i);

            if (argIterator.isDataFrame()) {
                callContext.getVariableValues()
                    .addVariableValue(argName, argIterator.getDataFrame(context));
            } else if (argIterator.isRDDOrDataFrame()) {
                callContext.getVariableValues().addVariableValue(argName, argIterator.getRDD(context));
            } else {
                callContext.getVariableValues()
                    .addVariableValue(argName, argIterator.materialize(context));
            }
        }
    }

    /**
     * Creates an execution instance without exposing the shared function-body prototype to mutation.
     * FunctionItem uses its cached snapshot; other Item implementations retain the generic deep-copy fallback.
     */
    private RuntimeIterator createFunctionBodyIterator() {
        if (this.functionItem instanceof FunctionItem concreteFunctionItem) {
            return concreteFunctionItem.createBodyIterator();
        }
        return this.functionItem.getBodyIterator().deepCopy();
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE
                    + " in "
                    + this.functionItem.getIdentifier().getName()
                    + "  function",
                getMetadata()
        );
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void closeLocal() {
        this.nextResult = null;
        this.hasNext = false;
        // Preserve a normally exhausted body for reuse; discard an execution that is still active.
        if (this.functionBodyIterator != null && this.functionBodyIterator.isOpen()) {
            discardBody();
        }
    }

    private void setNextResult() {
        try {
            this.nextResult = null;
            if (this.functionBodyIterator.hasNext()) {
                this.nextResult = this.functionBodyIterator.next();
            }

            if (this.nextResult == null) {
                // Reaching the end through normal iteration is the only path that can make a body reusable.
                this.hasNext = false;
                this.functionBodyIterator.close();
                if (this.isPartialApplication || !canReuseBody()) {
                    discardBody();
                }
            } else {
                this.hasNext = true;
            }
        } catch (RuntimeException exception) {
            discardBody();
            throw exception;
        }
    }

    /**
     * Sequential and updating bodies can retain statement or mutation state even after normal exhaustion.
     */
    private boolean canReuseBody() {
        return !this.isSequential && !this.isUpdating;
    }

    /**
     * Closes an active execution and removes it from this call site.
     */
    private void discardBody() {
        RuntimeIterator iterator = this.functionBodyIterator;
        try {
            if (iterator != null && iterator.isOpen()) {
                iterator.close();
            }
        } finally {
            this.functionBodyIterator = null;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        if (this.isPartialApplication) {
            throw new OurBadException(
                    "Unexpected program state reached. Partially applied function calls must be evaluated locally."
            );
        }

        DynamicContext callContext = createCallContext(dynamicContext);
        RuntimeIterator bodyIterator = createFunctionBodyIterator();
        return bodyIterator.getRDD(callContext);
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        if (this.isPartialApplication) {
            throw new OurBadException(
                    "Unexpected program state reached. Partially applied function calls must be evaluated locally."
            );
        }

        DynamicContext callContext = createCallContext(dynamicContext);
        RuntimeIterator bodyIterator = createFunctionBodyIterator();
        return bodyIterator.getDataFrame(callContext);
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!isUpdating()) {
            return new PendingUpdateList();
        }
        DynamicContext callContext = createCallContext(context);
        DynamicContext contextForUpdates = new DynamicContext(callContext);
        contextForUpdates.setCurrentMutabilityLevel(context.getCurrentMutabilityLevel());
        RuntimeIterator bodyIterator = createFunctionBodyIterator();
        return bodyIterator.getPendingUpdateList(contextForUpdates);
    }
}
