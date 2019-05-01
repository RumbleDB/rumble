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
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */
 package sparksoniq.spark.closures;

import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseSparkIteratorExpression;

import java.util.ArrayList;
import java.util.List;

public class OrderByMapToPairClosure implements PairFunction<FlworTuple, FlworKey, FlworTuple> {

    //TODO handle stablility
    private final boolean _isStable;
    private final List<OrderByClauseSparkIteratorExpression> _expressions;

    public OrderByMapToPairClosure(List<OrderByClauseSparkIteratorExpression> expressions, boolean isStable) {
        this._expressions = expressions;
        this._isStable = isStable;
    }

    @Override
    public Tuple2<FlworKey, FlworTuple> call(FlworTuple tuple) throws Exception {
        List<Item> results = new ArrayList<>();
        for(OrderByClauseSparkIteratorExpression orderByExpression : _expressions){
            orderByExpression.getExpression().open(new DynamicContext(tuple));
            while (orderByExpression.getExpression().hasNext()){
                Item resultItem = orderByExpression.getExpression().next();
                if(resultItem != null && !Item.isAtomic(resultItem))
                    throw new NonAtomicKeyException("Order by keys must be atomics",
                            orderByExpression.getIteratorMetadata().getExpressionMetadata());
                results.add(resultItem);
            }
            orderByExpression.getExpression().close();
        }
        FlworKey key = new FlworKey(results);
        return new Tuple2<>(key, tuple);
    }
}
