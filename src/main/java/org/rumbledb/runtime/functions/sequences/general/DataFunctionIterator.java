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
 * Authors: Marco Schöb
 *
 */

package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.exceptions.OurBadException;

import java.util.List;

public class DataFunctionIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private List<Item> nextResults;
    private int nextIndex;
    private boolean usedContext = false;

    public DataFunctionIterator(
            List<RuntimeIterator> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
        if (!this.children.isEmpty())
            this.sequenceIterator = this.children.get(0);
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);
        FlatMapFunction<Item, Item> transformation = new AtomizationClosure();
        return childRDD.flatMap(transformation);
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        JSoundDataFrame childDF = this.sequenceIterator.getDataFrame(dynamicContext);
        if (childDF.getItemType().isAtomicItemType()) {
            return childDF;
        }
        if (childDF.isEmptySequence()) {
            return childDF;
        }
        if (childDF.getItemType().isObjectItemType()) {
            throw new CannotAtomizeException("Cannot atomize objects. Type: " + childDF.getItemType(), getMetadata());
        }
        if (childDF.getItemType().isArrayItemType()) {
            throw new CannotAtomizeException("Cannot atomize arrays. Type: " + childDF.getItemType(), getMetadata());
        }
        throw new CannotAtomizeException("Cannot atomize. Type: " + childDF.getItemType(), getMetadata());
    }


    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResults.get(this.nextIndex); // save the result to be returned
            ++this.nextIndex;
            if (this.nextIndex >= this.nextResults.size()) {
                setNextResult();
            }
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " atomization iterator",
                getMetadata()
        );
    }

    @Override
    public void openLocal() {
        if (this.sequenceIterator != null) {
            this.sequenceIterator.open(this.currentDynamicContextForLocalExecution);
        }
        this.usedContext = false;
        setNextResult();
    }

    public void setNextResult() {
        if (this.sequenceIterator != null) {
            if (!this.sequenceIterator.hasNext()) {
                this.hasNext = false;
                return;
            }
            try {
                this.nextResults = this.sequenceIterator.next().atomizedValue();
                if(this.nextResults.isEmpty()) {
                    this.hasNext = false;
                } else {
                    this.nextIndex = 0;
                    this.hasNext = true;
                }
                return;
            } catch (CannotAtomizeException e) {
                throw new CannotAtomizeException("The sequence cannot be atomized.", getMetadata());
            }
        }
        if(!this.usedContext) {
            this.usedContext = true;
            List<Item> items = this.currentDynamicContextForLocalExecution.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata());
            if (items.size() != 1) {
                throw new OurBadException("The context item is not a singleton.", getMetadata());
            }
            this.nextResults = items.get(0).atomizedValue();
            if(this.nextResults.isEmpty()) {
                this.hasNext = false;
            } else {
                this.nextIndex = 0;
                this.hasNext = true;
            }
            return;
        }
        this.hasNext = false;
    }

    @Override
    protected void closeLocal() {
        if (this.sequenceIterator != null) {
            this.sequenceIterator.close();
        }
    }

    @Override
    protected void resetLocal() {
        if (this.sequenceIterator != null)
            this.sequenceIterator.reset(this.currentDynamicContextForLocalExecution);
        this.usedContext = false;
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }
}
