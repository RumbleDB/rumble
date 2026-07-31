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

package org.rumbledb.runtime.functions.sequences.general;

import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.plan.RuntimePlan;

import sparksoniq.spark.SparkSessionManager;

import lombok.NonNull;
import java.io.Serial;
import java.util.List;

public class SubsequenceFunctionIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item>,
            DataFrameRuntimePlan<Item> {


    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> sequenceIterator;
    private final RuntimePlan<Item> positionIterator;
    private final RuntimePlan<Item> lengthIterator;
    private int startPosition;
    private int length;
    private final int optimizationThreshold = 10_000_000; // do optimization only if startPosition is above this
                                                          // threshold

    public SubsequenceFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
        this.sequenceIterator = this.getChild(0);
        this.positionIterator = this.getChild(1);
        this.lengthIterator = this.getChildren().size() == 3 ? this.getChild(2) : null;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EvaluationCursor(
                this.sequenceIterator,
                this.positionIterator,
                this.lengthIterator,
                context,
                getMetadata()
        );
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);
        setInstanceVariables(context);

        if (!childRDD.isEmpty() || this.length == 0) {
            JavaPairRDD<Item, Long> zippedRDD = childRDD.zipWithIndex();
            JavaPairRDD<Item, Long> filteredRDD;
            if (this.length < 0) {
                filteredRDD = zippedRDD.filter((input) -> input._2() >= this.startPosition - 1);
            } else {
                filteredRDD = zippedRDD.filter(
                    (input) -> input._2() >= this.startPosition - 1 && input._2() < this.startPosition - 1 + this.length
                );
            }
            return filteredRDD.map(x -> x._1);
        }
        return SparkSessionManager.getInstance().getJavaSparkContext().emptyRDD();
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        if (this.startPosition < this.optimizationThreshold) {
            return getDataFrameOld(dynamicContext);
        } else
            return getDataFrameOffset(dynamicContext);
    }

    /**
     * Old implementation of getDataFrame, it is faster for low starting positions
     */
    private HomogeneousItemDataFrame getDataFrameOld(DynamicContext dynamicContext) {
        HomogeneousItemDataFrame df = org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(
            this.sequenceIterator,
            dynamicContext
        );
        setInstanceVariables(dynamicContext);

        List<FlworDataFrameColumn> allColumns = df.getColumns();

        String selectSQL = FlworDataFrameUtils.getSQLColumnProjection(allColumns, false);

        String input = FlworDataFrameUtils.createTempView(df.getDataFrame());
        if (this.length != -1) {
            df = df.evaluateSQL(
                String.format(
                    "SELECT * FROM %s LIMIT %s",
                    input,
                    Integer.toString(this.startPosition + this.length - 1)
                ),
                df.getItemType()
            );
        }

        Dataset<Row> ds = FlworDataFrameUtils.zipWithIndex(
            df.getDataFrame(),
            1L,
            SparkSessionManager.temporaryColumnName
        );

        String inputds = FlworDataFrameUtils.createTempView(ds);
        ds = ds.sparkSession()
            .sql(
                String.format(
                    "SELECT %s FROM (SELECT * FROM %s WHERE `%s` >= %s)",
                    selectSQL,
                    inputds,
                    SparkSessionManager.temporaryColumnName,
                    Integer.toString(this.startPosition)
                )
            );
        return new HomogeneousItemDataFrame(ds, df.getItemType());
    }

    /**
     * New implementation of getDataFrame using offset, it scales much better than the old implementation but is slower
     * for small values
     */
    private HomogeneousItemDataFrame getDataFrameOffset(DynamicContext dynamicContext) {
        HomogeneousItemDataFrame df = org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(
            this.sequenceIterator,
            dynamicContext
        );
        setInstanceVariables(dynamicContext);

        String input = FlworDataFrameUtils.createTempView(df.getDataFrame());
        if (this.length != -1) {
            df = df.evaluateSQL(
                String.format(
                    "SELECT * FROM %s LIMIT %s OFFSET %s",
                    input,
                    Integer.toString(this.length),
                    Integer.toString(this.startPosition - 1)
                ),
                df.getItemType()
            );
        } else {
            df = df.evaluateSQL(
                String.format(
                    "SELECT * FROM %s OFFSET %s",
                    input,
                    Integer.toString(this.startPosition - 1)
                ),
                df.getItemType()
            );
        }
        return new HomogeneousItemDataFrame(df.getDataFrame(), df.getItemType());
    }

    private void setInstanceVariables(DynamicContext context) {
        Item positionItem = this.positionIterator
            .materializeFirstOrNull(context);
        this.startPosition = (int) Math.round(positionItem.getDoubleValue());

        this.length = -1;
        if (this.getChildren().size() == 3) {
            Item lengthItem = this.lengthIterator
                .materializeFirstOrNull(context);
            this.length = (int) Math.round(lengthItem.getDoubleValue());
        }
    }

    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> sequencePlan;
        private final RuntimePlan<Item> positionPlan;
        private final RuntimePlan<Item> lengthPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> sequenceCursor;
        private int startPosition;
        private int currentLength;

        private EvaluationCursor(
                @NonNull RuntimePlan<Item> sequencePlan,
                @NonNull RuntimePlan<Item> positionPlan,
                RuntimePlan<Item> lengthPlan,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata
        ) {
            super(metadata);
            this.sequencePlan = sequencePlan;
            this.positionPlan = positionPlan;
            this.lengthPlan = lengthPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            Item positionItem = this.positionPlan.materializeFirstOrNull(this.context);
            this.startPosition = (int) Math.round(positionItem.getDoubleValue());

            this.currentLength = -1;
            if (this.lengthPlan != null) {
                Item lengthItem = this.lengthPlan.materializeFirstOrNull(this.context);
                this.currentLength = (int) Math.round(lengthItem.getDoubleValue());
            }
            if (this.startPosition <= 0 && this.currentLength != -1) {
                this.currentLength += this.startPosition - 1;
            }
            if (this.currentLength == 0) {
                return;
            }

            this.sequenceCursor = this.sequencePlan.getCursor(this.context);
            int currentPosition = 1;
            while (currentPosition < this.startPosition && this.sequenceCursor.hasNext()) {
                this.sequenceCursor.next();
                currentPosition++;
            }
        }

        @Override
        protected boolean hasNextLocal() {
            return this.currentLength != 0
                && this.sequenceCursor != null
                && this.sequenceCursor.hasNext();
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw exhausted();
            }
            if (this.currentLength < -1) {
                throw new OurBadException("Unexpected length value found.");
            }
            Item result = this.sequenceCursor.next();
            if (this.currentLength > 0) {
                this.currentLength--;
            }
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
                    IteratorFlowException.FLOW_EXCEPTION_MESSAGE + "subsequence function",
                    this.metadata
            );
        }

    }
}
