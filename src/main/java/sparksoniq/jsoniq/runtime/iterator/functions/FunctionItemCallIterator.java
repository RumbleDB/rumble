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

package sparksoniq.jsoniq.runtime.iterator.functions;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.FunctionItem;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionSignature;
import sparksoniq.jsoniq.runtime.iterator.operational.TypePromotionIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static sparksoniq.semantics.types.SequenceType.mostGeneralSequenceType;

public class FunctionItemCallIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    // parametrized fields
    private FunctionItem _functionItem;
    private List<RuntimeIterator> _functionArguments;

    // calculated fields
    private boolean _isPartialApplication;
    private RuntimeIterator _functionBodyIterator;
    private Item _nextResult;


    public FunctionItemCallIterator(
            FunctionItem functionItem,
            List<RuntimeIterator> functionArguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        for (RuntimeIterator arg : functionArguments) {
            if (arg == null) {
                this._isPartialApplication = true;
            } else {
                this._children.add(arg);
            }
        }
        this._functionItem = functionItem;
        this._functionArguments = functionArguments;

    }

    @Override
    public void openLocal() {
        if (this._isPartialApplication) {
            this._functionBodyIterator = generatePartiallyAppliedFunction(this._currentDynamicContextForLocalExecution);
        } else {
            this._functionBodyIterator = this._functionItem.getBodyIterator();
            this._currentDynamicContextForLocalExecution = this.createNewDynamicContextWithArguments(
                this._currentDynamicContextForLocalExecution
            );
        }

        this._functionBodyIterator.open(this._currentDynamicContextForLocalExecution);
        setNextResult();
    }

    private void validateAndReadArguments() {
        String formattedName = (!this._functionItem.getIdentifier().getName().equals(""))
            ? this._functionItem.getIdentifier().getName() + " "
            : "";
        if (this._functionItem.getParameterNames().size() != this._functionArguments.size()) {
            throw new UnexpectedTypeException(
                    "Dynamic function "
                        + formattedName
                        + "invoked with incorrect number of arguments. Expected: "
                        + this._functionItem.getParameterNames().size()
                        + ", Found: "
                        + this._functionArguments.size(),
                    getMetadata()
            );
        }

        if (this._functionItem.getSignature().getParameterTypes() != null) {
            for (int i = 0; i < this._functionArguments.size(); i++) {
                if (
                    this._functionArguments.get(i) != null
                        && !this._functionItem.getSignature().getParameterTypes().get(i).equals(mostGeneralSequenceType)
                ) {
                    TypePromotionIterator typePromotionIterator = new TypePromotionIterator(
                            this._functionArguments.get(i),
                            this._functionItem.getSignature().getParameterTypes().get(i),
                            "Invalid argument for " + formattedName + "function. ",
                            this._functionArguments.get(i).getHighestExecutionMode(),
                            getMetadata()
                    );
                    this._functionArguments.set(i, typePromotionIterator);
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
        this.validateAndReadArguments();

        String argName;
        RuntimeIterator argIterator;

        Map<String, List<Item>> localArgumentValues = new LinkedHashMap<>(
                this._functionItem.getLocalVariablesInClosure()
        );
        Map<String, JavaRDD<Item>> RDDArgumentValues = new LinkedHashMap<>(
                this._functionItem.getRDDVariablesInClosure()
        );
        Map<String, Dataset<Row>> DFArgumentValues = new LinkedHashMap<>(this._functionItem.getDFVariablesInClosure());

        List<String> partialApplicationParamNames = new ArrayList<>();
        List<SequenceType> partialApplicationParamTypes = new ArrayList<>();

        for (int i = 0; i < this._functionArguments.size(); i++) {
            argName = this._functionItem.getParameterNames().get(i);
            argIterator = this._functionArguments.get(i);

            if (argIterator == null) { // == ArgumentPlaceholder
                partialApplicationParamNames.add(argName);
                partialApplicationParamTypes.add(this._functionItem.getSignature().getParameterTypes().get(i));
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
                new FunctionIdentifier("", partialApplicationParamNames.size()),
                partialApplicationParamNames,
                new FunctionSignature(
                        partialApplicationParamTypes,
                        this._functionItem.getSignature().getReturnType()
                ),
                this._functionItem.getBodyIterator(),
                localArgumentValues,
                RDDArgumentValues,
                DFArgumentValues
        );
        return new FunctionRuntimeIterator(partiallyAppliedFunction, ExecutionMode.LOCAL, getMetadata());
    }

    private DynamicContext createNewDynamicContextWithArguments(DynamicContext context) {
        this.validateAndReadArguments();

        String argName;
        RuntimeIterator argIterator;

        Map<String, List<Item>> localArgumentValues = new LinkedHashMap<>(
                this._functionItem.getLocalVariablesInClosure()
        );
        Map<String, JavaRDD<Item>> RDDArgumentValues = new LinkedHashMap<>(
                this._functionItem.getRDDVariablesInClosure()
        );
        Map<String, Dataset<Row>> DFArgumentValues = new LinkedHashMap<>(this._functionItem.getDFVariablesInClosure());

        for (int i = 0; i < this._functionArguments.size(); i++) {
            argName = this._functionItem.getParameterNames().get(i);
            argIterator = this._functionArguments.get(i);

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
        if (this._hasNext) {
            Item result = this._nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE
                    + " in "
                    + this._functionItem.getIdentifier().getName()
                    + "  function",
                getMetadata()
        );
    }

    @Override
    protected boolean hasNextLocal() {
        return this._hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        this._functionBodyIterator.reset(this._currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        // ensure that recursive function calls terminate gracefully
        // the function call in the body of the deepest recursion call is never visited, never opened and never closed
        if (this.isOpen()) {
            this._functionBodyIterator.close();
        }
    }

    public void setNextResult() {
        this._nextResult = null;
        if (this._functionBodyIterator.hasNext()) {
            this._nextResult = this._functionBodyIterator.next();
        }

        if (this._nextResult == null) {
            this._hasNext = false;
            this._functionBodyIterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        if (this._isPartialApplication) {
            throw new OurBadException(
                    "Unexpected program state reached. Partially applied function calls must be evaluated locally."
            );
        }
        DynamicContext contextWithArguments = dynamicContext;
        this._functionBodyIterator = this._functionItem.getBodyIterator();
        contextWithArguments = this.createNewDynamicContextWithArguments(contextWithArguments);
        return this._functionBodyIterator.getRDD(contextWithArguments);
    }
}
