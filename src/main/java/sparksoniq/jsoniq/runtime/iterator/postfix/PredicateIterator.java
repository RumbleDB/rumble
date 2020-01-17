/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.runtime.iterator.postfix;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import scala.Tuple2;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.OurBadException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.AndOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.ComparisonOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.NotOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.OrOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.BooleanRuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PredicateIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator _iterator;
    private RuntimeIterator _filter;
    private Item _nextResult;
    private long _position;
    private boolean _mustMaintainPosition;
    private DynamicContext _filterDynamicContext;


    public PredicateIterator(
            RuntimeIterator sequence,
            RuntimeIterator filterExpression,
            IteratorMetadata iteratorMetadata
    ) {
        super(Arrays.asList(sequence, filterExpression), iteratorMetadata);
        _iterator = sequence;
        _filter = filterExpression;
        _filterDynamicContext = null;
    }

    @Override
    protected Item nextLocal() {
        if (_hasNext == true) {
            Item result = _nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Predicate!", getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        _iterator.close();
        _filterDynamicContext = new DynamicContext(_currentDynamicContextForLocalExecution);
        if (_filter.getVariableDependencies().containsKey("$last")) {
            setLast();
        }
        if (_filter.getVariableDependencies().containsKey("$position")) {
            _position = 0;
            _mustMaintainPosition = true;
        }
        _iterator.open(_currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        _iterator.close();
    }

    @Override
    protected void openLocal() {
        if (this._children.size() < 2) {
            throw new OurBadException("Invalid Predicate! Must initialize filter before calling next");
        }
        _filterDynamicContext = new DynamicContext(_currentDynamicContextForLocalExecution);
        if (_filter.getVariableDependencies().containsKey("$last")) {
            setLast();
        }
        if (_filter.getVariableDependencies().containsKey("$position")) {
            _position = 0;
            _mustMaintainPosition = true;
        }
        _iterator.open(_currentDynamicContextForLocalExecution);
        setNextResult();
    }

    private void setLast() {
        long last = 0;
        _iterator.open(_currentDynamicContextForLocalExecution);
        while (_iterator.hasNext()) {
            _iterator.next();
            ++last;
        }
        _iterator.close();
        _filterDynamicContext.setLast(last);
    }

    private void setNextResult() {
        _nextResult = null;

        while (_iterator.hasNext()) {
            Item item = _iterator.next();
            List<Item> currentItems = new ArrayList<>();
            currentItems.add(item);
            _filterDynamicContext.addVariableValue("$$", currentItems);
            if (_mustMaintainPosition)
                _filterDynamicContext.setPosition(++_position);

            _filter.open(_filterDynamicContext);
            Item fil = null;
            if (_filter.hasNext()) {
                fil = _filter.next();
            }
            _filter.close();
            // if filter is an integer, it is used to return the element with the index equal to the given integer
            if (fil instanceof IntegerItem) {
                _iterator.close();
                List<Item> sequence = getItemsFromIteratorWithCurrentContext(_iterator);
                int index = ((IntegerItem) fil).getIntegerValue();
                // less than or equal to size -> b/c of -1
                if (index >= 1 && index <= sequence.size()) {
                    // -1 for Jsoniq convention, arrays start from 1
                    _nextResult = sequence.get(index - 1);
                }
                break;
            } else if (fil != null && fil.getEffectiveBooleanValue()) {
                _nextResult = item;
                break;
            }
            _filter.close();
        }
        _filterDynamicContext.removeVariable("$$");

        if (_nextResult == null) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        _currentDynamicContextForLocalExecution = dynamicContext;
        RuntimeIterator iterator = this._children.get(0);
        RuntimeIterator filter = this._children.get(1);
        JavaRDD<Item> childRDD = iterator.getRDD(dynamicContext);
        if (
            !filter.getVariableDependencies().containsKey("$position")
                && !filter.getVariableDependencies().containsKey("$last")
                && (filter instanceof BooleanRuntimeIterator
                    || filter instanceof AndOperationIterator
                    || filter instanceof OrOperationIterator
                    || filter instanceof NotOperationIterator
                    || filter instanceof ComparisonOperationIterator)
        ) {
            Function<Item, Boolean> transformation = new PredicateClosure(filter, dynamicContext);
            JavaRDD<Item> resultRDD = childRDD.filter(transformation);
            return resultRDD;
        } else {
            JavaPairRDD<Item, Long> zippedChildRDD = childRDD.zipWithIndex();
            long last = 0;
            if (filter.getVariableDependencies().containsKey("$last")) {
                last = childRDD.count();
            }
            Function<Tuple2<Item, Long>, Boolean> transformation = new PredicateClosureZipped(
                    filter,
                    dynamicContext,
                    last
            );
            JavaPairRDD<Item, Long> resultRDD = zippedChildRDD.filter(transformation);
            return resultRDD.keys();
        }
    }

    @Override
    protected boolean initIsRDD() {
        return this._iterator.isRDD();
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result =
            new TreeMap<String, DynamicContext.VariableDependency>();
        result.putAll(_filter.getVariableDependencies());
        result.remove("$");
        result.putAll(_iterator.getVariableDependencies());
        return result;
    }
}
