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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StaticUserDefinedFunctionCallIterator extends HybridRuntimeIterator {
    // static: functionIdentifier known at compile time

    private static final long serialVersionUID = 1L;
    // parametrized fields
    private FunctionIdentifier functionIdentifier;
    private List<RuntimeIterator> functionArguments;

    // calculated fields
    private RuntimeIterator userDefinedFunctionCallIterator;
    private Item nextResult;


    public StaticUserDefinedFunctionCallIterator(
            FunctionIdentifier functionIdentifier,
            List<RuntimeIterator> functionArguments,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.functionIdentifier = functionIdentifier;
        this.functionArguments = functionArguments;
        this.userDefinedFunctionCallIterator = null;
    }

    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public void openLocal() {
        if (this.userDefinedFunctionCallIterator == null) {
            this.userDefinedFunctionCallIterator = this.currentDynamicContextForLocalExecution.getNamedFunctions()
                .getUserDefinedFunctionCallIterator(
                    this.functionIdentifier,
                    this.getHighestExecutionMode(),
                    getMetadata(),
                    this.functionArguments
                );
        }
        this.userDefinedFunctionCallIterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in " + this.functionIdentifier.getName() + "  function",
                getMetadata()
        );
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal() {
        this.userDefinedFunctionCallIterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        // ensure that recursive function calls terminate gracefully
        // the function call in the body of the deepest recursion call is never visited, never opened and never closed
        if (this.userDefinedFunctionCallIterator != null && this.userDefinedFunctionCallIterator.isOpen()) {
            this.userDefinedFunctionCallIterator.close();
        }
    }

    public void setNextResult() {
        this.nextResult = null;
        if (this.userDefinedFunctionCallIterator.hasNext()) {
            this.nextResult = this.userDefinedFunctionCallIterator.next();
        }

        if (this.nextResult == null) {
            this.hasNext = false;
            this.userDefinedFunctionCallIterator.close();
        } else {
            this.hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        this.userDefinedFunctionCallIterator = dynamicContext.getNamedFunctions()
            .getUserDefinedFunctionCallIterator(
                this.functionIdentifier,
                this.getHighestExecutionMode(),
                getMetadata(),
                this.functionArguments
            );
        return this.userDefinedFunctionCallIterator.getRDD(dynamicContext);
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        this.userDefinedFunctionCallIterator = dynamicContext.getNamedFunctions()
            .getUserDefinedFunctionCallIterator(
                this.functionIdentifier,
                this.getHighestExecutionMode(),
                getMetadata(),
                this.functionArguments
            );
        return this.userDefinedFunctionCallIterator.getDataFrame(dynamicContext);
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<>(super.getVariableDependencies());
        for (RuntimeIterator iterator : this.functionArguments) {
            if (iterator == null) {
                continue;
            }
            result.putAll(iterator.getVariableDependencies());
        }
        return result;
    }
}
