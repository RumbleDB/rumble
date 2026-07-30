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

import org.rumbledb.runtime.plan.AtMostOneLocalRuntimePlan;


import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.MapAtomicSameKey;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;

/**
 * FO 3.1 map:find($input as item()*, $key as xs:anyAtomicType) as array(*).
 */
public class MapFindFunctionIterator extends HybridRuntimeIterator
        implements
            DataFrameRuntimePlan<Item>,
            AtMostOneLocalRuntimePlan<Item> {


    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return computeResult(context);
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> inputIterator;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> keyIterator;

    public MapFindFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("map:find must have exactly two arguments.");
        }
        this.inputIterator = arguments.get(0);
        this.keyIterator = arguments.get(1);
    }

    private Item computeResult(DynamicContext context) {
        Item keyItem = null;
        try {
            keyItem = this.keyIterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "map:find expects exactly one atomic key as second argument.",
                    getMetadata()
            );
        }
        if (keyItem == null || !keyItem.isAtomic()) {
            throw new UnexpectedTypeException(
                    "map:find expects exactly one atomic key as second argument.",
                    getMetadata()
            );
        }

        List<Item> inputItems = this.inputIterator.materialize(context);
        List<List<Item>> foundMembers = new ArrayList<>();
        scanItems(inputItems, keyItem, foundMembers);
        return ItemFactory.getInstance()
            .createSequenceArrayItem(foundMembers, this.getRuntimeStaticContext().isQuerySideEffecting());
    }

    private void scanItems(List<Item> items, Item lookupKey, List<List<Item>> foundMembers) {
        for (Item item : items) {
            scanItem(item, lookupKey, foundMembers);
        }
    }

    private void scanItem(Item item, Item lookupKey, List<List<Item>> foundMembers) {
        if (item == null) {
            return;
        }
        if (item.isMap()) {
            List<Item> keys = item.getItemKeys();
            List<List<Item>> valueSequences = item.getSequenceValues();
            int size = Math.min(keys.size(), valueSequences.size());
            for (int i = 0; i < size; i++) {
                List<Item> valueSequence = valueSequences.get(i);
                if (MapAtomicSameKey.sameKey(keys.get(i), lookupKey)) {
                    if (valueSequence == null || valueSequence.isEmpty()) {
                        foundMembers.add(new ArrayList<>());
                    } else {
                        foundMembers.add(new ArrayList<>(valueSequence));
                    }
                }
                if (valueSequence != null && !valueSequence.isEmpty()) {
                    scanItems(valueSequence, lookupKey, foundMembers);
                }
            }
            return;
        }

        if (item.isArray()) {
            scanItems(item.getItemMembers(), lookupKey, foundMembers);
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("map:find is currently supported only in local execution mode.");
    }

    @Override
    public HomogeneousItemDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("map:find is currently supported only in local execution mode.");
    }
}
