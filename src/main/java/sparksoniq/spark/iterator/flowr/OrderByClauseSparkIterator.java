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

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FLWOR_CLAUSES;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.closures.OrderByClauseSortClosure;
import sparksoniq.spark.closures.OrderByMapToPairClosure;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseSparkIteratorExpression;

import java.util.List;

public class OrderByClauseSparkIterator extends FlowrClauseSparkIterator {
    private final boolean _isStable;
    private final List<OrderByClauseSparkIteratorExpression> _expressions;

    public OrderByClauseSparkIterator(RuntimeTupleIterator child, List<OrderByClauseSparkIteratorExpression> expressions,
                                      boolean stable, IteratorMetadata iteratorMetadata) {
        super(child, null, FLWOR_CLAUSES.ORDER_BY, iteratorMetadata);
        this._expressions = expressions;
        this._expressions.forEach(e -> this._children.add(e.getExpression()));
        this._isStable = stable;
    }

    @Override
    public JavaRDD<FlworTuple> getRDD() {
        if(this._rdd == null) {
            this._rdd = this._child.getRDD();
            //map to pairs - ArrayItem [sort keys] , tuples
            JavaPairRDD<FlworKey, FlworTuple> keyTuplePair = this._rdd
                    .mapToPair(new OrderByMapToPairClosure(this._expressions, _isStable));
            //sort by key
            keyTuplePair = keyTuplePair.sortByKey(new OrderByClauseSortClosure(this._expressions, _isStable));
            //map back to tuple RDD
            this._rdd = keyTuplePair.map(tuple2 -> tuple2._2());
        }
        return _rdd;
    }
}
