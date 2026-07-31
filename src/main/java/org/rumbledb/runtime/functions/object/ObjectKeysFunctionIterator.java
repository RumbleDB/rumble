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
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ObjectKeysFunctionIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimePlan<Item> iterator;

    public ObjectKeysFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.iterator = arguments.get(0);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ObjectKeysLocalCursor(
                this.iterator,
                context,
                getMetadata()
        );
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);
        FlatMapFunction<Item, Item> transformation = new ObjectKeysClosure();
        return childRDD.flatMap(transformation).distinct();
    }

    private static final class ObjectKeysLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> inputPlan;
        private final DynamicContext context;
        private final Set<String> seenKeys;
        private Cursor<Item> inputCursor;
        private Iterator<String> currentKeys;
        private String nextKey;

        private ObjectKeysLocalCursor(
                RuntimePlan<Item> inputPlan,
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
            this.inputCursor = this.inputPlan.getCursor(this.context);
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
