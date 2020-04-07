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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.RDDRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.List;

public class ParallelizeFunctionIterator extends RDDRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public ParallelizeFunctionIterator(
            List<RuntimeIterator> parameters,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(parameters, executionMode, iteratorMetadata);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> rdd = null;
        List<Item> contents = new ArrayList<>();
        RuntimeIterator sequenceIterator = this.children.get(0);
        sequenceIterator.open(context);
        while (sequenceIterator.hasNext()) {
            contents.add(sequenceIterator.next());
        }
        sequenceIterator.close();
        if (this.children.size() == 1) {
            rdd = SparkSessionManager.getInstance().getJavaSparkContext().parallelize(contents);
        } else {
            RuntimeIterator partitionsIterator = this.children.get(1);
            partitionsIterator.open(this.currentDynamicContextForLocalExecution);
            if (!partitionsIterator.hasNext()) {
                throw new UnexpectedTypeException(
                        "The second parameter of parallelize must be an integer, but an empty sequence is supplied.",
                        getMetadata()
                );
            }
            Item partitions = partitionsIterator.next();
            if (!partitions.isInteger()) {
                throw new UnexpectedTypeException(
                        "The second parameter of parallelize must be an integer, but a non-integer is supplied.",
                        getMetadata()
                );
            }
            try {
                rdd = SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .parallelize(contents, partitions.getIntegerValue());
            } catch (Exception e) {
                if (!partitionsIterator.hasNext()) {
                    throw new UnexpectedTypeException(
                            "The second parameter of parallelize must be an integer.",
                            getMetadata()
                    );
                }
            }
            partitionsIterator.close();
        }
        return rdd;
    }
}
