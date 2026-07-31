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

package org.rumbledb.runtime.functions.sequences.general;

import org.rumbledb.runtime.HybridRuntimeIterator;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.List;

public class UnorderedFunctionIterator extends HybridRuntimeIterator
        implements
            DataFrameRuntimePlan<Item> {


    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> iterator;

    public UnorderedFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
        this.iterator = this.getChild(0);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return this.iterator.getCursor(context);
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);
        return childRDD;
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        HomogeneousItemDataFrame df = org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(
            this.iterator,
            context
        );
        return df;
    }

}
