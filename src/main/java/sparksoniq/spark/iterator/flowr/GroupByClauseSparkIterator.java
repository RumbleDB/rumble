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
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import sparksoniq.exceptions.InvalidGroupVariableException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.closures.GroupByLinearizeTupleClosure;
import sparksoniq.spark.closures.GroupByToPairMapClosure;
import sparksoniq.spark.iterator.flowr.expression.GroupByClauseSparkIteratorExpression;

import java.util.*;

public class GroupByClauseSparkIterator extends SparkRuntimeTupleIterator {
    private final List<GroupByClauseSparkIteratorExpression> _variables;
    private List<FlworTuple> _localTupleResults;
    private int _resultIndex;


    public GroupByClauseSparkIterator(RuntimeTupleIterator child, List<GroupByClauseSparkIteratorExpression> variables,
                                      IteratorMetadata iteratorMetadata) {
        super(child, iteratorMetadata);
        this._variables = variables;
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

            if (_child.hasNext()) {
                this._hasNext = true;
            } else {
                this._hasNext = false;
            }

        } else {
            throw new SparksoniqRuntimeException("Invalid groupby clause.");
        }
    }

    @Override
    public FlworTuple next() {
        if(_hasNext == true){

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
     * All local results need to be calculated for grouping to be performed.
     */
    private void setAllLocalResults() {
        Map<FlworKey, List<FlworTuple>> keyTuplePairs = mapTuplesToPairs();
        keyTuplePairs.forEach((key, tupleList) -> linearizeTuples(tupleList));

        _child.close();
        if (_localTupleResults.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }


    private HashMap<FlworKey,List<FlworTuple>> mapTuplesToPairs() {
        HashMap<FlworKey, List<FlworTuple>> keyValuePairs = new HashMap<>();

        // assign current context as parent. re-use the same context object for efficiency
        DynamicContext tupleContext = new DynamicContext(_currentDynamicContext);
        while (_child.hasNext()) {
            FlworTuple inputTuple = _child.next();

            List<Item> results = new ArrayList<>();
            for (GroupByClauseSparkIteratorExpression _groupVariable : _variables) {
                tupleContext.removeAllVariables();                     // clear the previous variables
                tupleContext.setBindingsFromTuple(inputTuple);        // assign new variables from new tuple

                // if grouping on an expression
                RuntimeIterator groupVariableExpression = _groupVariable.getExpression();
                if (groupVariableExpression != null) {
                    if (inputTuple.contains(_groupVariable.getVariableReference().getVariableName())) {
                        throw new InvalidGroupVariableException("Group by variable redeclaration is illegal", _groupVariable.getIteratorMetadata());
                    }

                    List<Item> newVariableResults = new ArrayList<>();
                    groupVariableExpression.open(new DynamicContext(inputTuple));
                    while (groupVariableExpression.hasNext()) {
                        Item resultItem = groupVariableExpression.next();
                        if (!Item.isAtomic(resultItem)) {
                            throw new NonAtomicKeyException("Group by keys must be atomics", _groupVariable.getIteratorMetadata().getExpressionMetadata());
                        }
                        newVariableResults.add(resultItem);
                    }
                    groupVariableExpression.close();

                    //if a new variable is declared inside the group by clause, insert value in tuple
                    inputTuple.putValue(_groupVariable.getVariableReference().getVariableName(), newVariableResults, false);
                    results.addAll(newVariableResults);

                } else { // if grouping on a variable reference
                    VariableReferenceIterator groupVariableReference = _groupVariable.getVariableReference();
                    if (!inputTuple.contains(groupVariableReference.getVariableName())) {
                        throw new InvalidGroupVariableException("Variable " + groupVariableReference.getVariableName() + " cannot be used in group clause", _groupVariable.getIteratorMetadata());
                    }

                    groupVariableReference.open(new DynamicContext(inputTuple));
                    while (groupVariableReference.hasNext()) {
                        results.add(groupVariableReference.next());
                    }
                    groupVariableReference.close();
                }
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

    private void linearizeTuples(List<FlworTuple> keyTuplePairs) {
        Iterator<FlworTuple> iterator = keyTuplePairs.iterator();
        FlworTuple oldFirstTuple = iterator.next();
        FlworTuple newTuple = new FlworTuple(oldFirstTuple.getKeys().size());
        for(String tupleVariable : oldFirstTuple.getKeys()){
            iterator = keyTuplePairs.iterator();
            if(_variables.stream().anyMatch( v -> v.getVariableReference().getVariableName().equals(tupleVariable)))
                newTuple.putValue(tupleVariable, oldFirstTuple.getValue(tupleVariable), false);
            else {
                List<Item> allValues = new ArrayList<>();
                while(iterator.hasNext())
                    allValues.addAll(iterator.next().getValue(tupleVariable));
                newTuple.putValue(tupleVariable, allValues, false);
            }
        }
        _localTupleResults.add(newTuple);
    }


    @Override
    public JavaRDD<FlworTuple> getRDD(DynamicContext context) {
        _rdd = this._child.getRDD(context);
        //map to pairs - ArrayItem [sort keys] , tuples
        JavaPairRDD<FlworKey, FlworTuple> keyTuplePair = this._rdd
                .mapToPair(new GroupByToPairMapClosure(_variables));
        //group by key
        JavaPairRDD<FlworKey, Iterable<FlworTuple>> groupedPair =
                keyTuplePair.groupByKey();
        //linearize iterable tuples into arrays
        this._rdd = groupedPair.map(new GroupByLinearizeTupleClosure(_variables));
        return _rdd;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        return null;
    }
}
