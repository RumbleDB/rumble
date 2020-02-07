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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import scala.Tuple2;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.AndOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.ComparisonOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.NotOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.OrOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.BooleanRuntimeIterator;
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
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(sequence, filterExpression), executionMode, iteratorMetadata);
        this._iterator = sequence;
        this._filter = filterExpression;
        this._filterDynamicContext = null;
    }

    @Override
    protected Item nextLocal() {
        if (this._hasNext == true) {
            Item result = this._nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Predicate!", getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return this._hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        this._iterator.close();
        this._filterDynamicContext = new DynamicContext(this._currentDynamicContextForLocalExecution);
        if (this._filter.getVariableDependencies().containsKey("$last")) {
            setLast();
        }
        if (this._filter.getVariableDependencies().containsKey("$position")) {
            this._position = 0;
            this._mustMaintainPosition = true;
        }
        this._iterator.open(this._currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this._iterator.close();
    }

    @Override
    protected void openLocal() {
        if (this._children.size() < 2) {
            throw new OurBadException("Invalid Predicate! Must initialize filter before calling next");
        }
        this._filterDynamicContext = new DynamicContext(this._currentDynamicContextForLocalExecution);
        if (this._filter.getVariableDependencies().containsKey("$last")) {
            setLast();
        }
        if (this._filter.getVariableDependencies().containsKey("$position")) {
            this._position = 0;
            this._mustMaintainPosition = true;
        }
        this._iterator.open(this._currentDynamicContextForLocalExecution);
        setNextResult();
    }

    private void setLast() {
        long last = 0;
        this._iterator.open(this._currentDynamicContextForLocalExecution);
        while (this._iterator.hasNext()) {
            this._iterator.next();
            ++last;
        }
        this._iterator.close();
        this._filterDynamicContext.setLast(last);
    }

    private void setNextResult() {
        this._nextResult = null;

        while (this._iterator.hasNext()) {
            Item item = this._iterator.next();
            List<Item> currentItems = new ArrayList<>();
            currentItems.add(item);
            this._filterDynamicContext.addVariableValue("$$", currentItems);
            if (this._mustMaintainPosition)
                this._filterDynamicContext.setPosition(++this._position);

            this._filter.open(this._filterDynamicContext);
            Item fil = null;
            if (this._filter.hasNext()) {
                fil = this._filter.next();
            }
            this._filter.close();
            // if filter is an integer, it is used to return the element with the index equal to the given integer
            if (fil instanceof IntegerItem) {
                this._iterator.close();
                List<Item> sequence = this._iterator.materialize(this._currentDynamicContextForLocalExecution);
                int index = ((IntegerItem) fil).getIntegerValue();
                // less than or equal to size -> b/c of -1
                if (index >= 1 && index <= sequence.size()) {
                    // -1 for Jsoniq convention, arrays start from 1
                    this._nextResult = sequence.get(index - 1);
                }
                break;
            } else if (fil != null && fil.getEffectiveBooleanValue()) {
                this._nextResult = item;
                break;
            }
            this._filter.close();
        }
        this._filterDynamicContext.removeVariable("$$");

        if (this._nextResult == null) {
            this._hasNext = false;
            this._iterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
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

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result =
            new TreeMap<String, DynamicContext.VariableDependency>();
        result.putAll(this._filter.getVariableDependencies());
        result.remove("$");
        result.putAll(this._iterator.getVariableDependencies());
        return result;
    }
}
