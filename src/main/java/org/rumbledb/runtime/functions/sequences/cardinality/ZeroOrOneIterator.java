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
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.SequenceExceptionZeroOrOne;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class ZeroOrOneIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;

    public ZeroOrOneIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        RuntimeIterator sequenceIterator = this.children.get(0);
        if (!sequenceIterator.isRDDOrDataFrame()) {
            Item result = null;
            try {
                sequenceIterator.materializeAtMostOneItemOrNull(context);
            } catch (MoreThanOneItemException e) {
                throw new SequenceExceptionZeroOrOne(
                        "fn:zero-or-one() called with a sequence containing more than one item",
                        getMetadata()
                );
            }
            return result;
        } else {
            JavaRDD<Item> rdd = sequenceIterator.getRDD(context);
            List<Item> results = rdd.take(2);
            if (results.size() == 0) {
                return null;
            } else if (results.size() == 1) {
                return results.get(0);
            } else if (results.size() > 1) {
                throw new SequenceExceptionZeroOrOne(
                        "fn:zero-or-one() called with a sequence containing more than one item",
                        getMetadata()
                );
            }
        }
        return null;
    }

}
