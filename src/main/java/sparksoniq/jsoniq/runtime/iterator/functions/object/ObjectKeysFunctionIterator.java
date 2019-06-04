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

package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;

public class ObjectKeysFunctionIterator extends HybridRuntimeIterator {

    private RuntimeIterator _iterator;
    private Queue<Item> _nextResults;   // queue that holds the results created by the current item in inspection
    private List<Item> _prevResults;

    public ObjectKeysFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments,iteratorMetadata);
        _iterator = arguments.get(0);
    }

    @Override
    public void openLocal(DynamicContext context) {
        _iterator.open(context);
        _prevResults = new ArrayList<>();
        _nextResults = new LinkedList<>();

        setNextResult();
    }

    @Override
    public Item nextLocal() {
        if (this._hasNext) {
            Item result = _nextResults.remove();  // save the result to be returned
            if (_nextResults.isEmpty()) {
                // if there are no more results left in the queue, trigger calculation for the next result
                setNextResult();
            }
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " KEYS function",
                getMetadata());
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

    public void setNextResult() {
        while (_iterator.hasNext()) {
            Item item = _iterator.next();
            // ignore non-object items
            if (item.isObject()) {
                StringItem result;
                for (String key : item.getKeys()) {
                    result = new StringItem(key);
                    // check if key was met earlier
                    if (!_prevResults.contains(result)) {
                        _prevResults.add(result);
                        _nextResults.add(result);
                    }
                }
                // if some results are found from the current item, break out of while loop
                if (_nextResults.isEmpty()) {
                    break;
                }
            }
        }

        if (_nextResults.isEmpty()) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext dynamicContext) {
        _currentDynamicContext = dynamicContext;
        JavaRDD<Item> childRDD = _iterator.getRDD(dynamicContext);
        FlatMapFunction<Item, Item> transformation = new ObjectKeysClosure();
        return childRDD.flatMap(transformation).distinct();
    }

    @Override
    public boolean initIsRDD() {
        return _iterator.isRDD();
    }
}
