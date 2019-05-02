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

package sparksoniq.spark.iterator.flowr;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
    }

    @Override
    public boolean isRDD() {
        return _child.isRDD();
    }

    @Override
    public boolean isDataFrame() {
        // TODO implement letclause and remove the following return statement
        return false;
        // return _child.isDataFrame();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        // isRDD checks omitted, as open is used for non-RDD(local) operations

        if (this._child != null) {
            _child.open(_currentDynamicContext);

            if (_child.hasNext()) {
                this._hasNext = true;
            } else {
                this._hasNext = false;
            }
        } else {
            throw new SparksoniqRuntimeException("Invalid where clause.");
        }
    }

    @Override
    public FlworTuple next() {
        if (_hasNext == true) {
            if (_localTupleResults == null) {
                _localTupleResults = new ArrayList<>();
                _resultIndex = 0;
                setAllLocalResults();
            }

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

    /**
     * Evaluates expressions to atomics(error is thrown if not possible) which are used as keys for sorted TreeMap.
     * Requires _child iterator to be opened.
     *
     * @return Sorted TreeMap(ascending). key - atomics from expressions, value - input tuples
     */
    private TreeMap<FlworKey, List<FlworTuple>> mapExpressionsToOrderedPairs() {
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

                boolean isFieldEmpty = true;
                RuntimeIterator expression = orderByExpression.getExpression();
                expression.open(tupleContext);
                while (expression.hasNext()) {
                    Item resultItem = expression.next();
                    if (resultItem != null && !resultItem.isAtomic())
                        throw new NonAtomicKeyException("Order by keys must be atomics",
                                orderByExpression.getIteratorMetadata().getExpressionMetadata());
                    isFieldEmpty = false;
                    results.add(resultItem);
                }
                // if empty ordering field is found, add a Java null as placeholder
                if (isFieldEmpty) {
                    results.add(null);
                }
                expression.close();
            }
            FlworKey key = new FlworKey(results);
            List<FlworTuple> values = keyValuePairs.get(key);   // all values for a single matching key are held in a list
            if (values == null) {
                values = new ArrayList<>();
                keyValuePairs.put(key, values);
            }
            values.add(inputTuple);
        }
        return keyValuePairs;
    }

    @Override
    public JavaRDD<FlworTuple> getRDD(DynamicContext context) {
        if (this._child == null) {
            throw new SparksoniqRuntimeException("Invalid orderby clause.");
        }

        //map to pairs - ArrayItem [sort keys] , tuples
        JavaPairRDD<FlworKey, FlworTuple> keyTuplePair = this._child.getRDD(context)
                .mapToPair(new OrderByMapToPairClosure(this._expressions, _isStable));
        //sort by key
        keyTuplePair = keyTuplePair.sortByKey(new OrderByClauseSortClosure(this._expressions, _isStable));
        //map back to tuple RDD
        return keyTuplePair.map(tuple2 -> tuple2._2());
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        return null;
    }
}
