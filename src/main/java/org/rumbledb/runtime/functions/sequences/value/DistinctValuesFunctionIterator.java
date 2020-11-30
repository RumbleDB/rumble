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

package org.rumbledb.runtime.functions.sequences.value;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.MapType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.List;

public class DistinctValuesFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private Item nextResult;
    private List<Item> prevResults;

    public DistinctValuesFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
        this.sequenceIterator = arguments.get(0);
    }

    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "distinct-values function", getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal() {
        this.sequenceIterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.sequenceIterator.close();
    }


    @Override
    public void openLocal() {
        this.prevResults = new ArrayList<>();
        this.sequenceIterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    public void setNextResult() {
        this.nextResult = null;

        while (this.sequenceIterator.hasNext()) {
            Item item = this.sequenceIterator.next();
            if (!item.isAtomic()) {
                throw new NonAtomicKeyException(
                        "Invalid args. distinct-values can't be performed on non-atomics",
                        getMetadata()
                );
            } else {
                if (!this.prevResults.contains(item)) {
                    this.prevResults.add(item);
                    this.nextResult = item;
                    break;
                }
            }
        }

        if (this.nextResult == null) {
            this.hasNext = false;
            this.sequenceIterator.close();
        } else {
            this.hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(dynamicContext);
        Function<Item, Boolean> transformation = new FilterNonAtomicClosure();
        if (childRDD.filter(transformation).isEmpty()) {
            return childRDD.distinct();
        }
        throw new NonAtomicKeyException(
                "Invalid args. distinct-values can't be performed on non-atomics",
                getMetadata()
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        System.out.println("Called.");
        return true;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext dynamicContext) {
        Dataset<Row> df = this.sequenceIterator.getDataFrame(dynamicContext);
        df.show();
        StructType type = df.schema();
        StructField[] fields = type.fields();
        if (fields.length != 1 || !fields[0].name().equals(SparkSessionManager.atomicJSONiqItemColumnName)) {
            throw new NonAtomicKeyException(
                    "Invalid args. distinct-values can't be performed on non-atomics",
                    getMetadata()
            );
        }
        DataType dataType = fields[0].dataType();
        if (dataType instanceof StructType || dataType instanceof ArrayType || dataType instanceof MapType) {
            throw new NonAtomicKeyException(
                    "Invalid args. distinct-values can't be performed on non-atomics",
                    getMetadata()
            );
        }
        df.distinct().show();
        return df.distinct();
    }
}
