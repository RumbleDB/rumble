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

import java.util.ArrayList;
import java.util.List;

public class AtomizationIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private List<Item> results;
    private int currentIndex = 0;

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
    protected void openLocal() {
        getList();
    }

    private void getList() {
        this.results = new ArrayList<>();
        this.currentIndex = 0;
        List<Item> items;
        if (this.sequenceIterator != null) {
            items = this.sequenceIterator.materialize(this.currentDynamicContextForLocalExecution);
        } else {
            items = this.currentDynamicContextForLocalExecution.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata());
        }

        for (Item item : items) {
            this.results.addAll(item.typedValue());
        }

        this.hasNext = !this.results.isEmpty();
    }

    @Override
    protected void closeLocal() {
    }

    @Override
    protected void resetLocal() {
        getList();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (this.hasNext()) {
            if (this.currentIndex == this.results.size() - 1) {
                this.hasNext = false;
            }
            return this.results.get(this.currentIndex++);
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "atomization function", getMetadata());
    }
}
