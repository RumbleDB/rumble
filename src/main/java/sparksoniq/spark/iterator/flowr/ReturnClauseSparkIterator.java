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

import org.apache.spark.api.java.JavaRDD;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.closures.ReturnFlatMapClosure;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;

import java.util.Arrays;

public class ReturnClauseSparkIterator extends HybridRuntimeIterator {

    private JavaRDD<Item> itemRDD;
    private RuntimeTupleIterator _child;
    private RuntimeIterator _iterator;
    private Item _nextLocalResult;

    public ReturnClauseSparkIterator(RuntimeTupleIterator child, RuntimeIterator expression, IteratorMetadata iteratorMetadata) {
        super(Arrays.asList(expression), iteratorMetadata);
        _child = child;
        _iterator = expression;
    }

    @Override
    protected boolean initIsRDD() {
        return _child.isRDD();
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext context) {
        if(itemRDD == null) {
            ((FlowrClauseSparkIterator)_child).setDynamicContext(context);
            RuntimeIterator expression = this._children.get(0);
            itemRDD = this._child.getRDD().flatMap(new ReturnFlatMapClosure(expression));
        }
        return itemRDD;
    }

    @Override
    protected boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    protected Item nextLocal() {
        if(_hasNext == true){
            Item result = _nextLocalResult;  // save the result to be returned
            setNextLocalResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Object Lookup", getMetadata());
    }

    @Override
    protected void openLocal(DynamicContext context) {
        this._currentDynamicContext = context;

        _iterator.open(context);
        setNextLocalResult();
    }

    private void setNextLocalResult() {
        if (_iterator.hasNext()) {
            _nextLocalResult = _iterator.next();
            this._hasNext = true;
        } else {
            _iterator.close();
            this._hasNext = false;
        }
    }

    @Override
    protected void closeLocal() {
        _iterator.close();
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        _iterator.reset(_currentDynamicContext);
        setNextLocalResult();
    }






}
