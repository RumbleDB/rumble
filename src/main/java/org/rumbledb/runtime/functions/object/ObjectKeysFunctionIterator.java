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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class ObjectKeysFunctionIterator extends HybridRuntimeIterator {

    @Serial
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
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ObjectKeysLocalCursor(
                this.iterator,
                context,
                getMetadata()
        );
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
            if (key.equals(SparkSessionManager.mutabilityLevelColumnName)) {
                continue;
            }
            if (key.equals(SparkSessionManager.rowIdColumnName)) {
                continue;
            }
            if (key.equals(SparkSessionManager.tableLocationColumnName)) {
                continue;
            }
            if (key.equals(SparkSessionManager.pathInColumnName)) {
                continue;
            }
            if (
                !key.equals(SparkSessionManager.emptyObjectJSONiqItemColumnName)
                    && !key.equals(SparkSessionManager.nonObjectJSONiqItemColumnName)
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
                for (String key : item.getStringKeys()) {
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

    private static final class ObjectKeysLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimeIterator inputPlan;
        private final DynamicContext context;
        private final Set<String> seenKeys;
        private LocalCursor<Item> inputCursor;
        private Iterator<String> currentKeys;
        private String nextKey;

        private ObjectKeysLocalCursor(
                RuntimeIterator inputPlan,
                DynamicContext context,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.inputPlan = inputPlan;
            this.context = context;
            this.seenKeys = new HashSet<>();
        }

        @Override
        protected void openLocal() {
            this.inputCursor = this.inputPlan.createLocalCursor(this.context);
            this.inputCursor.open();
            this.currentKeys = Collections.emptyIterator();
            advance();
        }

        private void advance() {
            this.nextKey = null;
            while (true) {
                while (this.currentKeys.hasNext()) {
                    String key = this.currentKeys.next();
                    if (this.seenKeys.add(key)) {
                        this.nextKey = key;
                        return;
                    }
                }
                if (!this.inputCursor.hasNext()) {
                    return;
                }
                Item item = this.inputCursor.next();
                this.currentKeys = item.isObject()
                    ? item.getStringKeys().iterator()
                    : Collections.emptyIterator();
            }
        }

        @Override
        protected boolean hasNextLocal() {
            return this.nextKey != null;
        }

        @Override
        protected Item nextLocal() {
            if (this.nextKey == null) {
                throw invalidState("No more object keys are available.");
            }
            Item result = ItemFactory.getInstance().createStringItem(this.nextKey);
            advance();
            return result;
        }

        @Override
        protected void closeLocal() {
            if (this.inputCursor != null) {
                this.inputCursor.close();
                this.inputCursor = null;
            }
            this.currentKeys = null;
            this.nextKey = null;
            this.seenKeys.clear();
        }
    }
}
