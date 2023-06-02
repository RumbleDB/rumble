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

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
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

import static org.rumbledb.types.SequenceType.ITEM_STAR;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FunctionItemCallIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    // parametrized fields
    private Item functionItem;
    private List<RuntimeIterator> functionArguments;

    // calculated fields
    private boolean isPartialApplication;
    private RuntimeIterator functionBodyIterator;
    private Item nextResult;
    private transient DynamicContext dynamicContextForCalls;


    public FunctionItemCallIterator(
            Item functionItem,
            List<RuntimeIterator> functionArguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        for (RuntimeIterator arg : functionArguments) {
            if (arg == null) {
                this.isPartialApplication = true;
            } else {
                this.children.add(arg);
            }
        }
        this.functionItem = functionItem;
        this.functionArguments = functionArguments;
        this.functionBodyIterator = null;
        this.isUpdating = functionItem.getSignature().isUpdating();

        this.validateNumberOfArguments();
        this.wrapArgumentIteratorsWithTypeCheckingIterators();

        // Prepopulation of the dynamic context (without the parameters)
        Map<Name, List<Item>> localArgumentValues = new LinkedHashMap<>(
                this.functionItem.getLocalVariablesInClosure()
        );
        Map<Name, JavaRDD<Item>> RDDArgumentValues = new LinkedHashMap<>(
                this.functionItem.getRDDVariablesInClosure()
        );
        Map<Name, JSoundDataFrame> DFArgumentValues = new LinkedHashMap<>(
                this.functionItem.getDFVariablesInClosure()
        );

        this.dynamicContextForCalls = new DynamicContext(
                this.functionItem.getModuleDynamicContext(),
                localArgumentValues,
                RDDArgumentValues,
                DFArgumentValues
        );
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
                            .equals(ITEM_STAR)
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
                    if (
                        sequenceType.isEmptySequence()
                            || sequenceType.getArity().equals(Arity.One)
                            || sequenceType.getArity().equals(Arity.OneOrZero)
                    ) {
                        RuntimeIterator typePromotionIterator = new AtMostOneItemTypePromotionIterator(
                                this.functionArguments.get(i),
                                sequenceType,
                                "Invalid argument for " + this.functionItem.getIdentifier().getName() + " function. ",
                                executionMode,
                                getMetadata()
                        );
                        this.functionArguments.set(i, typePromotionIterator);
                    } else {
                        RuntimeIterator typePromotionIterator = new TypePromotionIterator(
                                this.functionArguments.get(i),
                                sequenceType,
                                "Invalid argument for " + this.functionItem.getIdentifier().getName() + " function. ",
                                executionMode,
                                getMetadata()
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
        } else {
            if (this.functionBodyIterator == null) {
                this.functionBodyIterator = this.functionItem.getBodyIterator().deepCopy();
            }
            this.populateDynamicContextWithArguments(
                this.currentDynamicContextForLocalExecution
            );
        }
        this.functionBodyIterator.open(this.dynamicContextForCalls);
        setNextResult();
    }

    /**
     * Partial application generates a new function:
     * - Supplied parameters are set as NonLocalVariables
     * - Argument placeholders form the parameters
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

        FunctionItem partiallyAppliedFunction = new FunctionItem(
                new FunctionIdentifier(
                        this.functionItem.getIdentifier().getName(),
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
        return new ConstantRuntimeIterator(partiallyAppliedFunction, ExecutionMode.LOCAL, getMetadata());
    }

    private void populateDynamicContextWithArguments(DynamicContext context) {
        Name argName;
        RuntimeIterator argIterator;

        for (int i = 0; i < this.functionArguments.size(); i++) {
            argName = this.functionItem.getParameterNames().get(i);
            argIterator = this.functionArguments.get(i);

            if (argIterator.isDataFrame()) {
                this.dynamicContextForCalls.getVariableValues()
                    .addVariableValue(argName, argIterator.getDataFrame(context));
            } else if (argIterator.isRDDOrDataFrame()) {
                this.dynamicContextForCalls.getVariableValues().addVariableValue(argName, argIterator.getRDD(context));
            } else {
                this.dynamicContextForCalls.getVariableValues()
                    .addVariableValue(argName, argIterator.materialize(context));
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
        this.functionBodyIterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        // ensure that recursive function calls terminate gracefully
        // the function call in the body of the deepest recursion call is never visited, never opened and never closed
        if (this.functionBodyIterator != null && this.functionBodyIterator.isOpen()) {
            this.functionBodyIterator.close();
        }
    }

    public void setNextResult() {
        this.nextResult = null;
        if (this.functionBodyIterator.hasNext()) {
            this.nextResult = this.functionBodyIterator.next();
        }

        if (this.nextResult == null) {
            this.hasNext = false;
            this.functionBodyIterator.close();
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

        this.populateDynamicContextWithArguments(dynamicContext);
        this.functionBodyIterator = this.functionItem.getBodyIterator();
        return this.functionBodyIterator.getRDD(this.dynamicContextForCalls);
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

        populateDynamicContextWithArguments(dynamicContext);
        this.functionBodyIterator = this.functionItem.getBodyIterator();
        return this.functionBodyIterator.getDataFrame(this.dynamicContextForCalls);
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!isUpdating()) {
            return new PendingUpdateList();
        }
        this.populateDynamicContextWithArguments(context);
        DynamicContext contextForUpdates = new DynamicContext(this.dynamicContextForCalls);
        contextForUpdates.setCurrentMutabilityLevel(context.getCurrentMutabilityLevel());
        this.functionBodyIterator = this.functionItem.getBodyIterator();
        return this.functionBodyIterator.getPendingUpdateList(contextForUpdates);
    }
}
