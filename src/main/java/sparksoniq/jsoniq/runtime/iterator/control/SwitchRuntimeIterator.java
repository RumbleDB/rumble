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

package sparksoniq.jsoniq.runtime.iterator.control;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.Map;


public class SwitchRuntimeIterator extends LocalRuntimeIterator {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final RuntimeIterator testField;
    private final Map<RuntimeIterator, RuntimeIterator> cases;
    private final RuntimeIterator defaultReturn;
    private RuntimeIterator matchingIterator = null;

    public SwitchRuntimeIterator(RuntimeIterator test, Map<RuntimeIterator, RuntimeIterator> cases,
                                 RuntimeIterator defaultReturn, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(test);
        for (RuntimeIterator key : cases.keySet())
            this._children.add(key);
        for (RuntimeIterator value : cases.values())
            this._children.add(value);
        this._children.add(defaultReturn);
        this.testField = test;
        this.cases = cases;
        this.defaultReturn = defaultReturn;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        initializeIterator(testField, cases, defaultReturn);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            matchingIterator.open(_currentDynamicContext);
            Item nextItem = matchingIterator.next();
            matchingIterator.close();
            this._hasNext = false;
            return nextItem;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in switch statement",
                getMetadata());
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        this.matchingIterator = null;
        initializeIterator(testField, cases, defaultReturn);
    }

    private void initializeIterator(RuntimeIterator test, Map<RuntimeIterator, RuntimeIterator> cases,
                                    RuntimeIterator defaultReturn) {
        Item testValue = getSingleItemOfTypeFromIterator(test, Item.class,
                new NonAtomicKeyException("Switch test must be atomic", getMetadata().getExpressionMetadata()));

        if (testValue != null && testValue.isArray()) {
            throw new NonAtomicKeyException("Invalid args. Switch condition can't be an array type", getMetadata().getExpressionMetadata());
        } else if (testValue != null && testValue.isObject()) {
            throw new NonAtomicKeyException("Invalid args. Switch condition  can't be an object type", getMetadata().getExpressionMetadata());
        }

        for (RuntimeIterator caseKey : cases.keySet()) {
            Item caseValue = getSingleItemOfTypeFromIterator(caseKey, Item.class,
                    new NonAtomicKeyException("Switch case test must be atomic", getMetadata().getExpressionMetadata()));

            if (caseValue != null && caseValue.isArray()) {
                throw new NonAtomicKeyException("Invalid args. Switch case can't be an array type", getMetadata().getExpressionMetadata());
            } else if (caseValue != null && caseValue.isObject()) {
                throw new NonAtomicKeyException("Invalid args. Switch case  can't be an object type", getMetadata().getExpressionMetadata());
            }

            // both are empty sequences
            if (testValue == null) {
                if (caseValue == null) {
                    matchingIterator = cases.get(caseKey);
                    break;
                } else {
                    // no match, do nothing
                }
            } else if (caseValue != null && testValue.equals(caseValue)) {
                matchingIterator = cases.get(caseKey);
                break;
            }
        }

        if (matchingIterator == null)
            matchingIterator = defaultReturn;

        matchingIterator.open(_currentDynamicContext);
        this._hasNext = matchingIterator.hasNext();
        matchingIterator.close();
    }
}
