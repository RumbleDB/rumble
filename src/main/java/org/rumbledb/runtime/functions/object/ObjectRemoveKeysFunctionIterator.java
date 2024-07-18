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
import org.rumbledb.exceptions.InvalidSelectorException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;

public class ObjectRemoveKeysFunctionIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Item nextResult;
    private List<String> removalKeys;

    public ObjectRemoveKeysFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.iterator = arguments.get(0);
    }

    @Override
    public void openLocal() {
        startLocal();
    }

    private void startLocal() {
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        List<Item> removalKeys = this.children.get(1).materialize(this.currentDynamicContextForLocalExecution);
        if (removalKeys.isEmpty()) {
            throw new InvalidSelectorException(
                    "Invalid Key Removal Parameter; Object key removal can't be performed with zero keys: ",
                    getMetadata()
            );
        }
        this.removalKeys = new ArrayList<>();
        for (Item removalKeyItem : removalKeys) {
            if (!removalKeyItem.isString()) {
                throw new UnexpectedTypeException("Remove-keys function has non-string key args.", getMetadata());
            }
            String removalKey = removalKeyItem.getStringValue();
            this.removalKeys.add(removalKey);
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
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " REMOVE-KEYS function",
                getMetadata()
        );
    }

    public void setNextResult() {
        this.nextResult = null;

        if (this.iterator.hasNext()) {
            Item item = this.iterator.next();
            if (item.isObject()) {
                this.nextResult = removeKeys(item, this.removalKeys);
            } else {
                this.nextResult = item;
            }
        }

        if (this.nextResult == null) {
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }

    private Item removeKeys(Item objItem, List<String> removalKeys) {
        ArrayList<String> finalKeylist = new ArrayList<>();
        ArrayList<Item> finalValueList = new ArrayList<>();

        for (String objectKey : objItem.getKeys()) {
            if (!removalKeys.contains(objectKey)) {
                finalKeylist.add(objectKey);
                finalValueList.add(objItem.getItemByKey(objectKey));
            }
        }
        return ItemFactory.getInstance()
            .createObjectItem(finalKeylist, finalValueList, getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal() {
        startLocal();
    }

    @Override
    protected void closeLocal() {
        this.iterator.close();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);
        List<Item> removalKeys = this.children.get(1).materialize(context);
        if (removalKeys.isEmpty()) {
            throw new InvalidSelectorException(
                    "Invalid Key Removal Parameter; Object key removal can't be performed with zero keys: ",
                    getMetadata()
            );
        }

        this.removalKeys = new ArrayList<>();
        for (Item removalKeyItem : removalKeys) {
            if (!removalKeyItem.isString()) {
                throw new UnexpectedTypeException("Remove-keys function has non-string key args.", getMetadata());
            }
            String removalKey = removalKeyItem.getStringValue();
            this.removalKeys.add(removalKey);
        }
        FlatMapFunction<Item, Item> transformation = new ObjectRemoveKeysClosure(
                this.removalKeys,
                getMetadata()
        );
        return childRDD.flatMap(transformation);
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        JSoundDataFrame dataFrame = this.iterator.getDataFrame(context);
        List<Item> columnsToDropItems = this.children.get(1).materialize(context);
        if (columnsToDropItems.isEmpty()) {
            throw new InvalidSelectorException(
                    "Invalid drop-columns parameter; drop-columns can't be performed without string columns to be removed.",
                    getMetadata()
            );
        }
        String[] columnsToDrop = new String[columnsToDropItems.size()];
        int i = 0;
        for (Item columnItem : columnsToDropItems) {
            if (!columnItem.isString()) {
                throw new UnexpectedTypeException("drop-columns invoked with non-string columns", getMetadata());
            }
            columnsToDrop[i] = columnItem.getStringValue();
            ++i;
        }
        return new JSoundDataFrame(dataFrame.getDataFrame().drop(columnsToDrop), dataFrame.getItemType());
    }
}
