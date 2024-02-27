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

package org.rumbledb.runtime.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.typing.TreatIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;

import sparksoniq.spark.SparkSessionManager;

public class RangeOperationIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;
    private long left;
    private long right;
    private long index;
    public static final int PARTITION_SIZE = 1000000;

    public RangeOperationIterator(
            RuntimeIterator leftIterator,
            RuntimeIterator rightiterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(leftIterator, rightiterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightiterator;
    }

    @Override
    public boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            if (this.index == this.right) {
                this.hasNext = false;
            }
            return ItemFactory.getInstance().createLongItem(this.index++);
        }
        throw new IteratorFlowException("Invalid next call in Range Operation", getMetadata());
    }

    /**
     * Initializes the boundaries of the range.
     * 
     * @param context the dynamic context.
     * @return true if the two bounds are defined, false if one of them is the empty sequence.
     */
    public Boolean init(DynamicContext context) {
        Item left;
        Item right;
        try {
            left = this.leftIterator.materializeAtMostOneItemOrNull(this.currentDynamicContextForLocalExecution);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Range expression must have integer input, but instead received more than one item",
                    getMetadata()
            );
        }
        try {
            right = this.rightIterator.materializeAtMostOneItemOrNull(this.currentDynamicContextForLocalExecution);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Range expression must have integer input, but instead received more than one item",
                    getMetadata()
            );
        }
        if (left == null || right == null) {
            return false;
        }
        if (
            !(left.isInteger())
                || !(right.isInteger())
        ) {
            throw new UnexpectedTypeException(
                    "Range expression must have integer input, but instead received "
                        +
                        left.getDynamicType()
                        + " and "
                        + right.getDynamicType(),
                    getMetadata()
            );
        }
        try {
            this.left = left.castToIntegerValue().longValue();
            this.right = right.castToIntegerValue().longValue();
        } catch (IteratorFlowException e) {
            throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
        }
        return true;
    }

    @Override
    public void openLocal() {
        this.index = 0;
        if (init(this.currentDynamicContextForLocalExecution)) {
            if (this.right < this.left) {
                this.hasNext = false;
            } else {
                this.index = this.left;
                this.hasNext = true;
            }
        } else {
            this.hasNext = false;
        }
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        return null;
    }

    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        if (!init(this.currentDynamicContextForLocalExecution)) {
            return new JSoundDataFrame(
                    SparkSessionManager.getInstance().getOrCreateSession().emptyDataFrame(),
                    BuiltinTypesCatalogue.item
            );
        }
        return createLongInterval(this.left, this.right);
    }

    /**
     * Creates a dataframe with a sequence of increasing numbers, of type long.
     * 
     * @param left the left bound(inclusive).
     * @param right the right bound (inclusive).
     * @return
     */
    public static JSoundDataFrame createLongInterval(long left, long right) {
        List<Long> list = new ArrayList<>();
        for (long i = left; i <= right; i += PARTITION_SIZE) {
            list.add(i);
        }
        JavaRDD<Long> rdd = SparkSessionManager.getInstance()
            .getJavaSparkContext()
            .parallelize(list, list.size());
        rdd = rdd.flatMap(
            i -> LongStream.range(i, Math.min(right + 1, i + PARTITION_SIZE)).iterator()
        );
        return TreatIterator.convertToDataFrame(rdd, BuiltinTypesCatalogue.longItem);
    }

    @Override
    protected void closeLocal() {
    }

    @Override
    protected void resetLocal() {
    }
}
