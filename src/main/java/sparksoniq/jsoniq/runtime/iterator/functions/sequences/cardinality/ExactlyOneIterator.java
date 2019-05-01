/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.runtime.iterator.functions.sequences.cardinality;

import org.apache.spark.api.java.JavaRDD;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SequenceExceptionExactlyOne;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class ExactlyOneIterator extends CardinalityFunctionIterator {

    private Item _nextResult;

    public ExactlyOneIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            return _nextResult;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " EXACTLY-ONE function",
                getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        RuntimeIterator sequenceIterator = this._children.get(0);

        if (!sequenceIterator.isRDD()) {
            sequenceIterator.open(context);
            if (!sequenceIterator.hasNext()) {
                throw new SequenceExceptionExactlyOne("fn:exactly-one() called with a sequence that doesn't contain exactly one item", getMetadata());
            } else {
                _nextResult = sequenceIterator.next();
                if (sequenceIterator.hasNext()) {
                    throw new SequenceExceptionExactlyOne("fn:exactly-one() called with a sequence that doesn't contain exactly one item", getMetadata());
                } else {
                    this._hasNext = true;
                }
            }
            sequenceIterator.close();
        } else {
            JavaRDD<Item> rdd = sequenceIterator.getRDD(_currentDynamicContext);
            List<Item> results = rdd.take(2);
            if (results.size() == 1) {
                this._hasNext = true;
                _nextResult = results.get(0);
            } else {
                throw new SequenceExceptionExactlyOne("fn:exactly-one() called with a sequence that doesn't contain exactly one item", getMetadata());
            }
        }
    }
}
