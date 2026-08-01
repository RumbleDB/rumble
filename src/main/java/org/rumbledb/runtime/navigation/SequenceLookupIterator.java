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

package org.rumbledb.runtime.navigation;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;
import scala.Tuple2;

import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.runtime.primary.BooleanRuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

public class SequenceLookupIterator extends AbstractAtMostOneItemRuntimePlan implements NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimePlan<Item> iterator;
    private final int position;
    private final int optimizationThreshold = 10_000_000; // do optimization only if position is above this threshold

    public SequenceLookupIterator(
            RuntimePlan<Item> sequence,
            int position,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(sequence), staticContext);
        this.iterator = sequence;
        this.position = position;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        if (this.position <= 0) {
            return null;
        }

        if (this.position < this.optimizationThreshold) {
            return lookupSmallPosition(dynamicContext);
        }

        if (this.iterator.getRuntimeStaticContext().getExecutionMode().isDataFrame()) {
            return lookupDF(dynamicContext);
        }

        if (this.iterator.getRuntimeStaticContext().getExecutionMode().isRDD()) {
            return lookupRDD(dynamicContext);
        }

        if (this.position <= 0) {
            return null;
        }
        List<Item> items = this.iterator.materializeAtMost(dynamicContext, this.position);
        return items.size() == this.position ? items.get(this.position - 1) : null;
    }

    public Item lookupSmallPosition(DynamicContext dynamicContext) {
        List<Item> materializedItems = this.iterator.materializeAtMost(dynamicContext, this.position);
        if (materializedItems.size() >= this.position) {
            return materializedItems.get(this.position - 1);
        } else {
            return null;
        }
    }

    public Item lookupDF(DynamicContext dynamicContext) {
        HomogeneousItemDataFrame df = ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(
            this.iterator,
            dynamicContext
        );
        String input = FlworDataFrameUtils.createTempView(df.getDataFrame());
        df = df.evaluateSQL(
            String.format(
                "SELECT * FROM %s LIMIT 1 OFFSET %s",
                input,
                Integer.toString(this.position - 1)
            ),
            df.getItemType()
        );
        JavaRDD<Item> rdd = df.toRDD(this.getRuntimeStaticContext().getMetadata());

        List<Item> results = rdd.take(1);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    public Item lookupRDD(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(dynamicContext);

        if (childRDD.isEmpty()) {
            return null;
        }
        JavaPairRDD<Item, Long> zippedRDD = childRDD.zipWithIndex();
        JavaPairRDD<Item, Long> filteredRDD;
        filteredRDD = zippedRDD.filter(
            (input) -> input._2() == this.position - 1
        );
        List<Tuple2<Item, Long>> results = filteredRDD.take(1);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0)._1();
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        if (
            nativeClauseContext.getClauseType() == FLWOR_CLAUSES.WHERE
                && this.iterator instanceof CommaExpressionIterator childIterator
        ) {
            List<RuntimePlan<Item>> children = childIterator.getOperands();
            if (
                children.size() == 2
                    && children.get(0) instanceof ComparisonIterator
                    && children.get(1) instanceof BooleanRuntimeIterator
                    && this.position == 1
            ) {
                NativeClauseContext childContext = NativeQueryRuntimePlan.generate(
                    children.get(0),
                    nativeClauseContext
                );
                if (childContext == NativeClauseContext.NoNativeQuery) {
                    return NativeClauseContext.NoNativeQuery;
                }
                return new NativeClauseContext(
                        childContext,
                        childContext.getResultingQuery(),
                        new SequenceType(childContext.getResultingType().getItemType(), SequenceType.Arity.One)
                );
            }
        }
        NativeClauseContext childContext = NativeQueryRuntimePlan.generate(
            this.iterator,
            nativeClauseContext
        );
        if (childContext == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (SequenceType.Arity.OneOrMore.isSubtypeOf(childContext.getResultingType().getArity())) {
            String resultString = String.format(
                "%s[%d]",
                childContext.getResultingQuery(),
                (this.position - 1)
            );
            return new NativeClauseContext(
                    childContext,
                    resultString,
                    new SequenceType(childContext.getResultingType().getItemType(), SequenceType.Arity.OneOrZero)
            );
        }
        return NativeClauseContext.NoNativeQuery;
    }
}
