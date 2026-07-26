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

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.LongStream;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.typing.TreatIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;

import org.rumbledb.types.SequenceType;
import sparksoniq.spark.SparkSessionManager;

public class RangeOperationIterator extends HybridRuntimeIterator {


    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator leftIterator;
    private final RuntimeIterator rightIterator;
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
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new Cursor(this.leftIterator, this.rightIterator, context, getMetadata());
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

    @Override
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
        return createLongInterval(this.left, this.right, this.getRuntimeStaticContext());
    }

    /**
     * Creates a dataframe with a sequence of increasing numbers, of type long.
     * 
     * @param left the left bound(inclusive).
     * @param right the right bound (inclusive).
     * @return
     */
    public static JSoundDataFrame createLongInterval(long left, long right, RuntimeStaticContext staticContext) {
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

    @Override
    protected void closeLocal() {
    }

    private static final class Cursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> leftPlan;
        private final RuntimePlan<Item> rightPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private long rightBound;
        private long position;
        private boolean hasNext;

        private Cursor(
                RuntimePlan<Item> leftPlan,
                RuntimePlan<Item> rightPlan,
                DynamicContext context,
                ExceptionMetadata metadata
        ) {
            this.leftPlan = Objects.requireNonNull(leftPlan, "left plan cannot be null");
            this.rightPlan = Objects.requireNonNull(rightPlan, "right plan cannot be null");
            this.context = Objects.requireNonNull(context, "dynamic context cannot be null");
            this.metadata = Objects.requireNonNull(metadata, "metadata cannot be null");
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
                        this.metadata
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
                throw new IteratorFlowException("Invalid next call in Range Operation", this.metadata);
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
                        this.metadata
                );
            }
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext leftContext = this.leftIterator.generateNativeQuery(nativeClauseContext);
        if (leftContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext rightContext = this.rightIterator.generateNativeQuery(
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
