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
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.types.BuiltinTypesCatalogue;

import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ObjectProjectFunctionIterator extends HybridRuntimeIterator implements DataFrameRuntimePlan<Item> {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ProjectionLocalCursor(this.iterator, this.getChild(1), context, getMetadata());
    }

    private static final class ProjectionLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimeIterator inputPlan;
        private final RuntimeIterator keysPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private LocalCursor<Item> inputCursor;
        private List<Item> keys;

        private ProjectionLocalCursor(
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
            this.keys = getProjectionKeys();
            this.inputCursor = this.inputPlan.createLocalCursor(this.context);
        }

        @Override
        protected boolean hasNextLocal() {
            return this.inputCursor.hasNext();
        }

        @Override
        protected Item nextLocal() {
            Item item = this.inputCursor.next();
            return item.isObject() ? getProjection(item) : item;
        }

        @Override
        protected void closeLocal() {
            if (this.inputCursor != null) {
                this.inputCursor.close();
                this.inputCursor = null;
            }
            this.keys = null;
        }

        private List<Item> getProjectionKeys() {
            List<Item> keys = this.keysPlan.materialize(this.context);
            if (keys.isEmpty()) {
                throw new InvalidSelectorException(
                        "Invalid Projection Key; Object projection can't be performed with zero keys: ",
                        this.metadata
                );
            }
            return keys;
        }

        private Item getProjection(Item object) {
            ArrayList<String> finalKeys = new ArrayList<>();
            ArrayList<Item> finalValues = new ArrayList<>();
            for (Item keyItem : this.keys) {
                String key = keyItem.getStringValue();
                Item value = object.getItemByKey(key);
                if (value != null) {
                    finalKeys.add(key);
                    finalValues.add(value);
                }
            }
            return ItemFactory.getInstance().createObjectItem(finalKeys, finalValues, this.metadata, true);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;

    public ObjectProjectFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.iterator = arguments.get(0);
    }

    private List<Item> getProjectionKeys(DynamicContext context) {
        List<Item> keys = this.getChild(1).materialize(context);
        if (keys.isEmpty()) {
            throw new InvalidSelectorException(
                    "Invalid Projection Key; Object projection can't be performed with zero keys: ",
                    getMetadata()
            );
        }
        return keys;
    }

    private Item getProjection(Item objItem, List<Item> keys) {
        ArrayList<String> finalKeylist = new ArrayList<>();
        ArrayList<Item> finalValueList = new ArrayList<>();
        for (Item keyItem : keys) {
            String key = keyItem.getStringValue();
            Item value = objItem.getItemByKey(key);
            if (value != null) {
                finalKeylist.add(key);
                finalValueList.add(value);
            }
        }
        return ItemFactory.getInstance()
            .createObjectItem(finalKeylist, finalValueList, getMetadata(), true);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);
        List<Item> projectionKeys = getProjectionKeys(context);
        FlatMapFunction<Item, Item> transformation = new ObjectProjectClosure(
                projectionKeys,
                getMetadata()
        );
        return childRDD.flatMap(transformation);
    }

    @Override
    public JSoundDataFrame getNativeDataFrame(DynamicContext context) {
        JSoundDataFrame childDataFrame = this.getChild(0).getDataFrame(context);
        String object = FlworDataFrameUtils.createTempView(childDataFrame.getDataFrame());
        if (!childDataFrame.getItemType().isObjectItemType()) {
            return childDataFrame;
        }
        List<String> fieldNames = childDataFrame.getKeys();

        List<String> keys = new ArrayList<>();
        for (Item keyItem : getProjectionKeys(context)) {
            String key = keyItem.getStringValue();
            if (fieldNames.contains(key)) {
                keys.add(key);
            }
        }
        if (keys.isEmpty()) {
            return childDataFrame.evaluateSQL(
                String.format(
                    "SELECT NULL as `%s` FROM %s",
                    SparkSessionManager.emptyObjectJSONiqItemColumnName,
                    object
                ),
                BuiltinTypesCatalogue.objectItem
            );
        }
        String projectionVariables = FlworDataFrameUtils.getSQLProjection(keys, false);
        JSoundDataFrame result = childDataFrame.evaluateSQL(
            String.format(
                "SELECT %s FROM %s",
                projectionVariables,
                object
            ),
            BuiltinTypesCatalogue.objectItem
        );
        return result;
    }
}
