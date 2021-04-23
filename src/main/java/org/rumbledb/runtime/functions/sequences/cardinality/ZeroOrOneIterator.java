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

package org.rumbledb.runtime.functions.sequences.cardinality;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.SequenceExceptionZeroOrOne;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class ZeroOrOneIterator extends CardinalityFunctionIterator {


    private static final long serialVersionUID = 1L;
    private Item nextResult;

    public ZeroOrOneIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            return this.nextResult;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " ZERO-OR-ONE function",
                getMetadata()
        );
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        RuntimeIterator sequenceIterator = this.children.get(0);

        if (!sequenceIterator.isRDDOrDataFrame()) {
            sequenceIterator.open(context);
            if (!sequenceIterator.hasNext()) {
                this.hasNext = false;
            } else {
                this.nextResult = sequenceIterator.next();
                if (sequenceIterator.hasNext()) {
                    throw new SequenceExceptionZeroOrOne(
                            "fn:zero-or-one() called with a sequence containing more than one item",
                            getMetadata()
                    );
                } else {
                    this.hasNext = true;
                }
            }
            sequenceIterator.close();
        } else {
            JavaRDD<Item> rdd = sequenceIterator.getRDD(this.currentDynamicContextForLocalExecution);
            List<Item> results = rdd.take(2);
            if (results.size() == 0) {
                this.hasNext = false;
            } else if (results.size() == 1) {
                this.hasNext = true;
                this.nextResult = results.get(0);
            } else if (results.size() > 1) {
                throw new SequenceExceptionZeroOrOne(
                        "fn:zero-or-one() called with a sequence containing more than one item",
                        getMetadata()
                );
            }
        }
    }
}
