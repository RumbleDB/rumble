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
import org.rumbledb.exceptions.InvalidSelectorException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.jsoniq.ExecutionMode;

import java.util.LinkedHashMap;
import java.util.List;

public class ObjectProjectFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Item nextResult;
    private List<Item> projectionKeys;

    public ObjectProjectFunctionIterator(
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
        this.projectionKeys = this.children.get(1).materialize(this.currentDynamicContextForLocalExecution);
        if (this.projectionKeys.isEmpty()) {
            throw new InvalidSelectorException(
                    "Invalid Projection Key; Object projection can't be performed with zero keys: ",
                    getMetadata()
            );
        }

        setNextResult();
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " PROJECT function",
                getMetadata()
        );
    }

    public void setNextResult() {
        this.nextResult = null;

        if (this.iterator.hasNext()) {
            Item item = this.iterator.next();
            if (item.isObject()) {
                this.nextResult = getProjection(item, this.projectionKeys);
            } else {
                this.nextResult = item;
            }
        }

        if (this.nextResult == null) {
            this.hasNext = false;
            this.iterator.close();
        } else {
            this.hasNext = true;
        }
    }

    private Item getProjection(Item objItem, List<Item> keys) {
        LinkedHashMap<String, Item> finalContent = new LinkedHashMap<>();
        for (Item keyItem : keys) {
            String key = keyItem.getStringValue();
            Item value = objItem.getItemByKey(key);
            if (value != null) {
                finalContent.put(key, value);
            }
        }
        return ItemFactory.getInstance()
            .createObjectItem(finalContent);
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.projectionKeys = this.children.get(1).materialize(this.currentDynamicContextForLocalExecution);
        if (this.projectionKeys.isEmpty()) {
            throw new InvalidSelectorException(
                    "Invalid Projection Key; Object projection can't be performed with zero keys: ",
                    getMetadata()
            );
        }

        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.iterator.close();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);
        this.projectionKeys = this.children.get(1).materialize(this.currentDynamicContextForLocalExecution);
        FlatMapFunction<Item, Item> transformation = new ObjectProjectClosure(
                this.projectionKeys
        );
        return childRDD.flatMap(transformation);
    }
}
