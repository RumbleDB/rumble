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
 */
package org.rumbledb.runtime.functions;

import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.UpdatingRuntimePlan;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic invocation of a function item that represents a builtin named function reference.
 */
public class BuiltinFunctionItemCallIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item>,
            UpdatingRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Item functionItem;
    private final List<ItemRuntimePlan> functionArguments;

    public BuiltinFunctionItemCallIterator(
            Item functionItem,
            List<ItemRuntimePlan> functionArguments,
            RuntimeStaticContext staticContext
    ) {
        super(
            functionArguments.stream().filter(arg -> arg != null).toList(),
            staticContext.toBuilder().isUpdating(true).build()
        );

        this.functionItem = functionItem;
        this.functionArguments = functionArguments;

        FunctionCallArgumentConversion.validateArity(functionItem, this.functionArguments, getMetadata());
        FunctionCallArgumentConversion.wrapAccordingToSignature(
            functionItem,
            this.functionArguments,
            staticContext
        );
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return this.getPlan().getCursor(context);
    }

    private ItemRuntimePlan getPlan() {
        return NamedFunctions.getBuiltInFunctionIterator(
            this.functionItem.getIdentifier(),
            new ArrayList<>(this.functionArguments),
            this.staticContext,
            true
        );
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        return this.getPlan().getRDD(dynamicContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(this.getPlan(), dynamicContext);
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!this.staticContext.isUpdating()) {
            return new PendingUpdateList();
        }
        return UpdatingRuntimePlan.get(this.getPlan(), context);
    }
}
