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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class AtomizationIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private List<Item> currentBatch;
    private int nextInBatch;

    public AtomizationIterator(
            List<RuntimeIterator> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
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
            Item result = this.currentBatch.get(this.nextInBatch); // save the result to be returned
            ++this.nextInBatch; // move the pointer to the next item in the batch
            if (this.nextInBatch >= this.currentBatch.size()) { // if we have exhausted the current batch
                fetchNextBatch(); // fetch the next batch
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
        this.sequenceIterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = false;
        fetchNextBatch();
    }

    public void fetchNextBatch() {
        if (!this.sequenceIterator.hasNext()) {
            this.hasNext = false;
            return;
        }
        try {
            this.currentBatch = this.sequenceIterator.next().atomizedValue();
            this.nextInBatch = 0;
            this.hasNext = !this.currentBatch.isEmpty();
            return;
        } catch (CannotAtomizeException e) {
            throw new CannotAtomizeException("The sequence cannot be atomized.", getMetadata());
        }
    }

    @Override
    protected void closeLocal() {
        this.sequenceIterator.close();
    }

    @Override
    protected void resetLocal() {
        this.sequenceIterator.reset(this.currentDynamicContextForLocalExecution);
        fetchNextBatch();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }
}
