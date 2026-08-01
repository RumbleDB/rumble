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

import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSelectorException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;

import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ObjectProjectFunctionIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ProjectionLocalCursor(this.iterator, this.getChild(1), context, getMetadata());
    }

    private static final class ProjectionLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> inputPlan;
        private final RuntimePlan<Item> keysPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> inputCursor;
        private List<Item> keys;

        private ProjectionLocalCursor(
                RuntimePlan<Item> inputPlan,
                RuntimePlan<Item> keysPlan,
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
            this.inputCursor = this.inputPlan.getCursor(this.context);
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
    private RuntimePlan<Item> iterator;

    public ObjectProjectFunctionIterator(
            List<RuntimePlan<Item>> arguments,
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

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);
        List<Item> projectionKeys = getProjectionKeys(context);
        FlatMapFunction<Item, Item> transformation = new ObjectProjectClosure(
                projectionKeys,
                getMetadata()
        );
        return childRDD.flatMap(transformation);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        HomogeneousItemDataFrame childDataFrame = ItemRuntimeDataFrameFactory.INSTANCE
            .fromPlan(this.getChild(0), context);
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
        HomogeneousItemDataFrame result = childDataFrame.evaluateSQL(
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
