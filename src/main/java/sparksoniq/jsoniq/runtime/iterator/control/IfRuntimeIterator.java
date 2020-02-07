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
    private RuntimeIterator selectedIterator = null;
    private Item nextResult = null;

    public IfRuntimeIterator(
            RuntimeIterator condition,
            RuntimeIterator branch,
            RuntimeIterator elseBranch,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.children.add(condition);
        this.children.add(branch);
        this.children.add(elseBranch);
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        this.selectedIterator = null;
        setNextResult();
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.selectedIterator = null;
        setNextResult();
    }

    @Override
    public Item next() {
        if (this.nextResult == null) {
            throw new IteratorFlowException("No next item.");
        }
        Item result = this.nextResult;
        setNextResult();
        return result;
    }

    @Override
    public boolean hasNext() {
        return this.nextResult != null;
    }

    public void setNextResult() {
        if (this.selectedIterator != null && this.nextResult == null) {
            throw new IteratorFlowException("Branch iterator has been fully consumed already.");
        }
        if (this.selectedIterator == null) {
            RuntimeIterator condition = this.children.get(0);
            condition.open(this.currentDynamicContextForLocalExecution);
            boolean effectiveBooleanValue = getEffectiveBooleanValue(condition);
            condition.close();
            if (effectiveBooleanValue) {
                this.selectedIterator = this.children.get(1);
            } else {
                this.selectedIterator = this.children.get(2);
            }
            this.selectedIterator.open(this.currentDynamicContextForLocalExecution);
        }
        if (this.selectedIterator.hasNext()) {
            this.nextResult = this.selectedIterator.next();
        } else {
            this.nextResult = null;
            this.selectedIterator.close();
        }
    }
}
