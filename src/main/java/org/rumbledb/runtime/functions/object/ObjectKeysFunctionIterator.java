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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ObjectKeysFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Queue<Item> nextResults; // queue that holds the results created by the current item in inspection
    private List<Item> alreadyFoundKeys;

    public ObjectKeysFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.iterator = arguments.get(0);
    }

    @Override
    public void openLocal() {
        this.alreadyFoundKeys = new ArrayList<>();
        this.nextResults = new LinkedList<>();

        if (this.iterator.isDataFrame()) {
            setResultsFromDF();
        } else {
            this.iterator.open(this.currentDynamicContextForLocalExecution);
            setResultsFromNextObjectItem();
        }
    }

    private void setResultsFromDF() {
        JSoundDataFrame childDF = this.iterator.getDataFrame(this.currentDynamicContextForLocalExecution);
        for (String key : childDF.getKeys()) {
            if (
                !key.equals(SparkSessionManager.emptyObjectJSONiqItemColumnName)
                    && !key.equals(SparkSessionManager.atomicJSONiqItemColumnName)
            ) {
                this.nextResults.add(ItemFactory.getInstance().createStringItem(key));
            }
        }
    }

    private void setResultsFromNextObjectItem() {
        while (this.iterator.hasNext()) {
            Item item = this.iterator.next();
            if (item.isObject()) { // ignore non-object items
                Item result;
                for (String key : item.getKeys()) {
                    result = ItemFactory.getInstance().createStringItem(key);
                    if (!this.alreadyFoundKeys.contains(result)) {
                        this.alreadyFoundKeys.add(result);
                        this.nextResults.add(result);
                    }
                }
                if (!this.nextResults.isEmpty()) {
                    break;
                }
            }
        }

        if (this.nextResults.isEmpty()) {
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResults.remove();
            if (this.nextResults.isEmpty()) {
                if (this.iterator.isDataFrame()) {
                    this.hasNext = false;
                } else {
                    setResultsFromNextObjectItem();
                }
            }
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " KEYS function",
                getMetadata()
        );
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal() {
        this.alreadyFoundKeys = new ArrayList<>();
        this.nextResults = new LinkedList<>();

        if (this.iterator.isDataFrame()) {
            setResultsFromDF();
        } else {
            this.iterator.reset(this.currentDynamicContextForLocalExecution);
            setResultsFromNextObjectItem();
        }
    }

    @Override
    protected void closeLocal() {
        if (!this.iterator.isDataFrame()) {
            this.iterator.close();
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);
        FlatMapFunction<Item, Item> transformation = new ObjectKeysClosure();
        return childRDD.flatMap(transformation).distinct();
    }
}
