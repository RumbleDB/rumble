/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.misc;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.types.DecimalType;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.typing.TreatIterator;
import org.rumbledb.spark.SparkSessionManager;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

public class RangeOperationIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, DataFrameRuntimePlan<Item>, NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan leftIterator;
    private final ItemRuntimePlan rightIterator;
    public static final int PARTITION_SIZE = 1000000;

    public RangeOperationIterator(
            ItemRuntimePlan leftIterator, ItemRuntimePlan rightiterator, RuntimeStaticContext staticContext) {
        super(Arrays.asList(leftIterator, rightiterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightiterator;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> getBounds(context).items(), getMetadata());
    }

    /**
     * Exact inclusive endpoints; an empty range has first greater than last.
     * xs:integer can exceed Java's long range. Keep endpoints exact for generation,
     * counting, comparisons, and subsequence slicing; longValue() would silently wrap them.
     */
    public record Bounds(BigInteger first, BigInteger last) implements Serializable {
        private static final BigInteger MAX_DECIMAL38 =
                BigInteger.TEN.pow(DecimalType.MAX_PRECISION()).subtract(BigInteger.ONE);

        public Bounds(long first, long last) {
            this(BigInteger.valueOf(first), BigInteger.valueOf(last));
        }

        public BigInteger size() {
            return this.last.subtract(this.first).add(BigInteger.ONE).max(BigInteger.ZERO);
        }

        public boolean fitsLong() {
            // BigInteger.bitLength excludes the sign bit, including for negative values.
            return this.first.bitLength() < 64 && this.last.bitLength() < 64;
        }

        public boolean fitsSparkPrecision() {
            return this.first.abs().compareTo(MAX_DECIMAL38) <= 0
                    && this.last.abs().compareTo(MAX_DECIMAL38) <= 0;
        }

        public Iterator<Item> items() {
            // Both iterators are lazy. Keep primitive iteration when every value fits in long.
            if (fitsLong()) {
                return LongStream.rangeClosed(this.first.longValueExact(), this.last.longValueExact())
                        .mapToObj(ItemFactory.getInstance()::createLongItem)
                        .iterator();
            }
            return Stream.iterate(
                            this.first, value -> value.compareTo(this.last) <= 0, value -> value.add(BigInteger.ONE))
                    .map(ItemFactory.getInstance()::createIntegerItem)
                    .iterator();
        }
    }

    /** Evaluates endpoints once, without constructing or scanning the range. */
    public Bounds getBounds(DynamicContext context) {
        Item left = materializeBound(this.leftIterator, context);
        Item right = materializeBound(this.rightIterator, context);
        if (left == null || right == null) {
            return new Bounds(1, 0);
        }
        return new Bounds(integerBound(left), integerBound(right));
    }

    private Item materializeBound(ItemRuntimePlan plan, DynamicContext context) {
        try {
            return plan.materializeAtMostOne(context);
        } catch (MoreThanOneItemException exception) {
            throw new UnexpectedTypeException(
                    "Range expression must have integer input, but instead received more than one item", getMetadata());
        }
    }

    private BigInteger integerBound(Item item) {
        if (!item.isInteger() && !item.isUntypedAtomic()) {
            throw new UnexpectedTypeException(
                    "Range expression must have integer input, but instead received " + item.getDynamicType(),
                    getMetadata());
        }
        return item.castToIntegerValue();
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        return createInterval(getBounds(context), getRuntimeStaticContext());
    }

    public static HomogeneousItemDataFrame createInterval(Bounds bounds, RuntimeStaticContext staticContext) {
        if (bounds.size().signum() == 0) {
            return createLongInterval(1, 0, staticContext);
        }
        if (bounds.fitsLong()) {
            return createLongInterval(
                    bounds.first().longValueExact(), bounds.last().longValueExact(), staticContext);
        }
        // Spark represents xs:integer as decimal(38, 0). Never silently narrow a value.
        if (!bounds.fitsSparkPrecision()) {
            throw new RumbleException(
                    "Range endpoints exceed Spark's supported integer precision (38 digits).",
                    staticContext.getMetadata());
        }
        BigInteger partitionSize = BigInteger.valueOf(PARTITION_SIZE);
        // Send partition starts to Spark; each worker generates its own values lazily.
        List<BigInteger> starts = new ArrayList<>();
        for (BigInteger start = bounds.first(); start.compareTo(bounds.last()) <= 0; start = start.add(partitionSize)) {
            starts.add(start);
        }
        JavaRDD<BigDecimal> values = SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .parallelize(starts, starts.size())
                .flatMap(start -> {
                    BigInteger end = start.add(partitionSize).min(bounds.last().add(BigInteger.ONE));
                    return Stream.iterate(start, value -> value.compareTo(end) < 0, value -> value.add(BigInteger.ONE))
                            .map(BigDecimal::new)
                            .iterator();
                });
        return TreatIterator.convertToDataFrame(values, BuiltinTypesCatalogue.integerItem, staticContext);
    }

    /**
     * Creates a dataframe with a sequence of increasing numbers, of type long.
     *
     * @param left the left bound(inclusive).
     * @param right the right bound (inclusive).
     * @return
     */
    public static HomogeneousItemDataFrame createLongInterval(
            long left, long right, RuntimeStaticContext staticContext) {
        if (left > right) {
            return TreatIterator.convertToDataFrame(
                    SparkSessionManager.getInstance().getJavaSparkContext().emptyRDD(),
                    BuiltinTypesCatalogue.longItem,
                    staticContext);
        }
        List<Long> list = new ArrayList<>();
        long start = left;
        while (true) {
            list.add(start);
            if (start > Long.MAX_VALUE - PARTITION_SIZE || start + PARTITION_SIZE > right) {
                break;
            }
            start += PARTITION_SIZE;
        }
        JavaRDD<Long> rdd =
                SparkSessionManager.getInstance().getJavaSparkContext().parallelize(list, list.size());
        rdd = rdd.flatMap(i -> {
            long end = i > Long.MAX_VALUE - (PARTITION_SIZE - 1) ? right : Math.min(right, i + PARTITION_SIZE - 1);
            return LongStream.rangeClosed(i, end).iterator();
        });
        return TreatIterator.convertToDataFrame(rdd, BuiltinTypesCatalogue.longItem, staticContext);
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext leftContext = NativeQueryRuntimePlan.generate(this.leftIterator, nativeClauseContext);
        if (leftContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext rightContext =
                NativeQueryRuntimePlan.generate(this.rightIterator, new NativeClauseContext(leftContext, null, null));
        if (rightContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        // Spark sequence() cannot represent arbitrary-precision integer endpoints.
        if (!leftContext.getResultingType().getItemType().isSubtypeOf(BuiltinTypesCatalogue.longItem)
                || !rightContext.getResultingType().getItemType().isSubtypeOf(BuiltinTypesCatalogue.longItem)) {
            return NativeClauseContext.NoNativeQuery;
        }
        return new NativeClauseContext(
                rightContext,
                String.format("sequence(%s, %s)", leftContext.getResultingQuery(), rightContext.getResultingQuery()),
                new SequenceType(
                        leftContext
                                .getResultingType()
                                .getItemType()
                                .findLeastCommonSuperTypeWith(
                                        rightContext.getResultingType().getItemType()),
                        SequenceType.Arity.ZeroOrMore));
    }
}
