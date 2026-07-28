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

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

import java.io.Serial;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * F&amp;O 3.1 array:put — returns a new array with the member at a 1-based position replaced
 * by a given sequence (FOAY0001 if position is out of bounds).
 */
public class ArrayPutFunctionIterator extends HybridRuntimeIterator implements DataFrameRuntimePlan {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> computeResult(context),
                getMetadata()
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator positionIterator;
    private final RuntimeIterator memberIterator;

    public ArrayPutFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("array:put must have exactly three arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.positionIterator = arguments.get(1);
        this.memberIterator = arguments.get(2);
    }

    private Item computeResult(DynamicContext context) {
        Item arrayItem = null;
        try {
            arrayItem = this.arrayIterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:put expects exactly one array as the first argument.",
                    getMetadata()
            );
        }
        if (arrayItem == null || !arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:put must be an array.",
                    getMetadata()
            );
        }

        Item positionItem = null;
        try {
            positionItem = this.positionIterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:put expects exactly one position argument.",
                    getMetadata()
            );
        }
        if (positionItem == null || !positionItem.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; position argument to array:put must be numeric.",
                    getMetadata()
            );
        }

        BigInteger positionInteger;
        if (positionItem.isInteger()) {
            positionInteger = positionItem.castToIntegerValue();
        } else {
            positionInteger = BigInteger.valueOf(positionItem.castToIntValue());
        }

        int size = arrayItem.getSize();
        BigInteger min = BigInteger.ONE;
        BigInteger max = BigInteger.valueOf((long) size);
        if (positionInteger.compareTo(min) < 0 || positionInteger.compareTo(max) > 0) {
            throw new ArrayIndexOutOfBoundsException(
                    "array:put position out of bounds: "
                        + positionInteger
                        + ", array length: "
                        + size,
                    getMetadata()
            );
        }

        int replaceIndex = positionInteger.intValue() - 1;
        List<Item> memberSequence = this.memberIterator.materialize(context);

        if (arrayItem.isArrayOfItems() && memberSequence.size() == 1) {
            List<Item> newItems = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                if (i == replaceIndex) {
                    newItems.add(memberSequence.get(0));
                } else {
                    newItems.add(arrayItem.getItemAt(i));
                }
            }
            return ItemFactory.getInstance()
                .createArrayItem(newItems, this.getRuntimeStaticContext().isQuerySideEffecting());
        }
        List<List<Item>> newMemberSequences = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            newMemberSequences.add(i == replaceIndex ? memberSequence : arrayItem.getSequenceAt(i));
        }
        return ItemFactory.getInstance()
            .createSequenceArrayItem(newMemberSequences, this.getRuntimeStaticContext().isQuerySideEffecting());
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:put is currently supported only in local execution mode."
        );
    }

    @Override
    public JSoundDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:put is currently supported only in local execution mode."
        );
    }
}
