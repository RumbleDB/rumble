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

package org.rumbledb.runtime.functions.input;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import sparksoniq.spark.SparkSessionManager;

import lombok.NonNull;
import java.io.Serial;
import java.util.List;

public class ParallelizeFunctionIterator extends HybridRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator sequenceIterator;
    private final RuntimeIterator partitionsIterator;

    public ParallelizeFunctionIterator(List<RuntimeIterator> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
        this.sequenceIterator = this.getChild(0);
        this.partitionsIterator = this.getChildren().size() > 1 ? this.getChild(1) : null;
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        if (this.partitionsIterator == null) {
            return this.sequenceIterator.createLocalCursor(context);
        }
        return new Cursor(
                this.sequenceIterator,
                this.partitionsIterator,
                context,
                getMetadata()
        );
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        if (this.sequenceIterator.isDataFrame()) {
            JSoundDataFrame dataFrame = this.sequenceIterator.getDataFrame(context);
            JavaRDD<Item> rdd = dataFrame.toRDD(this.getMetadata());
            if (this.partitionsIterator == null) {
                return rdd;
            }
            return rdd.repartition(
                getNumberOfPartitions(this.partitionsIterator, context, getMetadata()).getIntValue()
            );
        }
        List<Item> contents = this.sequenceIterator.materialize(context);
        if (this.partitionsIterator == null) {
            return SparkSessionManager.getInstance().getJavaSparkContext().parallelize(contents);
        }
        Item partitions = getNumberOfPartitions(this.partitionsIterator, context, getMetadata());
        return SparkSessionManager.getInstance()
            .getJavaSparkContext()
            .parallelize(contents, partitions.getIntValue());
    }

    private static Item getNumberOfPartitions(
            RuntimePlan<Item> partitionsPlan,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        Item partitions;
        try {
            partitions = partitionsPlan.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "The second parameter of parallelize must be an integer, but a sequence with more than one item is supplied.",
                    metadata
            );
        }
        if (partitions == null) {
            throw new UnexpectedTypeException(
                    "The second parameter of parallelize must be an integer, but an empty sequence is supplied.",
                    metadata
            );
        }
        if (!partitions.isInteger()) {
            throw new UnexpectedTypeException(
                    "The second parameter of parallelize must be an integer, but a non-integer is supplied.",
                    metadata
            );
        }
        return partitions;
    }

    @Override
    protected void openLocal() {
        this.sequenceIterator.open(this.currentDynamicContextForLocalExecution);
        if (this.partitionsIterator != null) {
            getNumberOfPartitions(
                this.partitionsIterator,
                this.currentDynamicContextForLocalExecution,
                getMetadata()
            );
        }
    }

    @Override
    protected void closeLocal() {
        this.sequenceIterator.close();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.sequenceIterator.hasNext();
    }

    @Override
    protected Item nextLocal() {
        return this.sequenceIterator.next();
    }

    private static final class Cursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> sequencePlan;
        private final RuntimePlan<Item> partitionsPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private LocalCursor<Item> sequenceCursor;

        private Cursor(
                @NonNull RuntimePlan<Item> sequencePlan,
                @NonNull RuntimePlan<Item> partitionsPlan,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata
        ) {
            super(metadata);
            this.sequencePlan = sequencePlan;
            this.partitionsPlan = partitionsPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.sequenceCursor = this.sequencePlan.createLocalCursor(this.context);
            getNumberOfPartitions(this.partitionsPlan, this.context, this.metadata);
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
