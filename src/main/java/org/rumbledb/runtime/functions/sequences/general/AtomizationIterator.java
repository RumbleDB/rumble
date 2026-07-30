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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AtomizationIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private Queue<Item> nextResults; // queue that holds the results created by the current item in inspection
    private boolean usedContext = false;

    public AtomizationIterator(
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
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResults.remove(); // save the result to be returned
            if (this.nextResults.isEmpty()) {
                // if there are no more results left in the queue, trigger calculation for the next result
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
        if (this.sequenceIterator != null)
            this.sequenceIterator.open(this.currentDynamicContextForLocalExecution);
        this.nextResults = new LinkedList<>();
        this.usedContext = false;
        setNextResult();
    }

    public void setNextResult() {
        if (this.sequenceIterator != null) {
            if (this.sequenceIterator.hasNext()) {
                this.nextResults.addAll(this.sequenceIterator.next().atomizedValue());
            }
        } else if (!this.usedContext) {
            this.usedContext = true;
            List<Item> items = this.currentDynamicContextForLocalExecution.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata());
            for (Item item : items) {
                this.nextResults.addAll(item.atomizedValue());
            }
        }
        this.hasNext = !this.nextResults.isEmpty();
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
            this.sequenceIterator.open(this.currentDynamicContextForLocalExecution);
        this.nextResults = new LinkedList<>();
        this.usedContext = false;
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }
}
