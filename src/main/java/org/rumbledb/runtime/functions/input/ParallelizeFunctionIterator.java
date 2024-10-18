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
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.parsing.RowToItemMapper;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.List;

public class ParallelizeFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private RuntimeIterator partitionsIterator;

    public ParallelizeFunctionIterator(List<RuntimeIterator> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
        this.sequenceIterator = this.children.get(0);
        this.partitionsIterator = null;
        if (this.children.size() > 1) {
            this.partitionsIterator = this.children.get(1);
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> rdd = null;
        List<Item> contents = new ArrayList<>();
        /*if (this.sequenceIterator.isDataFrame()) {
            JSoundDataFrame dataFrame = this.sequenceIterator.getDataFrame(context);
            rdd = dataFrameToRDDOfItems(dataFrame, this.getMetadata());
            // repartition it to the desired amount
        }*/
        this.sequenceIterator.materialize(context, contents);
        if (this.children.size() == 1) {
            rdd = SparkSessionManager.getInstance().getJavaSparkContext().parallelize(contents);
        } else {
            Item partitions = getNumberOfPartitions(context);
            rdd = SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .parallelize(
                    contents,
                    partitions.getIntValue()
                );
        }
        return rdd;
    }

    protected Item getNumberOfPartitions(DynamicContext context) {
        Item partitions = null;
        try {
            partitions = this.partitionsIterator.materializeAtMostOneItemOrNull(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "The second parameter of parallelize must be an integer, but a sequence with more than one item is supplied.",
                    getMetadata()
            );
        }
        if (partitions == null) {
            throw new UnexpectedTypeException(
                    "The second parameter of parallelize must be an integer, but an empty sequence is supplied.",
                    getMetadata()
            );
        }
        if (!partitions.isInteger()) {
            throw new UnexpectedTypeException(
                    "The second parameter of parallelize must be an integer, but a non-integer is supplied.",
                    getMetadata()
            );
        }
        return partitions;
    }

    @Override
    protected void openLocal() {
        this.children.get(0).open(this.currentDynamicContextForLocalExecution);
        if (this.children.size() > 1) {
            getNumberOfPartitions(this.currentDynamicContextForLocalExecution);
        }
    }

    @Override
    protected void closeLocal() {
        this.children.get(0).close();
    }

    @Override
    protected void resetLocal() {
        this.children.get(0).reset(this.currentDynamicContextForLocalExecution);
    }

    @Override
    protected boolean hasNextLocal() {
        return this.children.get(0).hasNext();
    }

    @Override
    protected Item nextLocal() {
        return this.children.get(0).next();
    }
}
