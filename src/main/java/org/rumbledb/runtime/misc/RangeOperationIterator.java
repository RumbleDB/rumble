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

import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

import lombok.NonNull;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.typing.TreatIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;

import org.rumbledb.types.SequenceType;
import sparksoniq.spark.SparkSessionManager;

public class RangeOperationIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            DataFrameRuntimePlan<Item> {


    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimePlan<Item> leftIterator;
    private final RuntimePlan<Item> rightIterator;
    private long left;
    private long right;
    private long index;
    public static final int PARTITION_SIZE = 1000000;

    public RangeOperationIterator(
            RuntimePlan<Item> leftIterator,
            RuntimePlan<Item> rightiterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(leftIterator, rightiterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightiterator;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EvaluationCursor(this.leftIterator, this.rightIterator, context, getMetadata());
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
            left = this.leftIterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Range expression must have integer input, but instead received more than one item",
                    getMetadata()
            );
        }
        try {
            right = this.rightIterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Range expression must have integer input, but instead received more than one item",
                    getMetadata()
            );
        }
        if (left == null || right == null) {
            return false;
        }
        if (left.isUntypedAtomic()) {
            left = ItemFactory.getInstance().createIntegerItem(left.castToIntegerValue());
        }
        if (right.isUntypedAtomic()) {
            right = ItemFactory.getInstance().createIntegerItem(right.castToIntegerValue());
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
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        if (!init(context)) {
            return new HomogeneousItemDataFrame(
                    SparkSessionManager.getInstance().getOrCreateSession().emptyDataFrame(),
                    BuiltinTypesCatalogue.item
            );
        }
        return createLongInterval(this.left, this.right, this.getRuntimeStaticContext());
    }

    /**
     * Creates a dataframe with a sequence of increasing numbers, of type long.
     * 
     * @param left the left bound(inclusive).
     * @param right the right bound (inclusive).
     * @return
     */
    public static HomogeneousItemDataFrame createLongInterval(
            long left,
            long right,
            RuntimeStaticContext staticContext
    ) {
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
        return TreatIterator.convertToDataFrame(rdd, BuiltinTypesCatalogue.longItem, staticContext);
    }


    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> leftPlan;
        private final RuntimePlan<Item> rightPlan;
        private final DynamicContext context;
        private long rightBound;
        private long position;
        private boolean hasNext;

        private EvaluationCursor(
                @NonNull RuntimePlan<Item> leftPlan,
                @NonNull RuntimePlan<Item> rightPlan,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata
        ) {
            super(metadata);
            this.leftPlan = leftPlan;
            this.rightPlan = rightPlan;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            Item left = materializeBound(this.leftPlan);
            Item right = materializeBound(this.rightPlan);
            if (left == null || right == null) {
                this.hasNext = false;
                return;
            }
            if (!left.isInteger() || !right.isInteger()) {
                throw new UnexpectedTypeException(
                        "Range expression must have integer input, but instead received "
                            + left.getDynamicType()
                            + " and "
                            + right.getDynamicType(),
                        this.getMetadata()
                );
            }
            this.position = left.castToIntegerValue().longValue();
            this.rightBound = right.castToIntegerValue().longValue();
            this.hasNext = this.position <= this.rightBound;
        }

        @Override
        protected boolean hasNextLocal() {
            return this.hasNext;
        }

        @Override
        protected Item nextLocal() {
            if (!this.hasNext) {
                throw new IteratorFlowException(
                        "Invalid next call in Range Operation",
                        this.getMetadata()
                );
            }
            long result = this.position;
            if (this.position == this.rightBound) {
                this.hasNext = false;
            } else {
                this.position++;
            }
            return ItemFactory.getInstance().createLongItem(result);
        }

        @Override
        protected void closeLocal() {
            this.hasNext = false;
        }

        private Item materializeBound(RuntimePlan<Item> plan) {
            try {
                return plan.materializeAtMostOne(this.context);
            } catch (MoreThanOneItemException exception) {
                throw new UnexpectedTypeException(
                        "Range expression must have integer input, but instead received more than one item",
                        this.getMetadata()
                );
            }
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext leftContext = NativeQueryRuntimePlan.generate(
            this.leftIterator,
            nativeClauseContext
        );
        if (leftContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext rightContext = NativeQueryRuntimePlan.generate(
            this.rightIterator,
            new NativeClauseContext(leftContext, null, null)
        );
        if (rightContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        return new NativeClauseContext(
                rightContext,
                String.format("sequence(%s, %s)", leftContext.getResultingQuery(), rightContext.getResultingQuery()),
                new SequenceType(
                        leftContext.getResultingType()
                            .getItemType()
                            .findLeastCommonSuperTypeWith(rightContext.getResultingType().getItemType()),
                        SequenceType.Arity.ZeroOrMore
                )
        );
    }
}
