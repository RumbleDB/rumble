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
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;


public class IfRuntimeIterator extends LocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private List<Item> result = null;
    private int currentIndex;

    public IfRuntimeIterator(
            RuntimeIterator condition,
            RuntimeIterator branch,
            RuntimeIterator elseBranch,
            IteratorMetadata iteratorMetadata
    ) {
        super(null, iteratorMetadata);
        this._children.add(condition);
        this._children.add(branch);
        this._children.add(elseBranch);
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        this.result = null;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.result = null;
    }

    @Override
    public Item next() {
        if (result == null) {
            currentIndex = 0;
            RuntimeIterator condition = this._children.get(0);
            RuntimeIterator branch = this._children.get(1);
            RuntimeIterator elseBranch = null;
            if (this._children.size() > 2)
                elseBranch = this._children.get(2);
            condition.open(_currentDynamicContext);
            boolean effectiveBooleanValue = getEffectiveBooleanValue(condition);
            condition.close();
            result = new ArrayList<>();
            if (effectiveBooleanValue) {
                result = getItemsFromIteratorWithCurrentContext(branch);
            } else {
                result = getItemsFromIteratorWithCurrentContext(elseBranch);
            }
        }
        if (currentIndex > result.size() - 1)
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + "If expr", getMetadata());
        if (currentIndex == result.size() - 1)
            this._hasNext = false;
        return result.get(currentIndex++);
    }
}
