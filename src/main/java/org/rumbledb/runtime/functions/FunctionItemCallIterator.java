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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;

public class FunctionItemCallIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    // parametrized fields
    private Item functionItem;
    private List<RuntimeIterator> functionArguments;

    // calculated fields
    private RuntimeIterator functionBodyIterator;
    private Item nextResult;
    private transient DynamicContext dynamicContextForCalls;


    public FunctionItemCallIterator(
            Item functionItem,
            List<RuntimeIterator> functionArguments,
            RuntimeStaticContext staticContext,
            boolean isTailOptimization
    ) {
        super(null, staticContext);
        for (RuntimeIterator arg : functionArguments) {
            this.children.add(arg);
        }
        this.functionItem = functionItem;
        this.functionArguments = functionArguments;
        this.functionBodyIterator = null;
        this.isUpdating = functionItem.getSignature().isUpdating();

        FunctionCallArgumentCoercion.validateArity(functionItem, this.functionArguments, getMetadata());
        FunctionCallArgumentCoercion.wrapAccordingToSignature(
            functionItem,
            this.functionArguments,
            staticContext
        );

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

    @Override
    public void openLocal() {
        if (this.functionBodyIterator == null) {
            this.functionBodyIterator = this.functionItem.getBodyIterator().deepCopy();
        }
        this.populateDynamicContextWithArguments(
            this.currentDynamicContextForLocalExecution
        );
        this.functionBodyIterator.open(this.dynamicContextForCalls);
        setNextResult();
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
