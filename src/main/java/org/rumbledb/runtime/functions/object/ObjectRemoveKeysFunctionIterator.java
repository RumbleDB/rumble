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
import org.rumbledb.exceptions.InvalidSelectorException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ObjectRemoveKeysFunctionIterator extends HybridRuntimeIterator {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new RemovalLocalCursor(this.iterator, this.getChild(1), context, getMetadata());
    }

    private static final class RemovalLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimeIterator inputPlan;
        private final RuntimeIterator keysPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private LocalCursor<Item> inputCursor;
        private List<String> keys;

        private RemovalLocalCursor(
                RuntimeIterator inputPlan,
                RuntimeIterator keysPlan,
                DynamicContext context,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.inputPlan = inputPlan;
            this.keysPlan = keysPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.keys = getRemovalKeys();
            this.inputCursor = this.inputPlan.createLocalCursor(this.context);
        }

        @Override
        protected boolean hasNextLocal() {
            return this.inputCursor.hasNext();
        }

        @Override
        protected Item nextLocal() {
            Item item = this.inputCursor.next();
            return item.isObject() ? removeKeys(item) : item;
        }

        @Override
        protected void closeLocal() {
            if (this.inputCursor != null) {
                this.inputCursor.close();
                this.inputCursor = null;
            }
            this.keys = null;
        }

        private List<String> getRemovalKeys() {
            List<Item> removalKeys = LocalCursorUtils.materialize(this.keysPlan, this.context);
            if (removalKeys.isEmpty()) {
                throw new InvalidSelectorException(
                        "Invalid Key Removal Parameter; Object key removal can't be performed with zero keys: ",
                        this.metadata
                );
            }
            List<String> result = new ArrayList<>();
            for (Item removalKeyItem : removalKeys) {
                if (!removalKeyItem.isString()) {
                    throw new UnexpectedTypeException("Remove-keys function has non-string key args.", this.metadata);
                }
                result.add(removalKeyItem.getStringValue());
            }
            return result;
        }

        private Item removeKeys(Item object) {
            ArrayList<String> finalKeys = new ArrayList<>();
            ArrayList<Item> finalValues = new ArrayList<>();
            for (String objectKey : object.getStringKeys()) {
                if (!this.keys.contains(objectKey)) {
                    finalKeys.add(objectKey);
                    finalValues.add(object.getItemByKey(objectKey));
                }
            }
            return ItemFactory.getInstance().createObjectItem(finalKeys, finalValues, this.metadata, true);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;

    public ObjectRemoveKeysFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.iterator = arguments.get(0);
    }

    private List<String> getRemovalKeys(DynamicContext context) {
        List<Item> removalKeys = LocalCursorUtils.materialize(this.getChild(1), context);
        if (removalKeys.isEmpty()) {
            throw new InvalidSelectorException(
                    "Invalid Key Removal Parameter; Object key removal can't be performed with zero keys: ",
                    getMetadata()
            );
        }
        List<String> result = new ArrayList<>();
        for (Item removalKeyItem : removalKeys) {
            if (!removalKeyItem.isString()) {
                throw new UnexpectedTypeException("Remove-keys function has non-string key args.", getMetadata());
            }
            String removalKey = removalKeyItem.getStringValue();
            result.add(removalKey);
        }
        return result;
    }

    private Item removeKeys(Item objItem, List<String> removalKeys) {
        ArrayList<String> finalKeylist = new ArrayList<>();
        ArrayList<Item> finalValueList = new ArrayList<>();

        for (String objectKey : objItem.getStringKeys()) {
            if (!removalKeys.contains(objectKey)) {
                finalKeylist.add(objectKey);
                finalValueList.add(objItem.getItemByKey(objectKey));
            }
        }
        return ItemFactory.getInstance()
            .createObjectItem(finalKeylist, finalValueList, getMetadata(), true);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);
        List<String> removalKeys = getRemovalKeys(context);
        FlatMapFunction<Item, Item> transformation = new ObjectRemoveKeysClosure(
                removalKeys,
                getMetadata()
        );
        return childRDD.flatMap(transformation);
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        JSoundDataFrame dataFrame = this.iterator.getDataFrame(context);
        List<Item> columnsToDropItems = LocalCursorUtils.materialize(this.getChild(1), context);
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
