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

package org.rumbledb.runtime.postfix;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.spark.SparkSessionManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class ArrayUnboxingIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Queue<Item> nextResults; // queue that holds the results created by the current item in inspection

    public ArrayUnboxingIterator(
            RuntimeIterator arrayIterator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(arrayIterator), executionMode, iteratorMetadata);
        this.iterator = arrayIterator;
    }

    @Override
    public void openLocal() {
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.nextResults = new LinkedList<>();
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResults.remove(); // save the result to be returned
            if (this.nextResults.isEmpty()) {
                // if there are no more results left in the queue, trigger calculation for the next result
                setNextResult();
            }
            return result;
        }
        throw new IteratorFlowException("Invalid next call in Array Unboxing", getMetadata());
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        this.iterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.iterator.close();
    }

    private void setNextResult() {
        while (this.iterator.hasNext()) {
            Item item = this.iterator.next();
            if (item instanceof ArrayItem) {
                ArrayItem arrItem = (ArrayItem) item;
                // if array is not empty, set the first item as the result
                if (0 < arrItem.getSize()) {
                    this.nextResults.addAll(arrItem.getItems());
                    break;
                }
            }
        }

        if (this.nextResults.isEmpty()) {
            this.hasNext = false;
            this.iterator.close();
        } else {
            this.hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.children.get(0).getRDD(dynamicContext);
        FlatMapFunction<Item, Item> transformation = new ArrayUnboxingClosure();
        JavaRDD<Item> resultRDD = childRDD.flatMap(transformation);
        return resultRDD;
    }

    @Override
    public boolean implementsDataFrames() {
        return true;
    }

    public Dataset<Row> getDataFrame(DynamicContext context) {
        Dataset<Row> childDataFrame = this.children.get(0).getDataFrame(context);
        childDataFrame.createOrReplaceTempView("array");
        StructType schema = childDataFrame.schema();
        String[] fieldNames = schema.fieldNames();
        if (
            fieldNames.length == 1 && Arrays.asList(fieldNames).contains(SparkSessionManager.atomicJSONiqItemColumnName)
        ) {
            int i = schema.fieldIndex(SparkSessionManager.atomicJSONiqItemColumnName);
            StructField field = schema.fields()[i];
            DataType type = field.dataType();
            if (type instanceof ArrayType) {
                return childDataFrame.sparkSession()
                    .sql(
                        String.format(
                            "SELECT explode(`%s`) AS `%s` FROM array",
                            SparkSessionManager.atomicJSONiqItemColumnName,
                            SparkSessionManager.atomicJSONiqItemColumnName
                        )
                    );
            }
        }
        return childDataFrame.sparkSession().emptyDataFrame();
    }
}
