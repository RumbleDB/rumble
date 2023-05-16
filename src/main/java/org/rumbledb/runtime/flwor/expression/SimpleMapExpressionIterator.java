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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.typing.ValidateTypeIterator;

import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

public class SimpleMapExpressionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;
    private Item nextResult;
    private DynamicContext mapDynamicContext;
    private Queue<Item> mapValues;


    public SimpleMapExpressionIterator(
            RuntimeIterator sequence,
            RuntimeIterator mapExpression,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(sequence, mapExpression), staticContext);
        this.leftIterator = sequence;
        this.rightIterator = mapExpression;
        this.mapDynamicContext = null;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.children.get(0).getRDD(dynamicContext);
        FlatMapFunction<Item, Item> transformation = new SimpleMapExpressionClosure(this.rightIterator, dynamicContext);
        return childRDD.flatMap(transformation);
    }

    @Override
    protected void openLocal() {
        this.mapDynamicContext = new DynamicContext(this.currentDynamicContextForLocalExecution);
        this.mapValues = new LinkedList<>();
        this.leftIterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.leftIterator.close();
    }

    @Override
    protected void resetLocal() {
        this.mapDynamicContext = new DynamicContext(this.currentDynamicContextForLocalExecution);
        this.mapValues = new LinkedList<>();
        this.leftIterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in simple map expression", getMetadata());
    }

    private void setNextResult() {
        this.nextResult = null;

        if (this.mapValues.size() > 0) {
            this.nextResult = this.mapValues.poll();
            this.hasNext = true;
        } else if (this.leftIterator.hasNext()) {
            List<Item> mapValuesRaw = getRightIteratorValues();
            while (mapValuesRaw.size() == 0 && this.leftIterator.hasNext()) { // Discard all empty sequences
                mapValuesRaw = getRightIteratorValues();
            }

            if (mapValuesRaw.size() == 1) {
                this.nextResult = mapValuesRaw.get(0);
            } else {
                this.mapValues.addAll(mapValuesRaw);
                this.nextResult = this.mapValues.poll();
            }
        }
        if (this.nextResult != null) {
            this.hasNext = true;
        } else {
            this.hasNext = false;
        }
    }

    private List<Item> getRightIteratorValues() {
        Item item = this.leftIterator.next();
        List<Item> currentItems = new ArrayList<>();
        this.mapDynamicContext.getVariableValues().addVariableValue(Name.CONTEXT_ITEM, currentItems);
        currentItems.add(item);
        List<Item> mapValuesRaw = this.rightIterator.materialize(this.mapDynamicContext);
        this.mapDynamicContext.getVariableValues().removeVariable(Name.CONTEXT_ITEM);
        return mapValuesRaw;
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result =
            new TreeMap<Name, DynamicContext.VariableDependency>();
        result.putAll(this.rightIterator.getVariableDependencies());
        result.remove(Name.CONTEXT_ITEM);
        result.putAll(this.leftIterator.getVariableDependencies());
        return result;
    }

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
            StructType schema = ValidateTypeIterator.convertToDataFrameSchema(getStaticType().getItemType());
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
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    input
                )
            );
        // execute query
        return new JSoundDataFrame(result, getStaticType().getItemType());
    }


}
