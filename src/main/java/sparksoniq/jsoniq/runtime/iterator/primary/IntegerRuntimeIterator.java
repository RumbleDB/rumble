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
package sparksoniq.jsoniq.runtime.iterator.primary;

import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

public class IntegerRuntimeIterator extends AtomicRuntimeIterator {
    public IntegerRuntimeIterator(int value, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._item = value;

    }

    @Override
    public IntegerItem next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + this._item, getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;

        this.result = new IntegerItem(_item, ItemMetadata.fromIteratorMetadata(getMetadata()));
        this._hasNext = true;
    }

    private IntegerItem result;
    private int _item;
}
