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

package org.rumbledb.runtime.flwor.expression;

import org.apache.log4j.LogManager;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.navigation.SimpleMapExpressionClosureZipped;
import org.rumbledb.runtime.typing.ValidateTypeIterator;

import scala.Tuple2;
import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SimpleMapExpressionIterator extends HybridRuntimeIterator {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new SimpleMapLocalCursor(this.leftIterator, this.rightIterator, context, getMetadata());
    }

    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator leftIterator;
    private final RuntimeIterator rightIterator;


    public SimpleMapExpressionIterator(
            RuntimeIterator sequence,
            RuntimeIterator mapExpression,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(sequence, mapExpression), staticContext);
        this.leftIterator = sequence;
        this.rightIterator = mapExpression;
    }

    private static final class SimpleMapLocalCursor extends AbstractLocalCursor<Item> {
        private final RuntimeIterator leftPlan;
        private final RuntimeIterator rightPlan;
        private final DynamicContext context;
        private final org.rumbledb.exceptions.ExceptionMetadata metadata;
        private List<Item> inputs;
        private int inputIndex;
        private LocalCursor<Item> currentResults;

        private SimpleMapLocalCursor(
                RuntimeIterator leftPlan,
                RuntimeIterator rightPlan,
                DynamicContext context,
                org.rumbledb.exceptions.ExceptionMetadata metadata
        ) {
            super(metadata);
            this.leftPlan = leftPlan;
            this.rightPlan = rightPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.inputs = this.leftPlan.materialize(this.context);
            this.inputIndex = 0;
        }

        @Override
        protected boolean hasNextLocal() {
            while (this.currentResults == null || !this.currentResults.hasNext()) {
                closeCurrentResults();
                if (this.inputIndex >= this.inputs.size()) {
                    return false;
                }
                DynamicContext mapContext = new DynamicContext(this.context);
                mapContext.getVariableValues()
                    .addVariableValue(
                        Name.CONTEXT_ITEM,
                        List.of(this.inputs.get(this.inputIndex))
                    );
                mapContext.getVariableValues().setPosition(this.inputIndex + 1L);
                mapContext.getVariableValues().setLast(this.inputs.size());
                this.inputIndex++;
                this.currentResults = this.rightPlan.createLocalCursor(mapContext);
            }
            return true;
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw new org.rumbledb.exceptions.IteratorFlowException(
                        "Invalid next() call in simple map expression",
                        this.metadata
                );
            }
            return this.currentResults.next();
        }

        @Override
        protected void closeLocal() {
            closeCurrentResults();
            this.inputs = null;
            this.inputIndex = 0;
        }

        private void closeCurrentResults() {
            if (this.currentResults != null) {
                this.currentResults.close();
                this.currentResults = null;
            }
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.getChild(0).getRDD(dynamicContext);
        JavaPairRDD<Item, Long> zippedChildRDD = childRDD.zipWithIndex();
        long count = childRDD.count();
        FlatMapFunction<Tuple2<Item, Long>, Item> transformation = new SimpleMapExpressionClosureZipped(
                this.rightIterator,
                dynamicContext,
                count
        );
        return zippedChildRDD.flatMap(transformation);
    }

    @Override
    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<Name, DynamicContext.VariableDependency>();
        result.putAll(this.rightIterator.getVariableDependencies());
        result.remove(Name.CONTEXT_ITEM);
        result.putAll(this.leftIterator.getVariableDependencies());
        return result;
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        JSoundDataFrame df = this.leftIterator.getDataFrame(context);
        if (df.isEmptySequence()) {
            return df;
        }
        NativeClauseContext forContext = new NativeClauseContext(
                FLWOR_CLAUSES.FOR,
                df.getDataFrame().schema(),
                context
        );
        NativeClauseContext nativeQuery = this.rightIterator.generateNativeQuery(forContext);
        if (nativeQuery == NativeClauseContext.NoNativeQuery) {
            JavaRDD<Item> rdd = getRDDAux(context);
            JavaRDD<Row> rowRDD = rdd.map(i -> RowFactory.create(i.castToDecimalValue()));
            StructType schema = ValidateTypeIterator.convertToDataFrameSchema(
                getStaticType().getItemType(),
                this.staticContext
            );
            schema.printTreeString();
            Dataset<Row> result = SparkSessionManager.getInstance()
                .getOrCreateSession()
                .createDataFrame(rowRDD, schema);
            return new JSoundDataFrame(result, getStaticType().getItemType());
        }
        LogManager.getLogger("SimpleMapExpressionIterator")
            .info("Rumble was able to optimize a simple map expression to a native SQL query.");
        String input = FlworDataFrameUtils.createTempView(df.getDataFrame());
        Dataset<Row> result = df.getDataFrame()
            .sparkSession()
            .sql(
                String.format(
                    "select %s as `%s` from %s",
                    nativeQuery.getResultingQuery(),
                    SparkSessionManager.nonObjectJSONiqItemColumnName,
                    input
                )
            );
        // execute query
        return new JSoundDataFrame(result, getStaticType().getItemType());
    }


}
