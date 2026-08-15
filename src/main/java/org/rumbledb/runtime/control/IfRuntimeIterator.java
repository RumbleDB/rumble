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

package org.rumbledb.runtime.control;

import java.io.Serial;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.*;
import org.rumbledb.runtime.update.PendingUpdateList;

public class IfRuntimeIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item>, DataFrameRuntimePlan<Item>, UpdatingRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public IfRuntimeIterator(
            ItemRuntimePlan condition,
            ItemRuntimePlan branch,
            ItemRuntimePlan elseBranch,
            RuntimeStaticContext staticContext) {
        super(List.of(condition, branch, elseBranch), staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return this.selectApplicableIterator(context).getCursor(context);
    }

    public ItemRuntimePlan selectApplicableIterator(DynamicContext dynamicContext) {
        ItemRuntimePlan condition = this.getChild(0);
        boolean effectiveBooleanValue = EffectiveBooleanValue.evaluate(condition, dynamicContext);
        if (effectiveBooleanValue) {
            return this.getChild(1);
        } else {
            return this.getChild(2);
        }
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        ItemRuntimePlan iterator = selectApplicableIterator(dynamicContext);
        return iterator.getRDD(dynamicContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        ItemRuntimePlan iterator = selectApplicableIterator(dynamicContext);

        return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(iterator, dynamicContext);
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!this.staticContext.isUpdating()) {
            return new PendingUpdateList();
        }

        ItemRuntimePlan iterator = selectApplicableIterator(context);
        return UpdatingRuntimePlan.get(iterator, context);
    }
}
