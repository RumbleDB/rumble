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
 package jiqs.spark.iterator.flowr;

import jiqs.jsoniq.compiler.translator.expr.flowr.FLWOR_CLAUSES;
import jiqs.spark.closures.OrderByClauseSortClosure;
import jiqs.spark.closures.OrderByMapToPairClosure;
import jiqs.spark.tuple.FlworKey;
import jiqs.spark.tuple.FlworTuple;
import jiqs.spark.iterator.flowr.expression.OrderByClauseSparkIteratorExpression;
import jiqs.spark.iterator.flowr.base.FlowrClauseSparkIterator;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import java.util.List;

public class OrderByClauseSparkIterator extends FlowrClauseSparkIterator {
    private final boolean _isStable;
    private final List<OrderByClauseSparkIteratorExpression> _expressions;

    public OrderByClauseSparkIterator(List<OrderByClauseSparkIteratorExpression> expressions,
                                         boolean stable) {
        super(null, FLWOR_CLAUSES.ORDER_BY);
        this._expressions = expressions;
        this._expressions.forEach(e -> this._children.add(e.getExpression()));
        this._isStable = stable;
    }

    @Override
    public JavaRDD<FlworTuple> getTupleRDD() {
        if(this._rdd == null) {
            this._rdd = this._previousClause.getTupleRDD();
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
