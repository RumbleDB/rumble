/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
package sparksoniq.spark.iterator.function;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkContextManager;

public class ParallelizeFunctionIterator extends SparkFunctionCallIterator {
    public ParallelizeFunctionIterator(List<RuntimeIterator> parameters, IteratorMetadata iteratorMetadata) {
        super(parameters, iteratorMetadata);
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext dynamicContext) {

        if (this._rdd == null) {
            List<Item> contents = new ArrayList<>();
            for (RuntimeIterator iterator : this._children) {
                iterator.open(this._currentDynamicContext);
                while (iterator.hasNext())
                    contents.add(iterator.next()/*.serialize()*/);
                iterator.close();
            }
            _rdd = SparkContextManager.getInstance().getContext().parallelize(contents);
        }
        return _rdd;
    }
}
