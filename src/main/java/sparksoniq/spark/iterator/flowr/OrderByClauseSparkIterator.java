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
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.closures.OrderByClauseSortClosure;
import sparksoniq.spark.closures.OrderByMapToPairClosure;
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseSparkIteratorExpression;

import java.util.*;

public class OrderByClauseSparkIterator extends SparkRuntimeTupleIterator {
    private final boolean _isStable;
    private final List<OrderByClauseSparkIteratorExpression> _expressions;

    private List<FlworTuple> _localTupleResults;
    private int _resultIndex;

    public OrderByClauseSparkIterator(RuntimeTupleIterator child, List<OrderByClauseSparkIteratorExpression> expressions,
                                      boolean stable, IteratorMetadata iteratorMetadata) {
        super(child, iteratorMetadata);
        this._expressions = expressions;
        this._isStable = stable;
        _localTupleResults = new ArrayList<>();
        _resultIndex = 0;
    }

    @Override
    public boolean isRDD() {
        return _child.isRDD();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        // isRDD checks omitted, as open is used for non-RDD(local) operations

        if (this._child != null) {
            _child.open(_currentDynamicContext);

            setAllLocalResults();
        } else {
            throw new SparksoniqRuntimeException("Invalid where clause.");
        }
    }

    @Override
    public FlworTuple next() {
        if(_hasNext == true){
            FlworTuple result = _localTupleResults.get(_resultIndex++);
            if (_resultIndex == _localTupleResults.size()) {
                this._hasNext = false;
            }
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in let flwor clause", getMetadata());
    }

    /**
     * All local results need to be calculated for sorting/ordering to be performed.
     */
    private void setAllLocalResults() {
        TreeMap<FlworKey, List<FlworTuple>> keyValuePairs = mapExpressionsToOrderedPairs();
        // get only the values(ordered tuples) and save them in a list for next() calls
        keyValuePairs.forEach((key, valueList) -> valueList.forEach((value) -> _localTupleResults.add(value)));

        _child.close();
        if (_localTupleResults.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    //TODO Solve issue - Associates the specified value with the specified key in this map. If the map previously contained a mapping for the key, the old value is replaced.

    /**
     * Evaluates expressions to atomics(error is thrown if not possible) which are used as keys for sorted TreeMap.
     * Requires _child iterator to be opened.
     * @return Sorted TreeMap(ascending). key - atomics from expressions, value - input tuples
     */
    private TreeMap<FlworKey,  List<FlworTuple>> mapExpressionsToOrderedPairs() {
        // tree map keeps the natural item order deduced from an implementation of Comparator
        // OrderByClauseSortClosure implements a comparator and provides the exact desired behavior for local execution as well
        TreeMap<FlworKey, List<FlworTuple>> keyValuePairs = new TreeMap<>(new OrderByClauseSortClosure(_expressions, true));

        // assign current context as parent. re-use the same context object for efficiency
        DynamicContext tupleContext = new DynamicContext(_currentDynamicContext);
        while (_child.hasNext()) {
            FlworTuple inputTuple = _child.next();

            List<Item> results = new ArrayList<>(); // results from the expressions will become a key
            for (OrderByClauseSparkIteratorExpression orderByExpression : _expressions) {
                tupleContext.removeAllVariables();                     // clear the previous variables
                tupleContext.setBindingsFromTuple(inputTuple);        // assign new variables from new tuple

                RuntimeIterator expression = orderByExpression.getExpression();
                expression.open(tupleContext);
                while (expression.hasNext()) {
                    Item resultItem = expression.next();
                    if (resultItem != null && !Item.isAtomic(resultItem))
                        throw new NonAtomicKeyException("Order by keys must be atomics",
                                orderByExpression.getIteratorMetadata().getExpressionMetadata());
                    results.add(resultItem);
                }
                expression.close();
            }
            FlworKey key = new FlworKey(results);
            if (keyValuePairs.get(key) == null) {
                keyValuePairs.put(key, new ArrayList<>());
            }
            keyValuePairs.get(key).add(inputTuple);
        }
        return keyValuePairs;
    }

    @Override
    public JavaRDD<FlworTuple> getRDD(DynamicContext context) {
        if (this._rdd == null) {
            if (this._child != null) {
                this._rdd = this._child.getRDD(context);
                //map to pairs - ArrayItem [sort keys] , tuples
                JavaPairRDD<FlworKey, FlworTuple> keyTuplePair = this._rdd
                        .mapToPair(new OrderByMapToPairClosure(this._expressions, _isStable));
                //sort by key
                keyTuplePair = keyTuplePair.sortByKey(new OrderByClauseSortClosure(this._expressions, _isStable));
                //map back to tuple RDD
                this._rdd = keyTuplePair.map(tuple2 -> tuple2._2());
            } else {
                throw new SparksoniqRuntimeException("Invalid orderby clause.");
            }
        }
        return _rdd;
    }
}
