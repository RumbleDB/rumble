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
package org.rumbledb.runtime.functions.input;

import java.io.Serial;
import java.math.BigInteger;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import lombok.NonNull;
import scala.Tuple2;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentValueException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.spark.SparkSessionManager;

public class ParallelizeFunctionIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan sequenceIterator;
    private final ItemRuntimePlan partitionsIterator;

    public ParallelizeFunctionIterator(List<ItemRuntimePlan> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
        this.sequenceIterator = this.getChild(0);
        this.partitionsIterator = this.getChildren().size() > 1 ? this.getChild(1) : null;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        if (this.partitionsIterator == null) {
            return this.sequenceIterator.getCursor(context);
        }
        return new EvaluationCursor(this.sequenceIterator, this.partitionsIterator, context, getMetadata());
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        if (this.partitionsIterator == null) {
            return this.sequenceIterator.getRDD(context);
        }
        int partitions = evaluateNumberOfPartitions(this.partitionsIterator, context, getMetadata());
        // A distributed sequence is kept distributed rather than collected and distributed again. It is sorted by
        // position into the requested number of partitions, because repartition would not preserve the item order.
        if (this.sequenceIterator.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            return this.sequenceIterator
                    .getRDD(context)
                    .zipWithIndex()
                    .mapToPair(Tuple2::swap)
                    .sortByKey(true, partitions)
                    .values();
        }
        // A local sequence is split into contiguous slices directly, which preserves the order without a shuffle.
        return SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .parallelize(this.sequenceIterator.materialize(context), partitions);
    }

    private static int evaluateNumberOfPartitions(
            ItemRuntimePlan partitionsPlan, DynamicContext context, ExceptionMetadata metadata) {
        Item partitions;
        try {
            partitions = partitionsPlan.materializeExactlyOne(context);
        } catch (NoItemException e) {
            throw new UnexpectedTypeException(
                    "The second parameter of parallelize must be an integer, but an empty sequence is supplied.",
                    metadata);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "The second parameter of parallelize must be an integer, but a sequence with more than one item is supplied.",
                    metadata);
        }
        if (!partitions.isInteger()) {
            throw new UnexpectedTypeException(
                    "The second parameter of parallelize must be an integer, but a non-integer is supplied.", metadata);
        }
        BigInteger numberOfPartitions = partitions.getIntegerValue();
        if (numberOfPartitions.signum() <= 0 || numberOfPartitions.bitLength() >= Integer.SIZE) {
            throw new InvalidArgumentValueException(
                    "The second parameter of parallelize must be a positive integer no greater than "
                            + Integer.MAX_VALUE
                            + ", but "
                            + numberOfPartitions
                            + " is supplied.",
                    metadata);
        }
        return numberOfPartitions.intValue();
    }

    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final ItemRuntimePlan sequencePlan;
        private final ItemRuntimePlan partitionsPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> sequenceCursor;

        private EvaluationCursor(
                @NonNull ItemRuntimePlan sequencePlan,
                @NonNull ItemRuntimePlan partitionsPlan,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata) {
            super(metadata);
            this.sequencePlan = sequencePlan;
            this.partitionsPlan = partitionsPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.sequenceCursor = this.sequencePlan.getCursor(this.context);
            evaluateNumberOfPartitions(this.partitionsPlan, this.context, this.metadata);
        }

        @Override
        protected boolean hasNextLocal() {
            return this.sequenceCursor.hasNext();
        }

        @Override
        protected Item nextLocal() {
            return this.sequenceCursor.next();
        }

        @Override
        protected void closeLocal() {
            if (this.sequenceCursor != null) {
                this.sequenceCursor.close();
                this.sequenceCursor = null;
            }
        }
    }
}
