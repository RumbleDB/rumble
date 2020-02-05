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

package sparksoniq.jsoniq.runtime.iterator.control;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.IteratorFlowException;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

public class IfRuntimeIterator extends LocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator _selectedIterator = null;
    private Item _nextResult = null;

    public IfRuntimeIterator(
            RuntimeIterator condition,
            RuntimeIterator branch,
            RuntimeIterator elseBranch,
            ExecutionMode executionMode,
            IteratorMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this._children.add(condition);
        this._children.add(branch);
        this._children.add(elseBranch);
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        this._selectedIterator = null;
        setNextResult();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this._selectedIterator = null;
        setNextResult();
    }

    @Override
    public Item next() {
        if (_nextResult == null) {
            throw new IteratorFlowException("No next item.");
        }
        Item result = _nextResult;
        setNextResult();
        return result;
    }

    @Override
    public boolean hasNext() {
        return _nextResult != null;
    }

    public void setNextResult() {
        if (_selectedIterator != null && _nextResult == null) {
            throw new IteratorFlowException("Branch iterator has been fully consumed already.");
        }
        if (_selectedIterator == null) {
            RuntimeIterator condition = this._children.get(0);
            condition.open(_currentDynamicContextForLocalExecution);
            boolean effectiveBooleanValue = getEffectiveBooleanValue(condition);
            condition.close();
            if (effectiveBooleanValue) {
                _selectedIterator = this._children.get(1);
            } else {
                _selectedIterator = this._children.get(2);
            }
            _selectedIterator.open(_currentDynamicContextForLocalExecution);
        }
        if (_selectedIterator.hasNext()) {
            _nextResult = _selectedIterator.next();
        } else {
            _nextResult = null;
            _selectedIterator.close();
        }
    }
}
