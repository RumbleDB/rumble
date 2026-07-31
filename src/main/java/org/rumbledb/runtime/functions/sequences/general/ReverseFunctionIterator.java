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

import org.rumbledb.runtime.HybridRuntimeIterator;

import org.apache.log4j.LogManager;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.plan.RuntimePlan;

import scala.Tuple2;
import sparksoniq.spark.SparkSessionManager;

import lombok.NonNull;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ReverseFunctionIterator extends HybridRuntimeIterator
        implements
            DataFrameRuntimePlan<Item> {


    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> sequenceIterator;

    public ReverseFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
        this.sequenceIterator = this.getChild(0);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EvaluationCursor(this.sequenceIterator, context, getMetadata());
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);
        JavaPairRDD<Long, Item> zippedRDD = childRDD.zipWithIndex().mapToPair(Tuple2::swap);
        return zippedRDD.sortByKey(false).map(item -> item._2);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        HomogeneousItemDataFrame childDataFrame = org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory.INSTANCE
            .fromPlan(this.getChild(0), context);
        String viewName = FlworDataFrameUtils.createTempView(childDataFrame.getDataFrame());
        String selectSQL = childDataFrame.getSQLColumnProjection(false);
        LogManager.getLogger("ReverseFunctioniterator")
            .info(
                String.format(
                    "SELECT %s FROM (SELECT %s, monotonically_increasing_id() as `%s` FROM %s ORDER BY `%s` DESC)",
                    selectSQL,
                    selectSQL,
                    "foo",
                    viewName,
                    "foo"
                )
            );
        String tempName = SparkSessionManager.temporaryColumnName;
        HomogeneousItemDataFrame result = childDataFrame.evaluateSQL(
            String.format(
                "SELECT %s FROM (SELECT %s, monotonically_increasing_id() as `%s` FROM %s ORDER BY `%s` DESC)",
                selectSQL,
                selectSQL,
                tempName,
                viewName,
                tempName
            ),
            childDataFrame.getItemType()
        );
        return result;
    }

    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> sequencePlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private List<Item> results;
        private int currentIndex;

        private EvaluationCursor(
                @NonNull RuntimePlan<Item> sequencePlan,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata
        ) {
            super(metadata);
            this.sequencePlan = sequencePlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.results = new ArrayList<>();
            try (Cursor<Item> childCursor = this.sequencePlan.getCursor(this.context)) {
                while (childCursor.hasNext()) {
                    this.results.add(childCursor.next());
                }
            }
            this.currentIndex = this.results.size() - 1;
        }

        @Override
        protected boolean hasNextLocal() {
            return this.currentIndex >= 0;
        }

        @Override
        protected Item nextLocal() {
            if (this.currentIndex < 0) {
                throw new IteratorFlowException(
                        IteratorFlowException.FLOW_EXCEPTION_MESSAGE + "reverse function",
                        this.metadata
                );
            }
            return this.results.get(this.currentIndex--);
        }

        @Override
        protected void closeLocal() {
            this.results = null;
            this.currentIndex = -1;
        }

    }
}
