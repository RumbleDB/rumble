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
 package sparksoniq.spark.iterator.flowr;

import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.SparkRuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.closures.ReturnFlatMapClosure;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;

import java.util.Arrays;

import org.apache.spark.api.java.JavaRDD;

public class ReturnClauseSparkIterator extends SparkRuntimeIterator {
    public ReturnClauseSparkIterator(RuntimeTupleIterator child, RuntimeIterator expression, IteratorMetadata iteratorMetadata) {
        super(Arrays.asList(expression), iteratorMetadata);
        _child = child;
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext context) {
        if(itemRDD == null) {
            ((FlowrClauseSparkIterator)_child).setDynamicContext(context);
            RuntimeIterator expression = this._children.get(0);
            itemRDD = this._child.getRDD().flatMap(new ReturnFlatMapClosure(expression));
        }
        return itemRDD;
    }

    private JavaRDD<Item> itemRDD;
    
    private RuntimeTupleIterator _child;

}
