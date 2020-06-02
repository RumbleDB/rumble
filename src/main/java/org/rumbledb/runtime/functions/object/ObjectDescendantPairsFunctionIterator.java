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

package org.rumbledb.runtime.functions.object;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.jsoniq.ExecutionMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ObjectDescendantPairsFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Queue<Item> nextResults; // queue that holds the results created by the current item in inspection

    public ObjectDescendantPairsFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
        this.iterator = arguments.get(0);
    }

    @Override
    public void openLocal() {
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.nextResults = new LinkedList<>();

        setNextResult();
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
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " DESCENDANT-PAIRS function",
                getMetadata()
        );
    }

    public void setNextResult() {
        while (this.iterator.hasNext()) {
            Item item = this.iterator.next();
            List<Item> singleItemList = new ArrayList<>();
            singleItemList.add(item);

            getDescendantPairs(singleItemList);
            if (!(this.nextResults.isEmpty())) {
                break;
            }
        }

        if (this.nextResults.isEmpty()) {
            this.hasNext = false;
            this.iterator.close();
        } else {
            this.hasNext = true;
        }
    }

    private void getDescendantPairs(List<Item> items) {
        for (Item item : items) {
            if (item.isArray()) {
                getDescendantPairs(item.getItems());
            } else if (item.isObject()) {
                List<String> keys = item.getKeys();
                for (String key : keys) {
                    Item value = item.getItemByKey(key);

                    List<String> keyList = Collections.singletonList(key);
                    List<Item> valueList = Collections.singletonList(value);

                    Item result = ItemFactory.getInstance()
                        .createObjectItem(keyList, valueList, getMetadata());
                    this.nextResults.add(result);
                    getDescendantPairs(valueList);
                }
            } else {
                // do nothing
            }
        }
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.nextResults.clear();

        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.iterator.close();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(dynamicContext);
        FlatMapFunction<Item, Item> transformation = new ObjectDescendantPairsClosure(getMetadata());
        return childRDD.flatMap(transformation);
    }
}
