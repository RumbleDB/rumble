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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
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
            ExceptionMetadata iteratorMetadata
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
        if (this._nextResult == null) {
            throw new IteratorFlowException("No next item.");
        }
        Item result = this._nextResult;
        setNextResult();
        return result;
    }

    @Override
    public boolean hasNext() {
        return this._nextResult != null;
    }

    public void setNextResult() {
        if (this._selectedIterator != null && this._nextResult == null) {
            throw new IteratorFlowException("Branch iterator has been fully consumed already.");
        }
        if (this._selectedIterator == null) {
            RuntimeIterator condition = this._children.get(0);
            condition.open(this._currentDynamicContextForLocalExecution);
            boolean effectiveBooleanValue = getEffectiveBooleanValue(condition);
            condition.close();
            if (effectiveBooleanValue) {
                this._selectedIterator = this._children.get(1);
            } else {
                this._selectedIterator = this._children.get(2);
            }
            this._selectedIterator.open(this._currentDynamicContextForLocalExecution);
        }
        if (this._selectedIterator.hasNext()) {
            this._nextResult = this._selectedIterator.next();
        } else {
            this._nextResult = null;
            this._selectedIterator.close();
        }
    }
}
