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
 package jiqs.jsoniq.runtime.iterator.primary;

import jiqs.jsoniq.item.BooleanItem;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import jiqs.exceptions.IteratorFlowException;

public class BooleanRuntimeIterator extends AtomicRuntimeIterator {
    @Override
    public BooleanItem next() {
        if (this._hasNext) {
            this._hasNext = false;
            return new BooleanItem(_item);
        }

        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + this._item);
    }

    public BooleanRuntimeIterator(boolean value) {
        super(null);
        this._item = value;

    }

    private boolean _item;
}

