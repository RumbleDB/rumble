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

package sparksoniq.spark.iterator.function;

import org.apache.spark.api.java.JavaRDD;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.List;

public class ParallelizeFunctionIterator extends SparkFunctionCallIterator {
    public ParallelizeFunctionIterator(List<RuntimeIterator> parameters, IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext context) {
        List<Item> contents = new ArrayList<>();
        RuntimeIterator sequenceIterator = this._children.get(0);
        sequenceIterator.open(context);
        while (sequenceIterator.hasNext())
            contents.add(sequenceIterator.next());
        sequenceIterator.close();
        if (this._children.size() == 1) {
            _rdd = SparkSessionManager.getInstance().getJavaSparkContext().parallelize(contents);
        } else {
            RuntimeIterator partitionsIterator = this._children.get(1);
            partitionsIterator.open(_currentDynamicContext);
            if (!partitionsIterator.hasNext())
                throw new UnexpectedTypeException("The second parameter of parallelize must be an integer, but an empty sequence is supplied.", getMetadata());
            Item partitions = partitionsIterator.next();
            if (!partitions.isInteger()) {
                throw new UnexpectedTypeException("The second parameter of parallelize must be an integer, but a non-integer is supplied.", getMetadata());
            }
            try {
                _rdd = SparkSessionManager.getInstance().getJavaSparkContext().parallelize(contents, partitions.getIntegerValue());
            } catch (Exception e) {
                if (!partitionsIterator.hasNext())
                    throw new SparksoniqRuntimeException("The second parameter of parallelize must be an integer.");
            }
            partitionsIterator.close();
        }
        return _rdd;
    }
}
