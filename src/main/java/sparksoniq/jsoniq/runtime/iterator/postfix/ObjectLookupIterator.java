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

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.InvalidSelectorException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.ContextExpressionIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.math.BigDecimal;
import java.util.Arrays;

public class ObjectLookupIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator _iterator;
    private Item _lookupKey;
    private boolean _contextLookup;
    private Item _nextResult;

    public ObjectLookupIterator(
            RuntimeIterator object,
            RuntimeIterator lookupIterator,
            IteratorMetadata iteratorMetadata
    ) {
        super(Arrays.asList(object, lookupIterator), iteratorMetadata);
        _iterator = object;
    }

    private void initLookupKey() {

        RuntimeIterator lookupIterator = this._children.get(1);

        _contextLookup = lookupIterator instanceof ContextExpressionIterator;

        if (!_contextLookup) {
            lookupIterator.open(_currentDynamicContextForLocalExecution);
            if (lookupIterator.hasNext()) {
                this._lookupKey = lookupIterator.next();
            } else {
                throw new InvalidSelectorException(
                        "Invalid Lookup Key; Object lookup can't be performed with zero keys: ",
                        getMetadata()
                );
            }
            if (lookupIterator.hasNext()) {
                throw new InvalidSelectorException(
                        "\"Invalid Lookup Key; Object lookup can't be performed with multiple keys: "
                            + _lookupKey.serialize(),
                        getMetadata()
                );
            }
            if (_lookupKey.isNull() || _lookupKey.isObject() || _lookupKey.isArray()) {
                throw new UnexpectedTypeException(
                        "Type error; Object selector can't be converted to a string: "
                            + _lookupKey.serialize(),
                        getMetadata()
                );
            } else {
                // convert to string
                if (_lookupKey.isBoolean()) {
                    Boolean value = ((BooleanItem) _lookupKey).getValue();
                    _lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (_lookupKey.isDecimal()) {
                    BigDecimal value = ((DecimalItem) _lookupKey).getValue();
                    _lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (_lookupKey.isDouble()) {
                    Double value = ((DoubleItem) _lookupKey).getValue();
                    _lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (_lookupKey.isInteger()) {
                    Integer value = ((IntegerItem) _lookupKey).getValue();
                    _lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (_lookupKey.isString()) {
                    // do nothing
                }
            }
            if (!_lookupKey.isString()) {
                throw new UnexpectedTypeException(
                        "Non string object lookup for " + _lookupKey.serialize(),
                        getMetadata()
                );
            }
            lookupIterator.close();
        }
    }

    @Override
    public void openLocal() {
        initLookupKey();
        _iterator.open(_currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        _iterator.reset(_currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        _iterator.close();
    }

    @Override
    public Item nextLocal() {
        if (_hasNext) {
            Item result = _nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Object Lookup", getMetadata());
    }

    public void setNextResult() {
        _nextResult = null;

        while (_iterator.hasNext()) {
            Item item = _iterator.next();
            if (item.isObject()) {
                if (!_contextLookup) {
                    Item result = item.getItemByKey(_lookupKey.getStringValue());
                    if (result != null) {
                        _nextResult = result;
                        break;
                    }
                } else {
                    Item contextItem = _currentDynamicContextForLocalExecution.getLocalVariableValue("$$", getMetadata()).get(0);
                    _nextResult = item.getItemByKey(contextItem.getStringValue());
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
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this._children.get(0).getRDD(dynamicContext);
        initLookupKey();
        String key;
        if (_contextLookup) {
            // For now this will always be an error. Later on we will pass the dynamic context from the parent iterator.
            key = dynamicContext.getLocalVariableValue("$$", getMetadata()).get(0).getStringValue();
        } else {
            key = _lookupKey.getStringValue();
        }
        FlatMapFunction<Item, Item> transformation = new ObjectLookupClosure(key);

        return childRDD.flatMap(transformation);
    }

    @Override
    public boolean initIsRDD() {
        return _iterator.isRDD();
    }
}
