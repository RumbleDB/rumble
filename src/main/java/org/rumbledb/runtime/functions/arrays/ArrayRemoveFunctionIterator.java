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

package org.rumbledb.runtime.functions.arrays;

import org.rumbledb.runtime.plan.AtMostOneLocalRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;


import java.io.Serial;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;

/**
 * F&amp;O 3.1 array:remove — returns a new array with members at the given 1-based positions omitted
 * (distinct positions; order preserved). Raises FOAY0001 if any position is out of bounds.
 */
public class ArrayRemoveFunctionIterator extends HybridRuntimeIterator
        implements
            DataFrameRuntimePlan<Item>,
            AtMostOneLocalRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimePlan<Item> arrayIterator;
    private final RuntimePlan<Item> positionsIterator;

    public ArrayRemoveFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("array:remove must have exactly two arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.positionsIterator = arguments.get(1);
    }


    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return remove(
            this.arrayIterator.materialize(context),
            this.positionsIterator.materialize(context)
        );
    }

    private Item remove(List<Item> arrays, List<Item> positionItems) {
        if (arrays.size() != 1) {
            throw new UnexpectedTypeException(
                    "array:remove expects exactly one array as the first argument.",
                    getMetadata()
            );
        }
        Item arrayItem = arrays.get(0);
        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:remove must be an array.",
                    getMetadata()
            );
        }

        int size = arrayItem.getSize();
        if (positionItems.isEmpty()) {
            return arrayItem;
        }

        Set<BigInteger> positionsToRemove = new HashSet<>();
        BigInteger min = BigInteger.ONE;
        BigInteger max = BigInteger.valueOf(size);

        for (Item p : positionItems) {
            if (!p.isNumeric()) {
                throw new UnexpectedTypeException(
                        "Type error; positions in array:remove must be numeric.",
                        getMetadata()
                );
            }
            BigInteger pos = p.isInteger() ? p.castToIntegerValue() : BigInteger.valueOf(p.castToIntValue());
            if (pos.compareTo(min) < 0 || pos.compareTo(max) > 0) {
                throw new ArrayIndexOutOfBoundsException(
                        "array:remove position out of bounds: " + pos + ", array length: " + size,
                        getMetadata()
                );
            }
            positionsToRemove.add(pos);
        }

        if (arrayItem.isArrayOfItems()) {
            List<Item> originalMembers = arrayItem.getItemMembers();
            List<Item> keptMembers = new ArrayList<>(Math.max(0, size - positionsToRemove.size()));
            for (int i = 0; i < size; i++) {
                BigInteger oneBased = BigInteger.valueOf((long) i + 1);
                if (!positionsToRemove.contains(oneBased)) {
                    keptMembers.add(originalMembers.get(i));
                }
            }
            return ItemFactory.getInstance()
                .createArrayItem(keptMembers, this.getRuntimeStaticContext().isQuerySideEffecting());
        } else {
            List<List<Item>> originalMembers = arrayItem.getSequenceMembers();
            List<List<Item>> keptMembers = new ArrayList<>(Math.max(0, size - positionsToRemove.size()));
            for (int i = 0; i < size; i++) {
                BigInteger oneBased = BigInteger.valueOf((long) i + 1);
                if (!positionsToRemove.contains(oneBased)) {
                    keptMembers.add(originalMembers.get(i));
                }
            }
            return ItemFactory.getInstance()
                .createSequenceArrayItem(keptMembers, this.getRuntimeStaticContext().isQuerySideEffecting());
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:remove is currently supported only in local execution mode."
        );
    }

    @Override
    public HomogeneousItemDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:remove is currently supported only in local execution mode."
        );
    }
}
