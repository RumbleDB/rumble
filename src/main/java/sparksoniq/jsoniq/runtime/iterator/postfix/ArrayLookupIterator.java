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
 package sparksoniq.jsoniq.runtime.iterator.postfix;

import sparksoniq.exceptions.InvalidSelectorException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.lang.reflect.Array;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;

public class ArrayLookupIterator extends LocalRuntimeIterator {

    private RuntimeIterator _iterator;
    private Integer _lookup;
    private Item _nextResult;

    public ArrayLookupIterator(RuntimeIterator array, RuntimeIterator iterator, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(array);
        this._children.add(iterator);
    }

    @Override
    public Item next() {
        if(_hasNext == true){
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next call in Array Lookup", getMetadata());
    }
    
    public void initLookupPosition() {
        _iterator = this._children.get(0);
        RuntimeIterator lookupIterator = this._children.get(1);

        lookupIterator.open(_currentDynamicContext);
        Item lookupExpression = null;
        if (lookupIterator.hasNext()) {
            lookupExpression = lookupIterator.next();
        }
        if (lookupIterator.hasNext())
            throw new InvalidSelectorException("\"Invalid Lookup Key; Array lookup can't be performed with multiple keys: "
                    + lookupExpression.serialize(), getMetadata());
        if (!Item.isNumeric(lookupExpression)) {
            throw new UnexpectedTypeException("Type error; Non numeric array lookup for : "
                    + lookupExpression.serialize(), getMetadata());
        }
        lookupIterator.close();

        _lookup = Item.getNumericValue(lookupExpression, Integer.class);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this._currentDynamicContext = context;
        
        initLookupPosition();

        _iterator.open(_currentDynamicContext);
        setNextResult();
    }

    public void setNextResult() {
        _nextResult = null;

        while (_iterator.hasNext()) {
            Item item = _iterator.next();
            if (item instanceof ArrayItem) {
                ArrayItem arrItem = (ArrayItem) item;
                if (_lookup > 0 && _lookup <= arrItem.getSize()) {
                    //-1 for Jsoniq convention, arrays start from 1
                    Item result = arrItem.getItemAt(_lookup - 1);
                    _nextResult = result;
                    break;
                }
            }
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext dynamicContext)
    {
        _currentDynamicContext = dynamicContext;
        JavaRDD<Item> childRDD = this._children.get(0).getRDD(dynamicContext);
        initLookupPosition();
        FlatMapFunction<Item, Item> transformation = new ArrayLookupClosure(_lookup);

        JavaRDD<Item> resultRDD = childRDD.flatMap(transformation);
        return resultRDD;
    }

    @Override
    public boolean isRDD()
    {
        return this._children.get(0).isRDD();
    }

}