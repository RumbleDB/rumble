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
 * See the License for the specific permissions and limitations under
 * the License.
 */

package org.rumbledb.runtime.functions.arrays;

import org.rumbledb.runtime.plan.AtMostOneLocalRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;


import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * F&amp;O 3.1 array:join — concatenates the members of a sequence of arrays in order into one array.
 */
public class ArrayJoinFunctionIterator extends HybridRuntimeIterator
        implements
            DataFrameRuntimePlan<Item>,
            AtMostOneLocalRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimePlan<Item> arraysIterator;

    public ArrayJoinFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 1) {
            throw new OurBadException("array:join must have exactly one argument.");
        }
        this.arraysIterator = arguments.get(0);
    }


    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return createResult(this.arraysIterator.materialize(context));
    }

    private Item createResult(List<Item> arrays) {
        List<List<Item>> joined = new ArrayList<>();
        for (Item arrayItem : arrays) {
            if (!arrayItem.isArray()) {
                throw new UnexpectedTypeException(
                        "Type error; array:join expects a sequence of arrays.",
                        getMetadata()
                );
            }
            int n = arrayItem.getSize();
            for (int i = 0; i < n; i++) {
                joined.add(new ArrayList<>(arrayItem.getSequenceAt(i)));
            }
        }
        // when joining, we always create a sequence array for now
        return ItemFactory.getInstance().createSequenceArrayItem(joined, false);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:join is currently supported only in local execution mode."
        );
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:join is currently supported only in local execution mode."
        );
    }
}
