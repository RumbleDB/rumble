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

package org.rumbledb.runtime.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;


import sparksoniq.spark.SparkSessionManager;

public class RangeOperationIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;
    private long left;
    private long right;
    private long index;

    public RangeOperationIterator(
            RuntimeIterator leftIterator,
            RuntimeIterator rightiterator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(leftIterator, rightiterator), executionMode, iteratorMetadata);
        this.leftIterator = leftIterator;
        this.rightIterator = rightiterator;
    }

    @Override
    public boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            if (this.index == this.right) {
                this.hasNext = false;
            }
            return ItemFactory.getInstance().createLongItem(this.index++);
        }
        throw new IteratorFlowException("Invalid next call in Range Operation", getMetadata());
    }

    public Boolean init(DynamicContext context) {
        Item left;
        Item right;
        try {
            left = this.leftIterator.materializeAtMostOneItemOrNull(this.currentDynamicContextForLocalExecution);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Range expression must have integer input, but instead received more than one item",
                    getMetadata()
            );
        }
        try {
            right = this.rightIterator.materializeAtMostOneItemOrNull(this.currentDynamicContextForLocalExecution);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Range expression must have integer input, but instead received more than one item",
                    getMetadata()
            );
        }
        if (left != null && right != null) {
            if (
                !(left.isInteger())
                    || !(right.isInteger())
            ) {
                throw new UnexpectedTypeException(
                        "Range expression must have integer input, but instead received "
                            +
                            left.getDynamicType()
                            + " and "
                            + right.getDynamicType(),
                        getMetadata()
                );
            }
            try {
                this.left = left.castToIntegerValue().longValue();
                this.right = right.castToIntegerValue().longValue();
                return true;
            } catch (IteratorFlowException e) {
                throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
            }
        }
        return false;
    }

    @Override
    public void openLocal() {
        this.index = 0;
        if (init(this.currentDynamicContextForLocalExecution)) {
            if (this.right < this.left) {
                this.hasNext = false;
            } else {
                this.index = this.left;
                this.hasNext = true;
            }
        } else {
            this.hasNext = false;
        }
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        return null;
    }

    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        if (init(this.currentDynamicContextForLocalExecution)) {
            List<Long> list = new ArrayList<>();
            for (long i = this.left; i <= this.right; i += 200000) {
                list.add(i);
            }
            System.err.println("Number of partitions " + list.size());
            JavaRDD<Long> rdd = SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .parallelize(list, list.size());
            List<StructField> fields = Collections.singletonList(
                DataTypes.createStructField(SparkSessionManager.atomicJSONiqItemColumnName, DataTypes.LongType, true)
            );
            StructType schema = DataTypes.createStructType(fields);

            JavaRDD<Row> rowRDD = rdd.map(i -> RowFactory.create(i));

            // apply the schema to row RDD
            Dataset<Row> df = SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rowRDD, schema);
            df.show();
            return new JSoundDataFrame(df, BuiltinTypesCatalogue.longItem);
        }
        return new JSoundDataFrame(
                SparkSessionManager.getInstance().getOrCreateSession().emptyDataFrame(),
                BuiltinTypesCatalogue.item
        );
    }

    @Override
    protected void closeLocal() {
    }

    @Override
    protected void resetLocal() {
    }
}
