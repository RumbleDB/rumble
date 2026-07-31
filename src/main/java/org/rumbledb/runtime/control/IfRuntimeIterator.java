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

import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.plan.UpdatingRuntimePlan;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.io.Serial;
import java.util.List;

public class IfRuntimeIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item>,
            UpdatingRuntimePlan {


    @Serial
    private static final long serialVersionUID = 1L;
    private RuntimePlan<Item> selectedIterator = null;

    public IfRuntimeIterator(
            RuntimePlan<Item> condition,
            RuntimePlan<Item> branch,
            RuntimePlan<Item> elseBranch,
            RuntimeStaticContext staticContext
    ) {
        super(
            List.of(
                condition,
                branch,
                elseBranch
            ),
            staticContext
        );
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ConditionalLocalCursor<>(
                getChild(0),
                getChild(1),
                getChild(2),
                context,
                this.getRuntimeStaticContext().getMetadata()
        );
    }



    public RuntimePlan<Item> selectApplicableIterator(
            DynamicContext dynamicContext
    ) {
        RuntimePlan<Item> condition = this.getChild(0);
        boolean effectiveBooleanValue = EffectiveBooleanValue.evaluate(condition, dynamicContext);
        if (effectiveBooleanValue) {
            return this.getChild(1);
        } else {
            return this.getChild(2);
        }
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        RuntimePlan<Item> iterator = selectApplicableIterator(
            dynamicContext
        );
        return iterator.getRDD(dynamicContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        RuntimePlan<Item> iterator = selectApplicableIterator(
            dynamicContext
        );

        return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(iterator, dynamicContext);
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!isUpdating()) {
            return new PendingUpdateList();
        }

        RuntimePlan<Item> iterator = selectApplicableIterator(context);
        return UpdatingRuntimePlan.get(iterator, context);
    }
}
