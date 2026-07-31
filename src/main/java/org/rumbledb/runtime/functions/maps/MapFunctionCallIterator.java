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

package org.rumbledb.runtime.functions.maps;

import org.rumbledb.runtime.HybridRuntimeIterator;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic function call when the function item is an XDM map ({@code $map($key)}), equivalent to {@code map:get}.
 */
public class MapFunctionCallIterator extends HybridRuntimeIterator
        implements
            DataFrameRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> lookupLocally(context).iterator(),
                getMetadata()
        );
    }

    private List<Item> lookupLocally(DynamicContext context) {
        if (this.keyIterator == null) {
            throw new UnexpectedTypeException(
                    "Map function calls must have exactly one argument.",
                    getMetadata()
            );
        }
        List<Item> atomized = new ArrayList<>();
        for (Item item : this.keyIterator.materialize(context)) {
            atomized.addAll(item.atomizedValue());
        }
        if (atomized.size() != 1 || !atomized.get(0).isAtomic()) {
            throw new UnexpectedTypeException(
                    "Map lookup key must atomize to a single atomic value [err:XPTY0004].",
                    getMetadata()
            );
        }
        List<Item> result = this.mapItem.getSequenceByKey(atomized.get(0));
        return result == null ? List.of() : result;
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final Item mapItem;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> keyIterator;

    public MapFunctionCallIterator(
            Item mapItem,
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> keyIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            keyIterator == null ? null : java.util.Collections.singletonList(keyIterator),
            staticContext
        );
        this.mapItem = mapItem;
        this.keyIterator = keyIterator;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("Map function calls are currently supported only in local execution mode.");
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("Map function calls are currently supported only in local execution mode.");
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeClauseContext.NoNativeQuery;
    }
}
