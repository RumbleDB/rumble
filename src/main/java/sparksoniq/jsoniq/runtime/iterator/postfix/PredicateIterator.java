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

package sparksoniq.jsoniq.runtime.iterator.postfix;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PredicateIterator extends HybridRuntimeIterator {

    private RuntimeIterator _iterator;
    private RuntimeIterator _filter;
    private Item _nextResult;


    public PredicateIterator(RuntimeIterator sequence, RuntimeIterator filterExpression, IteratorMetadata iteratorMetadata) {
        super(Arrays.asList(sequence, filterExpression), iteratorMetadata);
        _iterator = sequence;
        _filter = filterExpression;
    }

    @Override
    protected Item nextLocal() {
        if (_hasNext == true) {
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
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
        _iterator.reset(_currentDynamicContext);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        _iterator.close();
    }

    @Override
    protected void openLocal(DynamicContext context) {
        if (this._children.size() < 2) {
            throw new SparksoniqRuntimeException("Invalid Predicate! Must initialize filter before calling next");
        }

        _iterator.open(_currentDynamicContext);

        setNextResult();
    }

    private void setNextResult() {
        _nextResult = null;

        while (_iterator.hasNext()) {
            Item item = _iterator.next();
            List<Item> currentItems = new ArrayList<>();
            currentItems.add(item);
            _currentDynamicContext.addVariableValue("$$", currentItems);

            _filter.open(_currentDynamicContext);
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
                    //-1 for Jsoniq convention, arrays start from 1
                    _nextResult = sequence.get(index - 1);
                }
                break;
            } else if (Item.getEffectiveBooleanValue(fil)) {
                _nextResult = item;
                break;
            }
            _filter.close();
        }
        _currentDynamicContext.removeVariable("$$");

        if (_nextResult == null) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext dynamicContext) {
        _currentDynamicContext = dynamicContext;
        RuntimeIterator iterator = this._children.get(0);
        RuntimeIterator filter = this._children.get(1);
        JavaRDD<Item> childRDD = iterator.getRDD(dynamicContext);
        Function<Item, Boolean> transformation = new PredicateClosure(filter, dynamicContext);

        JavaRDD<Item> resultRDD = childRDD.filter(transformation);
        return resultRDD;
    }

    @Override
    protected boolean initIsRDD() {
        return this._iterator.isRDD();
    }
}
