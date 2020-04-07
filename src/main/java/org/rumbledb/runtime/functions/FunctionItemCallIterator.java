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
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.FunctionIdentifier;
import org.rumbledb.runtime.functions.base.FunctionSignature;
import org.rumbledb.runtime.operational.TypePromotionIterator;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;

import static org.rumbledb.types.SequenceType.mostGeneralSequenceType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FunctionItemCallIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    // parametrized fields
    private FunctionItem functionItem;
    private List<RuntimeIterator> functionArguments;

    // calculated fields
    private boolean isPartialApplication;
    private RuntimeIterator functionBodyIterator;
    private Item nextResult;


    public FunctionItemCallIterator(
            FunctionItem functionItem,
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

    }

    @Override
    public void openLocal() {
        this.validateNumberOfArguments();
        this.wrapArgumentIteratorsWithTypeCheckingIterators();

        if (this.isPartialApplication) {
            this.functionBodyIterator = generatePartiallyAppliedFunction(this.currentDynamicContextForLocalExecution);
        } else {
            this.functionBodyIterator = this.functionItem.getBodyIterator();
            this.currentDynamicContextForLocalExecution = this.createNewDynamicContextWithArguments(
                this.currentDynamicContextForLocalExecution
            );
        }

        this.functionBodyIterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
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
                        && !this.functionItem.getSignature().getParameterTypes().get(i).equals(mostGeneralSequenceType)
                ) {
                    TypePromotionIterator typePromotionIterator = new TypePromotionIterator(
                            this.functionArguments.get(i),
                            this.functionItem.getSignature().getParameterTypes().get(i),
                            "Invalid argument for " + this.functionItem.getIdentifier().getName() + " function. ",
                            this.functionArguments.get(i).getHighestExecutionMode(),
                            getMetadata()
                    );
                    this.functionArguments.set(i, typePromotionIterator);
                }
            }
        }
    }

    /**
     * Partial application generates a new function:
     * - Supplied parameters are set as NonLocalVariables
     * - Argument placeholders form the parameters
     *
     * @return FunctionRuntimeIterator that contains the newly generated FunctionItem
     */
    private FunctionRuntimeIterator generatePartiallyAppliedFunction(DynamicContext context) {
        String argName;
        RuntimeIterator argIterator;

        Map<String, List<Item>> localArgumentValues = new LinkedHashMap<>(
                this.functionItem.getLocalVariablesInClosure()
        );
        Map<String, JavaRDD<Item>> RDDArgumentValues = new LinkedHashMap<>(
                this.functionItem.getRDDVariablesInClosure()
        );
        Map<String, Dataset<Row>> DFArgumentValues = new LinkedHashMap<>(this.functionItem.getDFVariablesInClosure());

        List<String> partialApplicationParamNames = new ArrayList<>();
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
                } else if (argIterator.isRDD()) {
                    RDDArgumentValues.put(argName, argIterator.getRDD(context));
                } else {
                    localArgumentValues.put(argName, argIterator.materialize(context));
                }
            }
        }

        FunctionItem partiallyAppliedFunction = new FunctionItem(
                new FunctionIdentifier(
                        "partially applied " + this.functionItem.getIdentifier().getName(),
                        partialApplicationParamNames.size()
                ),
                partialApplicationParamNames,
                new FunctionSignature(
                        partialApplicationParamTypes,
                        this.functionItem.getSignature().getReturnType()
                ),
                this.functionItem.getBodyIterator(),
                localArgumentValues,
                RDDArgumentValues,
                DFArgumentValues
        );
        return new FunctionRuntimeIterator(partiallyAppliedFunction, ExecutionMode.LOCAL, getMetadata());
    }

    private DynamicContext createNewDynamicContextWithArguments(DynamicContext context) {
        String argName;
        RuntimeIterator argIterator;

        Map<String, List<Item>> localArgumentValues = new LinkedHashMap<>(
                this.functionItem.getLocalVariablesInClosure()
        );
        Map<String, JavaRDD<Item>> RDDArgumentValues = new LinkedHashMap<>(
                this.functionItem.getRDDVariablesInClosure()
        );
        Map<String, Dataset<Row>> DFArgumentValues = new LinkedHashMap<>(this.functionItem.getDFVariablesInClosure());

        for (int i = 0; i < this.functionArguments.size(); i++) {
            argName = this.functionItem.getParameterNames().get(i);
            argIterator = this.functionArguments.get(i);

            if (argIterator.isDataFrame()) {
                DFArgumentValues.put(argName, argIterator.getDataFrame(context));
            } else if (argIterator.isRDD()) {
                RDDArgumentValues.put(argName, argIterator.getRDD(context));
            } else {
                localArgumentValues.put(argName, argIterator.materialize(context));
            }
        }
        return new DynamicContext(context, localArgumentValues, RDDArgumentValues, DFArgumentValues);
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
    protected void resetLocal(DynamicContext context) {
        this.functionBodyIterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        // ensure that recursive function calls terminate gracefully
        // the function call in the body of the deepest recursion call is never visited, never opened and never closed
        if (this.isOpen()) {
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
        this.validateNumberOfArguments();
        this.wrapArgumentIteratorsWithTypeCheckingIterators();

        DynamicContext contextWithArguments = this.createNewDynamicContextWithArguments(dynamicContext);
        this.functionBodyIterator = this.functionItem.getBodyIterator();
        return this.functionBodyIterator.getRDD(contextWithArguments);
    }
}
