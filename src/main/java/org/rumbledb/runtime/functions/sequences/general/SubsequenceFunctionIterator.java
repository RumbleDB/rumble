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
package org.rumbledb.runtime.functions.sequences.general;

import java.io.Serial;
import java.math.BigInteger;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import lombok.NonNull;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.misc.RangeOperationIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.spark.SparkSessionManager;

public class SubsequenceFunctionIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item>, DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan sequenceIterator;
    private final ItemRuntimePlan positionIterator;
    private final ItemRuntimePlan lengthIterator;

    private final RangeOperationIterator rangeOperationIterator;

    public SubsequenceFunctionIterator(List<ItemRuntimePlan> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
        this.sequenceIterator = this.getChild(0);
        this.positionIterator = this.getChild(1);
        this.lengthIterator = this.getChildren().size() == 3 ? this.getChild(2) : null;

        if (this.sequenceIterator instanceof RangeOperationIterator rangeOperationIterator) {
            this.rangeOperationIterator = rangeOperationIterator;
        } else this.rangeOperationIterator = null;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        if (this.rangeOperationIterator != null) {
            // Generate only the selected values, without walking the skipped prefix.
            return new IteratorLocalCursor<>(
                    () -> {
                        RangeOperationIterator.Bounds bounds = getRangeSliceBounds(context);
                        return bounds.items();
                    },
                    getMetadata());
        }
        return new EvaluationCursor(
                this.sequenceIterator, this.positionIterator, this.lengthIterator, context, getMetadata());
    }

    private SubsequenceBounds getBounds(DynamicContext context) {
        return evaluateBounds(this.positionIterator, this.lengthIterator, context);
    }

    private static SubsequenceBounds evaluateBounds(
            ItemRuntimePlan position, ItemRuntimePlan length, DynamicContext context) {
        double start = position.materializeFirstOrNull(context).getDoubleValue();
        Double count =
                length == null ? null : length.materializeFirstOrNull(context).getDoubleValue();
        return new SubsequenceBounds(start, count);
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        if (this.rangeOperationIterator != null) {
            return createNativeDataFrame(context).toRDD(getMetadata());
        }
        SubsequenceBounds.Slice slice = getBounds(context).slice(BigInteger.valueOf(Long.MAX_VALUE));
        long offset = slice.offset().longValueExact();
        long end = slice.end().longValueExact();
        if (offset == end) {
            return SparkSessionManager.getInstance().getJavaSparkContext().emptyRDD();
        }
        JavaRDD<Item> child = this.sequenceIterator.getRDD(context);
        if (offset == 0 && end == Long.MAX_VALUE) {
            return child;
        }
        return child.zipWithIndex()
                .filter(input -> input._2() >= offset && input._2() < end)
                .map(input -> input._1());
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        if (this.rangeOperationIterator != null) {
            RangeOperationIterator.Bounds bounds = getRangeSliceBounds(context);
            return RangeOperationIterator.createInterval(bounds, getRuntimeStaticContext());
        }
        SubsequenceBounds.Slice slice = getBounds(context).slice(BigInteger.valueOf(Long.MAX_VALUE));
        long offset = slice.offset().longValueExact();
        long end = slice.end().longValueExact();
        HomogeneousItemDataFrame input = ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(this.sequenceIterator, context);
        Dataset<Row> rows = input.getDataFrame();
        if (offset == end) {
            return new HomogeneousItemDataFrame(rows.limit(0), input.getItemType());
        }
        // Spark's limit accepts only int. Apply it before indexing when possible.
        if (end <= Integer.MAX_VALUE) {
            rows = rows.limit((int) end);
        }
        if (offset == 0 && (end <= Integer.MAX_VALUE || end == Long.MAX_VALUE)) {
            return new HomogeneousItemDataFrame(rows, input.getItemType());
        }
        String index = SparkSessionManager.temporaryColumnName;
        rows = FlworDataFrameUtils.zipWithIndex(rows, 0L, index);
        rows = rows.filter(rows.col(index).geq(offset).and(rows.col(index).lt(end)))
                .drop(index);
        return new HomogeneousItemDataFrame(rows, input.getItemType());
    }

    /** Counts a sliced range without constructing or scanning its DataFrame. */
    public BigInteger getRangeCount(DynamicContext context) {
        if (this.rangeOperationIterator == null) {
            return null;
        }
        return getRangeSliceBounds(context).size();
    }

    private RangeOperationIterator.Bounds getRangeSliceBounds(DynamicContext context) {
        RangeOperationIterator.Bounds range = this.rangeOperationIterator.getBounds(context);
        SubsequenceBounds.Slice slice = getBounds(context).slice(range.size());
        if (slice.length().signum() == 0) {
            return new RangeOperationIterator.Bounds(1, 0);
        }
        // The slice describes positions; translate them back to exact integer values.
        // A small slice can still contain values outside the long range.
        BigInteger first = range.first().add(slice.offset());
        return new RangeOperationIterator.Bounds(
                first, first.add(slice.length()).subtract(BigInteger.ONE));
    }

    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final ItemRuntimePlan sequencePlan;
        private final ItemRuntimePlan positionPlan;
        private final ItemRuntimePlan lengthPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> sequenceCursor;
        private long currentLength;

        private EvaluationCursor(
                @NonNull ItemRuntimePlan sequencePlan,
                @NonNull ItemRuntimePlan positionPlan,
                ItemRuntimePlan lengthPlan,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata) {
            super(metadata);
            this.sequencePlan = sequencePlan;
            this.positionPlan = positionPlan;
            this.lengthPlan = lengthPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            SubsequenceBounds.Slice slice = evaluateBounds(this.positionPlan, this.lengthPlan, this.context)
                    .slice(BigInteger.valueOf(Long.MAX_VALUE));
            this.currentLength = slice.length().longValueExact();
            if (this.currentLength == 0) {
                return;
            }
            long offset = slice.offset().longValueExact();

            this.sequenceCursor = this.sequencePlan.getCursor(this.context);
            long currentPosition = 0;
            while (currentPosition < offset && this.sequenceCursor.hasNext()) {
                this.sequenceCursor.next();
                currentPosition++;
            }
        }

        @Override
        protected boolean hasNextLocal() {
            return this.currentLength != 0 && this.sequenceCursor != null && this.sequenceCursor.hasNext();
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw exhausted();
            }
            Item result = this.sequenceCursor.next();
            this.currentLength--;
            return result;
        }

        @Override
        protected void closeLocal() {
            if (this.sequenceCursor != null) {
                this.sequenceCursor.close();
                this.sequenceCursor = null;
            }
        }

        private RuntimeException exhausted() {
            return new IteratorFlowException(
                    IteratorFlowException.FLOW_EXCEPTION_MESSAGE + "subsequence function", this.metadata);
        }
    }
}
