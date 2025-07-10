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

package org.rumbledb.runtime.functions.arrays;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArrayFlattenFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private RuntimeIterator iterator;
    private Queue<Item> nextResults; // queue that holds the results created by the current item in inspection

    public ArrayFlattenFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.iterator = arguments.get(0);
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
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " FLATTEN function",
                getMetadata()
        );
    }

    @Override
    public void openLocal() {
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.nextResults = new LinkedList<>();

        setNextResult();
    }

    public void setNextResult() {
        while (this.iterator.hasNext()) {
            Item item = this.iterator.next();
            flatten(Collections.singletonList(item));
            if (!(this.nextResults.isEmpty())) {
                break;
            }
        }
        if (this.nextResults.isEmpty()) {
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }

    private void flatten(List<Item> items) {
        for (Item item : items) {
            if (item.isArray()) {
                flatten(item.getItems());
            } else {
                this.nextResults.add(item);
            }
        }
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal() {
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.nextResults = new LinkedList<>();

        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.iterator.close();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(dynamicContext);
        FlatMapFunction<Item, Item> transformation = new ArrayFlattenClosure();
        return childRDD.flatMap(transformation);
    }
}
