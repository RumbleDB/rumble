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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSelectorException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.ContextExpressionIterator;
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
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(object, lookupIterator), executionMode, iteratorMetadata);
        this._iterator = object;
    }

    private void initLookupKey() {

        RuntimeIterator lookupIterator = this._children.get(1);

        this._contextLookup = lookupIterator instanceof ContextExpressionIterator;

        if (!this._contextLookup) {
            lookupIterator.open(this._currentDynamicContextForLocalExecution);
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
                            + this._lookupKey.serialize(),
                        getMetadata()
                );
            }
            if (this._lookupKey.isNull() || this._lookupKey.isObject() || this._lookupKey.isArray()) {
                throw new UnexpectedTypeException(
                        "Type error; Object selector can't be converted to a string: "
                            + this._lookupKey.serialize(),
                        getMetadata()
                );
            } else {
                // convert to string
                if (this._lookupKey.isBoolean()) {
                    Boolean value = ((BooleanItem) this._lookupKey).getValue();
                    this._lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this._lookupKey.isDecimal()) {
                    BigDecimal value = ((DecimalItem) this._lookupKey).getValue();
                    this._lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this._lookupKey.isDouble()) {
                    Double value = ((DoubleItem) this._lookupKey).getValue();
                    this._lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this._lookupKey.isInteger()) {
                    Integer value = ((IntegerItem) this._lookupKey).getValue();
                    this._lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this._lookupKey.isString()) {
                    // do nothing
                }
            }
            if (!this._lookupKey.isString()) {
                throw new UnexpectedTypeException(
                        "Non string object lookup for " + this._lookupKey.serialize(),
                        getMetadata()
                );
            }
            lookupIterator.close();
        }
    }

    @Override
    public void openLocal() {
        initLookupKey();
        this._iterator.open(this._currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this._hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        this._iterator.reset(this._currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this._iterator.close();
    }

    @Override
    public Item nextLocal() {
        if (this._hasNext) {
            Item result = this._nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Object Lookup", getMetadata());
    }

    public void setNextResult() {
        this._nextResult = null;

        while (this._iterator.hasNext()) {
            Item item = this._iterator.next();
            if (item.isObject()) {
                if (!this._contextLookup) {
                    Item result = item.getItemByKey(this._lookupKey.getStringValue());
                    if (result != null) {
                        this._nextResult = result;
                        break;
                    }
                } else {
                    Item contextItem = this._currentDynamicContextForLocalExecution.getLocalVariableValue(
                        "$$",
                        getMetadata()
                    ).get(0);
                    this._nextResult = item.getItemByKey(contextItem.getStringValue());
                }
            }
        }

        if (this._nextResult == null) {
            this._hasNext = false;
            this._iterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this._children.get(0).getRDD(dynamicContext);
        initLookupKey();
        String key;
        if (this._contextLookup) {
            // For now this will always be an error. Later on we will pass the dynamic context from the parent iterator.
            key = dynamicContext.getLocalVariableValue("$$", getMetadata()).get(0).getStringValue();
        } else {
            key = this._lookupKey.getStringValue();
        }
        FlatMapFunction<Item, Item> transformation = new ObjectLookupClosure(key);

        return childRDD.flatMap(transformation);
    }
}
