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

    private static final long serialVersionUID = 1L;
    // parametrized fields
    private Item functionItem;
    private List<RuntimeIterator> functionArguments;

    // calculated fields
    private boolean isPartialApplication;
    private boolean isTailOptimization;
    private transient RuntimeIterator localBodyIterator;
    private transient boolean borrowedLocalBodyIterator;
    private transient Item nextResult;
    private transient DynamicContext localCallContext;


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
        this.localBodyIterator = null;
        this.isUpdating = functionItem.getSignature().isUpdating();

        this.validateNumberOfArguments();
        this.wrapArgumentIteratorsWithTypeCheckingIterators();

        this.localCallContext = createCallContext();
    }

    private DynamicContext createCallContext() {
        Map<Name, List<Item>> localArgumentValues = new LinkedHashMap<>(
                this.functionItem.getLocalVariablesInClosure()
        );
        Map<Name, JavaRDD<Item>> RDDArgumentValues = new LinkedHashMap<>(
                this.functionItem.getRDDVariablesInClosure()
        );
        Map<Name, JSoundDataFrame> DFArgumentValues = new LinkedHashMap<>(
                this.functionItem.getDFVariablesInClosure()
        );

        return new DynamicContext(
                this.functionItem.getModuleDynamicContext(),
                localArgumentValues,
                RDDArgumentValues,
                DFArgumentValues
        );
    }

    private DynamicContext getLocalCallContext() {
        if (this.localCallContext == null) {
            this.localCallContext = createCallContext();
        }
        return this.localCallContext;
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
            if (this.localBodyIterator != null && this.localBodyIterator.isOpen()) {
                this.localBodyIterator.close();
            }
            this.localBodyIterator = generatePartiallyAppliedFunction(this.currentDynamicContextForLocalExecution);
            this.borrowedLocalBodyIterator = false;
        } else {
            if (this.localBodyIterator == null) {
                this.localBodyIterator = ((FunctionItem) this.functionItem).acquireBodyIterator();
                this.borrowedLocalBodyIterator = true;
            }
            this.bindArguments(
                this.currentDynamicContextForLocalExecution,
                getLocalCallContext()
            );
        }
        this.localBodyIterator.open(getLocalCallContext());
        setNextResult();
    }

    /**
     * Partial application generates a new function:
     * 
     * <ul>
     * <li>Supplied parameters are set as NonLocalVariables</li>
     * <li>Argument placeholders form the parameters</li>
     * </ul>
     *
     * @return FunctionRuntimeIterator that contains the newly generated FunctionItem
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
        FunctionItem partiallyAppliedFunction = ((FunctionItem) this.functionItem).createPartialFunction(
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
            localArgumentValues,
            RDDArgumentValues,
            DFArgumentValues
        );
        return new ConstantRuntimeIterator(
                partiallyAppliedFunction,
                this.staticContext.withStaticType(
                    SequenceType.createSequenceType("function(*)")
                ).withExecutionMode(ExecutionMode.LOCAL).withMetadata(getMetadata())
        );
    }

    private void bindArguments(DynamicContext callerContext, DynamicContext callContext) {
        Name argName;
        RuntimeIterator argIterator;

        for (int i = 0; i < this.functionArguments.size(); i++) {
            argName = this.functionItem.getParameterNames().get(i);
            argIterator = this.functionArguments.get(i);

            if (argIterator.isDataFrame()) {
                callContext.getVariableValues().setVariableValue(argName, argIterator.getDataFrame(callerContext));
            } else if (argIterator.isRDDOrDataFrame()) {
                callContext.getVariableValues().setVariableValue(argName, argIterator.getRDD(callerContext));
            } else {
                callContext.getVariableValues().setVariableValue(argName, argIterator.materialize(callerContext));
            }
        }
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
    protected void resetLocal() {
        if (this.isPartialApplication) {
            if (this.localBodyIterator != null && this.localBodyIterator.isOpen()) {
                this.localBodyIterator.close();
            }
            this.localBodyIterator = generatePartiallyAppliedFunction(this.currentDynamicContextForLocalExecution);
            this.localBodyIterator.open(getLocalCallContext());
            setNextResult();
            return;
        }
        this.bindArguments(this.currentDynamicContextForLocalExecution, getLocalCallContext());
        if (this.localBodyIterator == null) {
            this.localBodyIterator = ((FunctionItem) this.functionItem).acquireBodyIterator();
            this.borrowedLocalBodyIterator = true;
            this.localBodyIterator.open(getLocalCallContext());
            setNextResult();
            return;
        }
        this.localBodyIterator.reset(getLocalCallContext());
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        // ensure that recursive function calls terminate gracefully
        // the function call in the body of the deepest recursion call is never visited, never opened and never closed
        if (this.localBodyIterator != null && this.localBodyIterator.isOpen()) {
            this.localBodyIterator.close();
        }
        if (this.localBodyIterator != null && this.borrowedLocalBodyIterator) {
            ((FunctionItem) this.functionItem).releaseBodyIterator(this.localBodyIterator);
        }
        this.localBodyIterator = null;
        this.borrowedLocalBodyIterator = false;
    }

    public void setNextResult() {
        this.nextResult = null;
        if (this.localBodyIterator.hasNext()) {
            this.nextResult = this.localBodyIterator.next();
        }

        if (this.nextResult == null) {
            this.hasNext = false;
            this.localBodyIterator.close();
        } else {
            this.hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        if (this.isPartialApplication) {
            throw new OurBadException(
                    "Unexpected program state reached. Partially applied function calls must be evaluated locally."
            );
        }

        DynamicContext callContext = createCallContext();
        bindArguments(dynamicContext, callContext);
        RuntimeIterator bodyIterator = ((FunctionItem) this.functionItem).createBodyIteratorForDistributedExecution();
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

        DynamicContext callContext = createCallContext();
        bindArguments(dynamicContext, callContext);
        RuntimeIterator bodyIterator = ((FunctionItem) this.functionItem).createBodyIteratorForDistributedExecution();
        return bodyIterator.getDataFrame(callContext);
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!isUpdating()) {
            return new PendingUpdateList();
        }
        DynamicContext callContext = createCallContext();
        bindArguments(context, callContext);
        DynamicContext contextForUpdates = new DynamicContext(callContext);
        contextForUpdates.setCurrentMutabilityLevel(context.getCurrentMutabilityLevel());
        RuntimeIterator bodyIterator = ((FunctionItem) this.functionItem).createBodyIteratorForDistributedExecution();
        return bodyIterator.getPendingUpdateList(contextForUpdates);
    }
}
